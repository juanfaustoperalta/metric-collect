package com.metric.collect.service;

import com.metric.collect.metric.MetricList;
import com.metric.collect.model.CatalogMetric;

import java.util.Collection;
import java.util.Map;


/**
 * Interface donde se define la estructura del sender que define a que {@link Client} va cada
 * metrica
 */
public interface MetricSender {

	/**
	 * Buildeo de la metrica (simple o compuesta), merge de las mismas y send a cada repositorio
	 *
	 * @param metrics una {@link Map} como key {@link CatalogMetric} y de value {@link MetricList}
	 */
	void sendMetric(Map<CatalogMetric, MetricList> metrics);

}
