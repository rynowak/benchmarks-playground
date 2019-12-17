package com.rynowak.weatherapp;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class WeatherappApplication {

	@Bean
	public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
		PoolingHttpClientConnectionManager result = new PoolingHttpClientConnectionManager();
		result.setMaxTotal(20);
		return result;
	}

	@Bean
	public RequestConfig requestConfig() {
		return RequestConfig.DEFAULT;
	}

	@Bean
	public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager manager, RequestConfig config) {
		return HttpClientBuilder
			.create()
			.setConnectionManager(manager)
			.setDefaultRequestConfig(config)
			.build();
	}

	@Bean
	public RestTemplate restTemplate(HttpClient httpClient) {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setHttpClient(httpClient);
		return new RestTemplate(factory);
	}

	public static void main(String[] args) {
		SpringApplication.run(WeatherappApplication.class, args);
	}

}
