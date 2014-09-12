package com.amos.utils.performance;

import org.aspectj.lang.ProceedingJoinPoint;

import com.jamonapi.Monitor;

/**
 * aop monitor to perform performance monitoring with eg jamon. Implemented as Around advice, which starts an {@link Monitor}
 * instance before proceeding with the execution and stops it afterwards (i.e. it guarantees the consistency).
 * 
 */
public interface PerfMonitor {

   /**
    * set identifier for the jamon monitor - can be empty
    * 
    * @param identifier
    */
   void setIdentifier(String identifier);

   /**
    * Set to <code>true</code> (default) when the complete class/method signature should be used for the monitor label. When false,
    * only the methodname is used
    * 
    * @param completeSignature
    */
   public void setCompleteSignature(boolean completeSignature);

   /**
    * aop around-monitor
    * 
    * @param jp
    * @param jamonMonitorAnnotation
    * @return
    */
   Object monitorJamonMonitored(ProceedingJoinPoint jp, JamonMonitored jamonMonitorAnnotation) throws Throwable;

   /**
    * aop around monitor for usage without annontations
    * 
    * @param jp
    * @return
    * @throws Throwable
    */
   Object monitor(ProceedingJoinPoint jp) throws Throwable;

}
