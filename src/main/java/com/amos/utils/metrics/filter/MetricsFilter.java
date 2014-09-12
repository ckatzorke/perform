package com.amos.utils.metrics.filter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.web.filter.OncePerRequestFilter;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

public class MetricsFilter extends OncePerRequestFilter {

   private static final Logger LOGGER = Logger.getLogger(MetricsFilter.class.getCanonicalName());

   private static final int UNKNOWN = 0;

   // private PageImpressionHandler handler = null;

   private Histogram responseTimes;

   private Map<Integer, Meter> htmlMetersByStatusCodes;
   private Map<Integer, Meter> mediaMetersByStatusCodes;

   private Counter activeRequests;

   private Timer htmlRequestTimer;
   private Timer mediaRequestTimer;

   public static MetricRegistry metricsRegistry = new MetricRegistry();

   public static HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

   @Override
   protected void initFilterBean() throws ServletException {
      this.htmlMetersByStatusCodes = new ConcurrentHashMap<Integer, Meter>();
      this.mediaMetersByStatusCodes = new ConcurrentHashMap<Integer, Meter>();
      htmlMetersByStatusCodes.put(
            HttpServletResponse.SC_OK,
            metricsRegistry.meter(MetricRegistry.name(HttpServletRequest.class, "HTML_OK", "responses",
                  String.valueOf(TimeUnit.SECONDS))));
      htmlMetersByStatusCodes.put(
            HttpServletResponse.SC_FORBIDDEN,
            metricsRegistry.meter(MetricRegistry.name(HttpServletRequest.class, "HTML_NOTAUTHORIZED", "responses",
                  String.valueOf(TimeUnit.SECONDS))));
      htmlMetersByStatusCodes.put(
            HttpServletResponse.SC_NOT_FOUND,
            metricsRegistry.meter(MetricRegistry.name(HttpServletRequest.class, "HTML_NOTFOUND", "responses",
                  String.valueOf(TimeUnit.SECONDS))));
      htmlMetersByStatusCodes.put(
            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            metricsRegistry.meter(MetricRegistry.name(HttpServletRequest.class, "HTML_INTERNALERROR", "responses",
                  String.valueOf(TimeUnit.SECONDS))));
      htmlMetersByStatusCodes.put(
            UNKNOWN,
            metricsRegistry.meter(MetricRegistry.name(HttpServletRequest.class, "HTML_OTHER", "responses",
                  String.valueOf(TimeUnit.SECONDS))));
      mediaMetersByStatusCodes.put(
            HttpServletResponse.SC_OK,
            metricsRegistry.meter(MetricRegistry.name(HttpServletRequest.class, "MEDIA_OK", "responses",
                  String.valueOf(TimeUnit.SECONDS))));
      mediaMetersByStatusCodes.put(
            HttpServletResponse.SC_FORBIDDEN,
            metricsRegistry.meter(MetricRegistry.name(HttpServletRequest.class, "MEDIA_NOTAUTHORIZED", "responses",
                  String.valueOf(TimeUnit.SECONDS))));
      mediaMetersByStatusCodes.put(
            HttpServletResponse.SC_NOT_FOUND,
            metricsRegistry.meter(MetricRegistry.name(HttpServletRequest.class, "MEDIA_NOTFOUND", "responses",
                  String.valueOf(TimeUnit.SECONDS))));
      mediaMetersByStatusCodes.put(
            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            metricsRegistry.meter(MetricRegistry.name(HttpServletRequest.class, "MEDIA_INTERNALERROR", "responses",
                  String.valueOf(TimeUnit.SECONDS))));
      mediaMetersByStatusCodes.put(
            UNKNOWN,
            metricsRegistry.meter(MetricRegistry.name(HttpServletRequest.class, "MEDIA_OTHER", "responses",
                  String.valueOf(TimeUnit.SECONDS))));

      this.activeRequests = metricsRegistry.counter(MetricRegistry.name(HttpServletRequest.class, "activeRequests"));
      this.htmlRequestTimer = metricsRegistry.timer(MetricRegistry.name(HttpServletRequest.class, "html-requests",
            String.valueOf(TimeUnit.SECONDS)));
      this.mediaRequestTimer = metricsRegistry.timer(MetricRegistry.name(HttpServletRequest.class, "media-requests",
            String.valueOf(TimeUnit.SECONDS)));

      this.responseTimes = metricsRegistry.histogram(MetricRegistry.name(HttpServletRequest.class, "response-time"));

      final JmxReporter reporter = JmxReporter.forRegistry(metricsRegistry).build();
      reporter.start();

   }

   @Override
   public void destroy() {

   }

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
         throws ServletException, IOException {
      final StatusExposingServletResponse wrappedResponse = new StatusExposingServletResponse((HttpServletResponse) response);
      activeRequests.inc();
      String requestedPath = request.getRequestURL().toString();
      String requestedUri = request.getRequestURI();
      Timer timer = null;
      boolean isHtml = isHtml(requestedUri);
      if (isHtml) {
         timer = htmlRequestTimer;
      }
      else {
         timer = mediaRequestTimer;
      }
      final Timer.Context context = timer.time();
      Monitor jamon = MonitorFactory.start(isHtml ? "content" : "media");
      try {
         filterChain.doFilter(request, wrappedResponse);
      }
      finally {
         long rt = context.stop();
         rt = TimeUnit.MILLISECONDS.convert(rt, TimeUnit.NANOSECONDS);
         responseTimes.update(rt);
         jamon.stop();
         activeRequests.dec();
         markMeterForStatusCode(wrappedResponse.getStatus(), isHtml);
         if (rt > 5000) {
            LOGGER.severe("Performance::" + requestedPath + " [" + rt + " ms]");
         }
      }
   }

   private void markMeterForStatusCode(int status, boolean isHtml) {
      Meter metric = isHtml ? htmlMetersByStatusCodes.get(status) : mediaMetersByStatusCodes.get(status);
      if (metric == null) {
         metric = isHtml ? htmlMetersByStatusCodes.get(0) : mediaMetersByStatusCodes.get(0);
      }
      metric.mark();
   }

   public static boolean isHtml(String requestedUri) {
      // would be better to check the content type - but this will be defined later, so just guess
      // check filetype
      boolean html = false;
      if (requestedUri.endsWith("/")) {
         // due to seo optimized trailing slashes, it is content
         html = true;
      }
      else {
         // get file or folder
         String fileOrFolder = requestedUri;
         if (requestedUri.lastIndexOf('/') > -1) {
            fileOrFolder = requestedUri.substring(requestedUri.lastIndexOf('/'));
         }
         String mime = "html";
         if (fileOrFolder.lastIndexOf('.') > -1) {
            mime = fileOrFolder.substring(fileOrFolder.lastIndexOf('.'));
            // check for htm
            if (mime.startsWith(".htm")) {
               html = true;
            }
         }
         else {
            // no '.'
            html = true;
         }
      }

      return html;
   }

   private static class StatusExposingServletResponse extends HttpServletResponseWrapper {
      // The Servlet spec says: calling setStatus is optional, if no status is set, the default is 200.
      private int httpStatus = 200;

      public StatusExposingServletResponse(HttpServletResponse response) {
         super(response);
      }

      @Override
      public void sendError(int sc) throws IOException {
         httpStatus = sc;
         super.sendError(sc);
      }

      @Override
      public void sendError(int sc, String msg) throws IOException {
         httpStatus = sc;
         super.sendError(sc, msg);
      }

      @Override
      public void setStatus(int sc) {
         httpStatus = sc;
         super.setStatus(sc);
      }

      public int getStatus() {
         return httpStatus;
      }
   }

}
