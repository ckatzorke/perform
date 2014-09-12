package com.amos.utils.performance;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Monitor that can be used as aspect. Sample config:
 * 
 * <pre>
 * &lt;bean id=&quot;jamonMonitor&quot; class=&quot;de.allianz.portal.core.utils.aop.JamonMonitor&quot;&gt;
 *      &lt;!-- prefix --&gt;
 *      &lt;property name=&quot;identifier&quot; value=&quot;yourcustomprefix::&quot; /&gt;
 *      &lt;!-- set to true if the complete class signature should be prefixed --&gt;
 *      &lt;property name=&quot;completeSignature&quot; value=&quot;true&quot;/&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * 
 */
@Aspect
public class JamonMonitor extends AbstractJamonMonitor implements PerfMonitor {

   @Around("@annotation(jamonMonitored)")
   public Object monitorJamonMonitored(ProceedingJoinPoint jp, JamonMonitored jamonMonitored) throws Throwable {
      return super.monitorJamonMonitored(jp, jamonMonitored);
   }
}
