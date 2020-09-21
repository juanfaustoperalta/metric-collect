package com.metric.collect.metric;

import com.google.gson.JsonParseException;
import com.metric.collect.model.CatalogMetric;
import com.metric.collect.repository.CatalogClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Interface donde se define la estructura y soporte para una metrica
 */
public interface Metric {
	String DASH = "_";

	CatalogMetric getCatalogMetric();

	List<CatalogClient> getCatalogClients();

	/**
	 * Método utilizado para construir las métricas a trackear
	 *
	 * @return un {@link Map} donde la key representa el cliente a donde trackear y el value es un
	 *         {@link Map} donde la key representa un atributo y el value el valor de la métrica
	 */
	Map<CatalogClient, Map<String, Object>> buildImpl(Map<CatalogMetric, MetricList> context) throws JsonParseException;


	/**
	 * Crea un MetricList default que sirve para los casos en donde se toma una sola metrica por
	 * tipo por thread
	 */
	default MetricList createMetricList() {
		return new MetricList<>(){};
	}

	/**
	 * Metodo Exception-Safe. Cuando el método buildImpl falla, se agrega la excepción a un mapa y
	 * el mismo se retorna para trackearlas
	 */
	default Map<CatalogClient, Map<String, Object>> build(Map<CatalogMetric, MetricList> context) {
		try {

			return this.buildImpl(context);

		} catch (Exception ex) {

			Map<CatalogClient, Map<String, Object>> exceptionMap = new HashMap<>();

			Map<String, Object> exception = new HashMap<>();

			exception.put("errorInBuild:" + this.getCatalogMetric(), ex);

			this.getCatalogClients().forEach(catalogClient -> exceptionMap.put(catalogClient, exception));

			return exceptionMap;
		}
	}


	/**
	 * Metodo para crear label de key en el mapa.
	 *
	 * @param label
	 * @return lab
	 */
	default String getLabel(String label){

		return this.getCatalogMetric().getName().concat(DASH).concat(label);
	}

}
