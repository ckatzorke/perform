package oss.perform.core;

import java.lang.annotation.Annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * The {@link Aspect} for the {@link Annotation} {@link Performance}.<br>
 * Sample xml config:
 *
 * <pre>
 * &lt;bean id=&quot;performanceAspect&quot; class=&quot;oss.perform.PerformanceAspect&quot;&gt;
 * &lt;/bean&gt;
 * </pre>
 *
 *
 */
@Aspect
public class PerformanceAspect {

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Aspect joinpoint for pointcuts <code>@Around("@annotation(performance)")</code>
	 *
	 * @param pjp
	 * @param performance
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(performance)")
	public Object monitorPerformance(ProceedingJoinPoint pjp, Performance performance) throws Throwable {
		return monitor(pjp, getTarget(pjp));
	}

	/**
	 * Monitor method that can be used for pointcuts to be monitored that are not (for any reason) annotated with
	 * {@link Performance}.
	 *
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	public Object monitor(ProceedingJoinPoint pjp) throws Throwable {
		return monitor(pjp, getTarget(pjp));
	}

	/**
	 * @param jp
	 * @param target
	 * @return
	 * @throws Throwable
	 */
	private Object monitor(ProceedingJoinPoint jp, MonitoredTarget target) throws Throwable {
		final PerformanceMonitor performanceMonitor = applicationContext.getBean(PerformanceMonitor.class);
		performanceMonitor.start(target);
		Object ret = null;
		try {
			ret = jp.proceed();
		} catch (final Exception e) {
			throw e;
		} finally {
			performanceMonitor.stop();
		}
		return ret;

	}

	private MonitoredTarget getTarget(ProceedingJoinPoint pjp) {
		final MonitoredTarget target = new MonitoredTarget(pjp.getSignature().getDeclaringTypeName(), pjp.getSignature()
				.getName());
		return target;
	}
}
