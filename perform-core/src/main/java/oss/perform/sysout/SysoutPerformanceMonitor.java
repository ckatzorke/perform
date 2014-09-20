package oss.perform.sysout;

import oss.perform.core.MonitoredTarget;
import oss.perform.core.PerformanceMonitor;

public class SysoutPerformanceMonitor implements PerformanceMonitor {

	private long start;
	private String label;

	@Override
	public void start(MonitoredTarget target) {
		label = target.getTargetClass() + "#" + target.getTargetMethod() + "(..)";
		start = System.currentTimeMillis();
	}

	@Override
	public void stop() {
		final long runtime = System.currentTimeMillis() - start;
		System.out.println(label + ": " + runtime + "[ms]");
	}

}
