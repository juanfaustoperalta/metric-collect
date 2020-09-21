package com.metric.collect.service;

import com.metric.collect.metric.Metric;
import com.metric.collect.metric.MetricList;
import com.metric.collect.model.CatalogMetric;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Recoletor de metricas basado en un threadLocal por request
 *
 */
public class MetricCollect {

	private final static ThreadLocal<Map<CatalogMetric, MetricList>> localCache = new InheritableThreadLocal<>();

	/**
	 * inicializa un concurrenthashmap y lo inserta al threadlocal
	 */
	public static void init() {

		final var context = new ConcurrentHashMap<CatalogMetric, MetricList>();
		localCache.set(context);

	}

	/**
	 * limpia el threadlocal para que quede disponible para un proximo request
	 */
	public static void destroy() {
		localCache.remove();
	}


	/**
	 * Retorna el contexto. Si no esta inicializado, se crea
	 *
	 * @return Mapa donde la key es {@link CatalogMetric} y value {@link MetricList}
	 */
	public static Map<CatalogMetric, MetricList> getContext() {
		return localCache.get();
	}

	/**
	 * Agrega una metrica al contexto
	 * @param metric
	 */
	public static void addMetric(final Metric metric) {
		getContext().putIfAbsent(metric.getCatalogMetric(), metric.createMetricList());

		getContext().get(metric.getCatalogMetric()).add(metric);
	}


	public static MetricList getMetric(CatalogMetric catalogMetric) {
		return getContext().get(catalogMetric);
	}

}
