package com.metric.collect.client;


import java.util.Map;


/**
 * Interface donde se define la estructura y soporte para una cliente
 */
public interface Client {


	/**
	 * Envio de la metrica al {@link Client} correspondiente
	 *
	 * @param metric
	 */
	void sendMetric(Map<String, Object> metric);

}
