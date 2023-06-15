package org.vaadin.example.backend.service.client;

import org.vaadin.example.backend.repository.client.IdimgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IdimgService {

    @Autowired
    private IdimgMapper mapper;

    public Optional<byte[]> getImageClient(Integer codeClient){
        Optional<byte[]> result = mapper.getImageClient(codeClient);
        if(result.isEmpty()){
            return Optional.empty();
        }
        return result;
    }
}
