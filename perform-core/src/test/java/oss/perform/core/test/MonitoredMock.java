package oss.perform.core.test;

import oss.perform.core.Performance;

public class MonitoredMock {

	@Performance
	public void annotatedMethod() throws InterruptedException {
		System.out.println("annotatedMethod() Taking 100ms...");
		Thread.sleep(100l);
	}

	public void nonMonitoredMethod() {
		System.out.println("nonMonitoredMethod()");
	}

	public void nonAnnotatedWithCustomPointcut() throws InterruptedException {
		System.out.println("nonAnnotatedWithCustomPointcut(). Taking 100ms");
		Thread.sleep(100l);
	}
}
