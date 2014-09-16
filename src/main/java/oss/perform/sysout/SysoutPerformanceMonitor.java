package oss.perform.sysout;

import oss.perform.core.PerformanceMonitor;

public class SysoutPerformanceMonitor implements PerformanceMonitor {

	private long start;
	private String label;

	@Override
	public void start(String label) {
		this.label = label;
		start = System.currentTimeMillis();
	}

	@Override
	public void stop() {
		final long runtime = System.currentTimeMillis() - start;
		System.out.println("PERFORMANCE::" + label + ": " + runtime + "[ms]");
	}

}
