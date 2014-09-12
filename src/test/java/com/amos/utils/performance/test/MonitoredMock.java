package com.amos.utils.performance.test;

import com.amos.utils.performance.JamonMonitored;

public class MonitoredMock {

   @JamonMonitored
   public void mockMethod() {
      System.out.println("just for checking if Jamon works...");
   }

   public void nonMonitoredMethod() {
      System.out.println("no monitor for this one...");
   }

   @JamonMonitored(identifier = "mockingbird", completeSignature = false)
   public void mockMethodWithCustomMonitor() {
      System.out.println("monitor label = mockingbird, completesig = false");
   }

   public void mockMethodWithOutAnnotation() {
      System.out.println("mockMethodWithOutAnnotation");
   }

}
