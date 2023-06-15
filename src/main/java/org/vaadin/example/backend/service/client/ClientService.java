package org.vaadin.example.backend.service.client;

import org.vaadin.example.backend.entity.client.Client;
import org.vaadin.example.backend.repository.client.ClientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    ClientMapper mapper;

    public Client getClientByCode(Integer codeClient){
        return mapper.getClientByCode(codeClient);
    }

    public Client getClientByIdCard(String idCard){
        return mapper.getClientByIdCard(idCard);
    }
}
