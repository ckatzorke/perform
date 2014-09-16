# Perform - a library for monitoring and visualizing performance

The library relies heavily on Spring and Spring AOP.

Feature list:

 * Annotation driven Aspect (@Performance) for easy monitoring of your managed beans
 * Fallback for configurable pointcut definition
 * Configurable, EL supported labels for your monitors (TODO)
 * Implementations for JAMon, Simon (TODO), Metrics (TODO), Logfile for use with Logstash and Kibana (TODO)

## How to use

Configure your application context

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">
	<aop:aspectj-autoproxy proxy-target-class="false" />

	<!-- The Aspect for @Performance -->
	<bean id="performance" class="oss.perform.core.PerformanceAspect"/>

	<!-- the PerformanceMonitor Impl -->
	<bean id="sysout" class="oss.perform.sysout.SysoutPerformanceMonitor"/>
	
	<!-- Pointcut declaration for monitoring without annotation -->
	<aop:config proxy-target-class="false">
		<aop:aspect ref="performance">
			<aop:pointcut id="monitorWithoutAnnotation" expression="execution(public void your.package.YourService.methodWIthoutAnnotation())" />
			<aop:around pointcut-ref="monitorWithoutAnnotation" method="monitor" />
		</aop:aspect>
	</aop:config>

	<bean id="yourService" class="your.package.YourService" />
</beans>
```

In your Managed Bean
```java
@Performance
public void annotatedMethodToBeMonitored(){
....
}
```

## Available implementations

### JAMon