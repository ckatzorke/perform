package oss.perform.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking a method to be performance-monitored.<br>
 * Contains the attributes {@link #label()} that will be used as identifier for this performance-monitor.<br>
 * The label can have EL arguments for referencing the target class and target method
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Performance {
	String label() default "";
}
