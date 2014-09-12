package com.amos.utils.metrics.filter;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;

public class HealthCheckRegistryContextListener extends HealthCheckServlet.ContextListener {

   @Override
   protected HealthCheckRegistry getHealthCheckRegistry() {
      return MetricsFilter.healthCheckRegistry;
   }

}
