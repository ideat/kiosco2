package org.vaadin.example.backend.rest;

import org.vaadin.example.backend.entity.sec.StageHistoryCreditRequestDto;
import org.vaadin.example.backend.entity.sec.SummaryCreditRequestStage;
import org.vaadin.example.backend.entity.sec.creditRequest.CreditRequest;
import org.vaadin.example.backend.entity.sec.creditRequest.PaymentPlan;
import org.vaadin.example.backend.entity.sec.kiosco.ProductKiosco;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SecService {
    @Value("${url_sec}")
    private String url;

    RestTemplate restTemplate = new RestTemplate();

    public List<SummaryCreditRequestStage> getSummaryByIdCard(String idCard){
        final String uri = url +"/getSummaryByIdCard/"+idCard.trim();

        HttpEntity<SummaryCreditRequestStage[]> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<SummaryCreditRequestStage[]> response = restTemplate.exchange(uri, HttpMethod.GET,entity,SummaryCreditRequestStage[].class);

        return Arrays.asList(response.getBody());
    }

    public List<StageHistoryCreditRequestDto> getDetailByNumberRequest(Integer numberRequest){
        final String uri = url +"/getDetailByNumberRequest";

        HttpHeaders headers = new HttpHeaders();
        headers.set("number-request", numberRequest.toString());

        HttpEntity<StageHistoryCreditRequestDto[]> entity = new HttpEntity<>(headers);
        ResponseEntity<StageHistoryCreditRequestDto[]> response = restTemplate
                .exchange(uri, HttpMethod.GET,entity,StageHistoryCreditRequestDto[].class);
        return Arrays.asList(response.getBody());
    }

    public List<PaymentPlan> getPaymentPlanSimulation(CreditRequest creditRequest){
        final String uri = url +"/getDetailByNumberRequest";
        List<PaymentPlan> paymentPlanList = new ArrayList<>();



        return paymentPlanList;
    }

    public List<ProductKiosco> getProductKiosco(){
        final String uri = url +"/product/getAll";
        HttpEntity<ProductKiosco[]> entity = new HttpEntity<>(new HttpHeaders());

        ResponseEntity<ProductKiosco[]> response = restTemplate.exchange(uri,HttpMethod.GET,entity,ProductKiosco[].class);
        return Arrays.asList(response.getBody());

    }

    public byte[] getPaymentPlanSimulationReport(String paymentPlanSimulation){
        final String uri = url + "/paymentplansimulation/";

        HttpHeaders headers = new HttpHeaders();
        headers.set("paymentplansimulation",paymentPlanSimulation);

        HttpEntity<byte[]> entity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(uri,HttpMethod.GET,entity,byte[].class);

        return response.getBody();
    }
}
