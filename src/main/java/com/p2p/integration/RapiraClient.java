package com.p2p.integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

@Component
public class RapiraClient {

    private final WebClient webClient;

    public RapiraClient() {
        this.webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
    }

    public BigDecimal getMarketRate() {
        try {
            JsonNode response = webClient.get()
                    .uri("https://api.rapira.net/open/market/rates")
                    .header("Accept", "application/json")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();

            JsonNode data = response.get("data");
            if (data != null && data.isArray()) {
                for (JsonNode rate : data) {
                    String symbol = rate.get("symbol").asText();
                    if ("USDT/RUB".equals(symbol)) {
                        BigDecimal price = new BigDecimal(rate.get("askPrice").asText());
                        return price.setScale(2, RoundingMode.HALF_UP);
                    }
                }
            }

            return new BigDecimal("88.00");

        } catch (Exception e) {
            return new BigDecimal("88.00");
        }
    }
}