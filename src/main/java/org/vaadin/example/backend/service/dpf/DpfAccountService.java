package org.vaadin.example.backend.service.dpf;

import org.vaadin.example.backend.entity.dpf.DpfAccounts;
import org.vaadin.example.backend.repository.dpf.DpfAccountsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DpfAccountService {

    @Autowired
    DpfAccountsMapper dpfaccountsMapper;

    public List<DpfAccounts> getDpfsByClient(Integer codeClient){
        return dpfaccountsMapper.getDpfsByClient(codeClient);
    }

}
