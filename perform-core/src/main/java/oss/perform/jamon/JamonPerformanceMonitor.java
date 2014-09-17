package oss.perform.jamon;

import oss.perform.core.PerformanceMonitor;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

public class JamonPerformanceMonitor implements PerformanceMonitor {
	private Monitor monitor;

	@Override
	public void start(String label) {
		monitor = MonitorFactory.start(label);
	}

	@Override
	public void stop() {
		monitor.stop();
	}

}
