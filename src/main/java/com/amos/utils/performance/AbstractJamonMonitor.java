package com.amos.utils.performance;

import org.aspectj.lang.ProceedingJoinPoint;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

public abstract class AbstractJamonMonitor implements PerfMonitor {

   private String identifier = "";
   public boolean completeSignature = true;

   public Object monitorJamonMonitored(ProceedingJoinPoint jp, JamonMonitored jamonMonitored) throws Throwable {
      // only when custom identifier exists
      if (jamonMonitored != null && !"".equals(jamonMonitored.identifier())) {
         String id = jamonMonitored.identifier();
         boolean completeSig = jamonMonitored.completeSignature();
         return monitor(jp, id, completeSig);
      }
      return monitor(jp, identifier, completeSignature);
   }

   public Object monitor(ProceedingJoinPoint jp) throws Throwable {
      // call #monitor(ProceedingJoinPoint, JamonMonitored)
      return monitorJamonMonitored(jp, null);
   }

   protected Object monitor(ProceedingJoinPoint jp, String id, boolean completeSig) throws Throwable {
      String label = getLabel(jp, id, completeSig);
      Monitor monitor = MonitorFactory.start(label);
      Object ret = null;
      try {
         ret = jp.proceed();
      }
      catch (Exception e) {
         throw e;
      }
      finally {
         monitor.stop();
      }
      return ret;

   }

   private String getLabel(ProceedingJoinPoint jp, String id, boolean completeSig) {
      StringBuffer label = new StringBuffer("");

      if (id != null && !"".equals(id)) {
         label.append(id).append(" - ");
      }
      if (completeSig) {
         label.append(jp.getSignature().getDeclaringType().getName());
         label.append("#");
      }
      label.append(jp.getSignature().getName());
      return label.toString();
   }

   public void setIdentifier(String identifier) {
      this.identifier = identifier;
   }

   public void setCompleteSignature(boolean completeSignature) {
      this.completeSignature = completeSignature;
   }

}
