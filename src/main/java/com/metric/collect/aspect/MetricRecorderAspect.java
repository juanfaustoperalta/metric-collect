package com.metric.collect.aspect;


import com.metric.collect.service.MetricCollect;
import com.metric.collect.service.MetricSender;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class MetricRecorderAspect {

	private MetricSender metricSender;

	@Before("@annotation(metricRecorder)")
	public void beforeMethodController(final MetricRecorder metricRecorder) {
		MetricCollect.init();
	}

	@After("@annotation(metricRecorder)")
	public void afterMethodController(final MetricRecorder metricRecorder) {
		metricSender.sendMetric(MetricCollect.getContext());
		MetricCollect.destroy();
	}

	public void setMetricSender(MetricSender metricSender) {
		this.metricSender = metricSender;
	}
}
