package com.amos.utils.performance.test;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-jamonmonitor-appctx.xml"})
public class JamonMonitorTest {

   @Autowired
   private MonitoredMock mockService;

   public void setMockService(MonitoredMock mockService) {
      this.mockService = mockService;
   }

   @Before
   public void onSetUp() throws Exception {
      MonitorFactory.reset();
   }

   @Test
   public void testJamonAvailable() {
      // test monitor
      int numRows = MonitorFactory.getNumRows();
      Assert.assertEquals(0, numRows);
      mockService.mockMethod();
      numRows = MonitorFactory.getNumRows();
      Assert.assertEquals(1, numRows);
      Object[][] data = MonitorFactory.getData();
      System.out.println(data);
   }

   @Test
   public void testNonMonitoredMethod() throws Exception {
      // test monitor
      int numRows = MonitorFactory.getNumRows();
      Assert.assertEquals(0, numRows);
      mockService.nonMonitoredMethod();
      numRows = MonitorFactory.getNumRows();
      Assert.assertEquals(0, numRows);
   }

   @Test
   public void testJamonWithcustomLabel() {
      // test monitor
      int numRows = MonitorFactory.getNumRows();
      Assert.assertEquals(0, numRows);
      mockService.mockMethodWithCustomMonitor();
      numRows = MonitorFactory.getNumRows();
      Assert.assertEquals(1, numRows);
      // check label mockingbird!
      Monitor timeMonitor = MonitorFactory.getTimeMonitor("mockingbird - mockMethodWithCustomMonitor");
      Assert.assertNotNull(timeMonitor);
      Assert.assertEquals(1d, timeMonitor.getHits());
   }

}
