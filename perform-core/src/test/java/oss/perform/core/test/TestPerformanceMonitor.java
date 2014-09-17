package oss.perform.core.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import oss.perform.core.PerformanceMonitor;

/**
 * Basic test using Mockito
 * 
 * @author ckatzorke
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-performance.xml" })
public class TestPerformanceMonitor {

	@Autowired
	private MonitoredMock mockService;

	@Autowired
	private PerformanceMonitor performanceMonitor;

	@Before
	public void onSetUp() throws Exception {
		Mockito.reset(performanceMonitor);
	}

	@Test
	public void testAnnotatedMethod() throws Exception {
		mockService.annotatedMethod();
		Mockito.verify(performanceMonitor, Mockito.times(1)).start(Mockito.anyString());
		Mockito.verify(performanceMonitor, Mockito.times(1)).stop();
	}

	@Test
	public void testNonAnnotatedMethod() throws Exception {
		mockService.nonAnnotatedWithCustomPointcut();
		Mockito.verify(performanceMonitor, Mockito.times(1)).start(Mockito.anyString());
		Mockito.verify(performanceMonitor, Mockito.times(1)).stop();
	}

	@Test
	public void testNonMonitoredMethod() throws Exception {
		mockService.nonMonitoredMethod();
		Mockito.verify(performanceMonitor, Mockito.never()).start(Mockito.anyString());
		Mockito.verify(performanceMonitor, Mockito.never()).stop();
	}
}
