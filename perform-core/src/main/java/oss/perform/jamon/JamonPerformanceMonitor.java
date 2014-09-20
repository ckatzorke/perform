package oss.perform.jamon;

import oss.perform.core.MonitoredTarget;
import oss.perform.core.PerformanceMonitor;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

public class JamonPerformanceMonitor implements PerformanceMonitor {
	private Monitor monitor;

	@Override
	public void start(MonitoredTarget target) {
		monitor = MonitorFactory.start(target.getTargetClass() + "#" + target.getTargetMethod() + "(..)");
	}

	@Override
	public void stop() {
		monitor.stop();
	}

}
