package com.metric.collect.metric;


import com.metric.collect.model.CatalogMetric;
import com.metric.collect.repository.CatalogClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Soporte para guardar metricas del mismo tipo, tomadas en distintos thread
 *
 * @param <T>
 *        extend {@link Metric}
 */
public abstract class MetricList<T extends Metric> {

	private final List<T> metricList;

	public MetricList() {
		// Concurrent list
		this.metricList = new CopyOnWriteArrayList<>();
	}

	/**
	 * Metodo para construir las metricas de acuerdo a la cantidad insertadas
	 *
	 * @return Mapa donde la key es un {@link CatalogClient} y el value es un Map<String, Object>
	 *         con la metrica construida
	 */
	public Map<CatalogClient, Map<String, Object>> build(Map<CatalogMetric, MetricList> context) {
		if (this.metricList.size() == 1) {
			return this.buildSingle(context);
		} else if (this.metricList.size() > 1) {
			return this.buildMultiple(context);
		}
		// Esto no debería suceder, dado que la lista nunca puede ser vacia
		throw new RuntimeException("No implement yet");
	}

	/**
	 * Llama al build de la metrica simple insertada en la lista.
	 *
	 * @return Mapa donde la key es un {@link CatalogClient} y el value es un Map<String, Object>
	 *         con la metrica construida
	 */
	private Map<CatalogClient, Map<String, Object>> buildSingle(Map<CatalogMetric, MetricList> context) {
		T metric = this.metricList.get(0);
		return metric.build(context);
	}

	/**
	 * Lanza una excepcion RuntimeException cuando el metodo no esta implementado. Debería crearse
	 * un hijo de MetricList e implementar buildMultiple de acuerdo a la construccion de la metrica
	 * duplicada
	 *
	 * @return Mapa donde la key es un {@link CatalogClient} y el value es un Map<String, Object>
	 *         con la metrica construida
	 */
	protected Map<CatalogClient, Map<String, Object>> buildMultiple(Map<CatalogMetric, MetricList> context) {
		throw new RuntimeException("No implemented yet");
	}

	public void add(final T metric) {
		this.metricList.add(metric);
	}

	public List<T> getMetricList() {
		return this.metricList;
	}

	public List<CatalogClient> getCatalogClients() {
		return this.metricList.get(0).getCatalogClients();
	}

	public CatalogMetric getCatalogMetric() {
		return this.metricList.get(0).getCatalogMetric();
	}
}
