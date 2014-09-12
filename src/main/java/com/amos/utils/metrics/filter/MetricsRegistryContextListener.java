package com.amos.utils.metrics.filter;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;

public class MetricsRegistryContextListener extends MetricsServlet.ContextListener {

   @Override
   protected MetricRegistry getMetricRegistry() {
      return MetricsFilter.metricsRegistry;
   }

}
