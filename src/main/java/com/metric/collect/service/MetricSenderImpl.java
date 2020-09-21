package com.metric.collect.service;

import com.metric.collect.client.Client;
import com.metric.collect.metric.MetricList;
import com.metric.collect.model.CatalogMetric;
import com.metric.collect.repository.CatalogClient;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

/**
 * Implementación de {@link MetricSender}
 */
public class MetricSenderImpl
				implements MetricSender {

	private Map<CatalogClient, Client> repositories;

	/**
	 * @param metrics una {@link Collection} de {@link MetricList}
	 * @see MetricSender
	 */
	@Override
	public void sendMetric(Map<CatalogMetric, MetricList> metrics) {

		//Casteo dentro de la lamda por problema al tipar, que no infiere el tipo.
		//Con var funciona pero pierde legibilidad de lo que devuelve la lamda
		final List<Map<CatalogClient, Map<String, Object>>> metricListBuilt = metrics.values().stream()
						.map(metricList -> (Map<CatalogClient, Map<String, Object>>) metricList.build(metrics))
						.collect(Collectors.toList());

		var contextMerge = new HashMap<CatalogClient, HashMap<String, Object>>();

		//merge de las metricas para enviarlas todas en la misma trx (tener cuidado cuando se llegue al limite para NR)
		metricListBuilt.stream().forEach(clientContext -> {
			clientContext.forEach((client, context) -> {
				if (!contextMerge.containsKey(client)) {
					contextMerge.put(client, new HashMap<>());
				}
				contextMerge.get(client).putAll(context);
			});
		});

		contextMerge.forEach((catalogClient, context) -> this.repositories.get(catalogClient).sendMetric(context));

	}


	public Map<CatalogClient, Client> getRepositories() {
		return this.repositories;
	}

	public void setRepositories(Map<CatalogClient, Client> repositories) {
		this.repositories = repositories;
	}
}