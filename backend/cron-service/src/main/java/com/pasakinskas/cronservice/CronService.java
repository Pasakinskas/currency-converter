package com.pasakinskas.cronservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.WebServiceException;

@Component
public class CronService {

    @Value("${service.api-url}")
    private String API_URL;

    @Scheduled(fixedDelayString = "${service.repeat-interval}")
    public void hitApi() {
        ResponseEntity<String> response = new RestTemplate()
                .getForEntity(API_URL + "/update-rates", String.class);

        if (response.getStatusCode().isError()) {
            throw new WebServiceException("Error contacting currency converter server");
        }
    }

    @Scheduled(fixedDelayString = "${service.once-a-month-miliseconds}")
    public void loadHistoricRates() {
        ResponseEntity<String> response = new RestTemplate()
                .getForEntity(API_URL + "/historic?start=2020-04-01&end=2020-06-01", String.class);

        if (response.getStatusCode().isError()) {
            throw new WebServiceException("Error contacting currency converter server");
        }
    }
}
