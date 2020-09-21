package com.metric.collect.model;

/**
 * Catalog de metricas para estandarizar los nombres
 */
public enum CatalogMetric {

	EXOGORTH("exogorth"), CASSANDRA("cassandra"), ELASTIC("elastic"), EXECUTION_TIME("execution_time"), POST_TRACKING("post_tracking");

	private final String name;

	CatalogMetric(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
