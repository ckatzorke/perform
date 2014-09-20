package oss.perform.core;

/**
 * The targeted method to be monitored, wrapped in a bean.
 *
 * @author ckatzorke
 *
 */
public class MonitoredTarget {

	private final String targetClass;
	private final String targetMethod;

	public MonitoredTarget(String targetClass, String targetMethod) {
		this.targetClass = targetClass;
		this.targetMethod = targetMethod;
	}

	public String getTargetClass() {
		return targetClass;
	}

	public String getTargetMethod() {
		return targetMethod;
	}

}
