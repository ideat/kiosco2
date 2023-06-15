package org.vaadin.example.backend.rest;

import org.vaadin.example.backend.entity.autoform.Forms;
import org.vaadin.example.backend.entity.autoform.Parameter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class AutoFormService {
    @Value("${url_auto_form}")
    private String url;

    RestTemplate restTemplate = new RestTemplate();

    public Forms create(Forms forms){
        final String uri = url + "/form/create";
        forms.setOriginModule("KIOSCO");
        HttpEntity<Forms> entity = new HttpEntity<>(forms);
        ResponseEntity<Forms> response = restTemplate.postForEntity(uri,entity,Forms.class);
        return response.getBody();
    }

    public Forms findFromKioscoByIdClientAndTypeFormAndCategoryTypeForm(Integer idClient, String nameTypeForm, String categoryTypeForm ){
        final String uri = url + "/form/findFromKioscoByIdClientAndTypeFormAndCategoryTypeForm";

        HttpHeaders headers = new HttpHeaders();
        headers.add("id_client",idClient.toString());
        headers.add("name_type_form",nameTypeForm);
        headers.add("category_type_form",categoryTypeForm);
        HttpEntity<Forms> entity = new HttpEntity<>(headers);

        ResponseEntity<Forms> response = restTemplate.exchange(uri, HttpMethod.GET,entity,Forms.class);

        return response.getBody();

    }

    public List<Parameter> findFromKioscoAllActive() {
        final String uri = url+"/parameter/findFromKioscoAll";
        HttpEntity<Parameter[]> entity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<Parameter[]> response = restTemplate.exchange(uri, HttpMethod.GET,entity,Parameter[].class);

        return Arrays.asList(response.getBody());
    }
}
