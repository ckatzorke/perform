package oss.perform.core;

import org.springframework.context.ApplicationContext;

/**
 * Interface for specific implementations that provide functionality for monitoring performance.<br>
 * Implementations must be configured properly in {@link ApplicationContext}, e.g providing correct scope (SINGLETON,
 * PROTOTYPE) and possible factory/instantiation methods.
 */
public interface PerformanceMonitor {

	/**
	 * Start the monitor for this label (before invocation of target method)
	 *
	 * @param label
	 */
	void start(String label);

	/**
	 * Stop the monitor (after invocation of target method)
	 */
	void stop();
}
