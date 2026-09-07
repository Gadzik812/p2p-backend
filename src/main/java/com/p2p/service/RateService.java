package com.p2p.service;

import com.p2p.integration.RapiraClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class RateService {

    private final RapiraClient rapiraClient;
    private BigDecimal marketRate = new BigDecimal("88.00");
    private LocalDateTime lastUpdated = LocalDateTime.now();

    private final BigDecimal SPREAD_BUY = new BigDecimal("2.50");
    private final BigDecimal SPREAD_SELL = new BigDecimal("1.70");

    public RateService(RapiraClient rapiraClient) {
        this.rapiraClient = rapiraClient;
        updateMarketRate();
    }

    @Scheduled(fixedDelay = 3600000)
    public void updateMarketRate() {
        try {
            BigDecimal newRate = rapiraClient.getMarketRate();
            if (newRate.compareTo(BigDecimal.ZERO) > 0) {
                this.marketRate = newRate;
                this.lastUpdated = LocalDateTime.now();
            }
        } catch (Exception e) {
            // Оставляем старый курс
        }
    }

    public BigDecimal getBuyRate() {
        return marketRate.subtract(SPREAD_BUY).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSellRate() {
        return marketRate.add(SPREAD_SELL).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getMarketRate() {
        return marketRate.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalSpread() {
        return getSellRate().subtract(getBuyRate()).setScale(2, RoundingMode.HALF_UP);
    }

    public Map<String, Object> getRates() {
        Map<String, Object> rates = new HashMap<>();
        rates.put("marketRate", getMarketRate());
        rates.put("buyRate", getBuyRate());
        rates.put("sellRate", getSellRate());
        rates.put("totalSpread", getTotalSpread());
        rates.put("spreadBuy", SPREAD_BUY);
        rates.put("spreadSell", SPREAD_SELL);
        rates.put("lastUpdated", lastUpdated);
        rates.put("source", "Rapira API");
        return rates;
    }
}