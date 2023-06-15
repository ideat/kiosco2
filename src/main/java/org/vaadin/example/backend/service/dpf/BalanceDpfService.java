package org.vaadin.example.backend.service.dpf;

import org.vaadin.example.backend.entity.dpf.DetailTransactionDpf;
import org.vaadin.example.backend.entity.dpf.HeaderBalanceDpf;
import org.vaadin.example.backend.entity.dpf.dto.BalanceDpfDto;
import org.vaadin.example.backend.entity.dpf.dto.DetailTransactionDpfDto;
import org.vaadin.example.backend.repository.dpf.DetailTransactionDpfMapper;
import org.vaadin.example.backend.repository.dpf.HeaderBalanceDpfMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BalanceDpfService {

    @Autowired
    HeaderBalanceDpfMapper headerBalanceDpfMapper;

    @Autowired
    DetailTransactionDpfMapper detailTransactionDpfMapper;

    public BalanceDpfDto getBalanceDpf(String account){

        HeaderBalanceDpf headerBalanceDpf = headerBalanceDpfMapper.findByAccount(account);
        List<DetailTransactionDpf> detailTransactionDpfList = detailTransactionDpfMapper.findByAccount(account);

        List<DetailTransactionDpfDto> detailTransactionDpfDtoList = new ArrayList<>();

        for(DetailTransactionDpf d: detailTransactionDpfList){
            DetailTransactionDpfDto dto = new DetailTransactionDpfDto();

            dto.setTransactionDate(d.getPftdtftra());
            dto.setTypeTransaction(d.getPfcondesc());
            dto.setDescription(d.getPftdtdesc());
            dto.setAmount(d.getPftdtimpp()*-1);

            detailTransactionDpfDtoList.add(dto);
        }

        BalanceDpfDto balanceDpfDto = new BalanceDpfDto();
        balanceDpfDto.setNumberDpf(headerBalanceDpf.getPfmdpndep());
        balanceDpfDto.setExpiredDate(headerBalanceDpf.getPfmdpfvto());
        balanceDpfDto.setTerm(headerBalanceDpf.getPfmdpplzo().toString().concat(" Dias"));
        balanceDpfDto.setRegisterDate(headerBalanceDpf.getPfmdpfreg());
        balanceDpfDto.setCodeClient(headerBalanceDpf.getGbagecage());
        balanceDpfDto.setCurrency(headerBalanceDpf.getPfmdpcmon().equals(1)?"Bs.":"$us.");
        balanceDpfDto.setCurrencyName (headerBalanceDpf.getPfmdpcmon().equals(1)?"BOLIVIANOS.":"DOLARES AMERICANOS");
        balanceDpfDto.setFullName(headerBalanceDpf.getGbagenomb());
        balanceDpfDto.setAmount(headerBalanceDpf.getPfmdpcapi());
        balanceDpfDto.setProduct(headerBalanceDpf.getPftdpdesc());
        balanceDpfDto.setBeforeNumberDpf(headerBalanceDpf.getPfmdpcorr());
        balanceDpfDto.setOriginalRegisterDate(headerBalanceDpf.getPfmdpfror());
        balanceDpfDto.setDetailTransaction(detailTransactionDpfDtoList);
        balanceDpfDto.setNumberRenovations(headerBalanceDpf.getPfmdpcren());
        balanceDpfDto.setProcessDate(headerBalanceDpf.getPfmdpfpro());
        balanceDpfDto.setPaymentPeriod(headerBalanceDpf.getPfmdpppgi().toString().concat(" Dias"));

        return balanceDpfDto;

    }
}
