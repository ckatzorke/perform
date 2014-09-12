package com.amos.utils.performance.test;

import org.junit.Assert;
import org.junit.Test;

import com.amos.utils.metrics.filter.MetricsFilter;

public class TestGuessContent {
   @Test
   public void testGuess() throws Exception {
      Assert.assertTrue(MetricsFilter.isHtml("/"));
      Assert.assertTrue(MetricsFilter.isHtml(""));
      Assert.assertTrue(MetricsFilter.isHtml("/test"));
      Assert.assertTrue(MetricsFilter.isHtml("/www.allianz.com/index"));
      Assert.assertTrue(MetricsFilter.isHtml("/www.allianz.com/index/"));
      Assert.assertTrue(MetricsFilter.isHtml("/www.allianz.com/index.html"));
      Assert.assertTrue(MetricsFilter.isHtml("/www.allianz.com/index.html/"));

      Assert.assertFalse(MetricsFilter.isHtml("/www.allianz.com/logo.png"));
      Assert.assertFalse(MetricsFilter.isHtml("/logo.png"));
      Assert.assertFalse(MetricsFilter.isHtml("style.css"));
   }
}
