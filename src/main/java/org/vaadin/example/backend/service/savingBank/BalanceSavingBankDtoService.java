package org.vaadin.example.backend.service.savingBank;

import org.vaadin.example.backend.entity.savingBank.DetailTransactionSanvingBank;
import org.vaadin.example.backend.entity.savingBank.HeaderBalanceSavingBank;
import org.vaadin.example.backend.entity.savingBank.dto.BalanceSavingBankDto;
import org.vaadin.example.backend.entity.savingBank.dto.DetailTransactionSavingBankDto;
import org.vaadin.example.backend.repository.savingBank.DetailTransactionSavingBankMapper;
import org.vaadin.example.backend.repository.savingBank.HeaderBalanceSavingBankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BalanceSavingBankDtoService {

    @Autowired
    HeaderBalanceSavingBankMapper headerMapper;

    @Autowired
    DetailTransactionSavingBankMapper detailMapper;

    public BalanceSavingBankDto getBalanceSavingBank(String account,
                                                     Optional<Date> initDate,
                                                     Optional<Date> endDate,
                                                     Optional<Integer> numberRegisters){

        HeaderBalanceSavingBank headerBalanceSavingBank = headerMapper.findByAccount(account);
        List<DetailTransactionSanvingBank> detailTransactionSanvingBankList = new ArrayList<>();
        List<DetailTransactionSanvingBank> detailTransactionSanvingBankList2 = new ArrayList<>();

        List<DetailTransactionSavingBankDto> detailTransactionSavingBankDtoList = new ArrayList<>();


        detailTransactionSanvingBankList = detailMapper.findAllByAccount(account);


        Double initBalance = 0.0;
        Double initDebit = 0.0;
        Double initCredit = 0.0;
        Optional<Date> initDateReport = initDate;
        Optional<Date> endDateReport = endDate;

        if(initDate.isPresent() && endDate.isPresent()){
            detailTransactionSanvingBankList2 = detailMapper.findByAccountAndRangeDates(account, initDate.get(), endDate.get());
            initBalance = detailTransactionSanvingBankList.stream()
                    .filter(f -> f.getCatrnftra().before(initDate.get()))
                    .mapToDouble(DetailTransactionSanvingBank::getCatrnimpo).sum();
            initDebit =  detailTransactionSanvingBankList.stream()
                    .filter(f -> f.getCatrnftra().before(initDate.get()) && f.getCatrnimpo().doubleValue()>0.0)
                    .mapToDouble(DetailTransactionSanvingBank::getCatrnimpo).sum();
            initCredit = detailTransactionSanvingBankList.stream()
                    .filter(f -> f.getCatrnftra().before(initDate.get()) && f.getCatrnimpo().doubleValue()<=0.0)
                    .mapToDouble(DetailTransactionSanvingBank::getCatrnimpo).sum();
            detailTransactionSanvingBankList = detailTransactionSanvingBankList2;
        }
        if(numberRegisters.isPresent()){
            Integer size = detailTransactionSanvingBankList.size()-1;

            Integer firstNroTran = detailTransactionSanvingBankList.get(size-numberRegisters.get()).getCatrnntra();
            initDateReport = Optional.of(detailTransactionSanvingBankList.get(size-numberRegisters.get()).getCatrnftra());
            endDateReport = Optional.of(detailTransactionSanvingBankList.get(size).getCatrnftra());

            initBalance = detailTransactionSanvingBankList.stream()
                    .filter(f -> f.getCatrnntra().intValue()<=firstNroTran)
                    .mapToDouble(DetailTransactionSanvingBank::getCatrnimpo).sum();

            initDebit =  detailTransactionSanvingBankList.stream()
                    .filter(f -> f.getCatrnntra().intValue()<=firstNroTran && f.getCatrnimpo().doubleValue()>0.0)
                    .mapToDouble(DetailTransactionSanvingBank::getCatrnimpo).sum();
            initCredit = detailTransactionSanvingBankList.stream()
                    .filter(f -> f.getCatrnntra().intValue()<=firstNroTran && f.getCatrnimpo().doubleValue()<=0.0)
                    .mapToDouble(DetailTransactionSanvingBank::getCatrnimpo).sum();


            detailTransactionSanvingBankList = detailTransactionSanvingBankList.stream()
                    .filter(f -> f.getCatrnntra() > firstNroTran)
                    .collect(Collectors.toList());


        }

        DetailTransactionSavingBankDto firstRegister = new DetailTransactionSavingBankDto();
        firstRegister.setTransactionDate(null);
        firstRegister.setDescription("Saldo Anterior -------------->");
//        firstRegister.setDebit(initDebit); //initDebit
//        firstRegister.setCredit(initCredit*-1); //initCredit*-1
        firstRegister.setDebit(null); //initDebit
        firstRegister.setCredit(null); //initCredit*-1
        firstRegister.setBalance(initBalance);

        firstRegister.setAmountReserved(headerBalanceSavingBank.getCamcafpig()!=null?headerBalanceSavingBank.getCamcafpig():0.0);

        detailTransactionSavingBankDtoList.add(firstRegister);
        for(DetailTransactionSanvingBank d: detailTransactionSanvingBankList){

            DetailTransactionSavingBankDto detailTransactionSavingBankDto = new DetailTransactionSavingBankDto();
            detailTransactionSavingBankDto.setTransactionDate(d.getCatrnftra());
            detailTransactionSavingBankDto.setDescription(d.getCatmvdesc());
            detailTransactionSavingBankDto.setDebit(d.getCatrnimpo().doubleValue()>0.0?d.getCatrnimpo():0.0);
            detailTransactionSavingBankDto.setCredit(d.getCatrnimpo().doubleValue()<=0.0?d.getCatrnimpo()*-1:0.0);
            detailTransactionSavingBankDto.setAmountReserved(0.0);
            initBalance = initBalance + d.getCatrnimpo();
            detailTransactionSavingBankDto.setBalance(initBalance);

            detailTransactionSavingBankDtoList.add(detailTransactionSavingBankDto);

        }

        BalanceSavingBankDto balanceSavingBankDto = new BalanceSavingBankDto();
        balanceSavingBankDto.setInitDate(initDateReport.get());
        balanceSavingBankDto.setEndDate(endDateReport.get());
        balanceSavingBankDto.setFullName(headerBalanceSavingBank.getGbagenomb());
        balanceSavingBankDto.setCodeClient(headerBalanceSavingBank.getGbagecage());
        String address = headerBalanceSavingBank.getGbagedir1() != null? headerBalanceSavingBank.getGbagedir1().trim() : ""
                .concat(headerBalanceSavingBank.getGbagedir2() != null? headerBalanceSavingBank.getGbagedir2().trim() : "");
        balanceSavingBankDto.setAddressHome(address);
        balanceSavingBankDto.setHomePhone(headerBalanceSavingBank.getGbagetlfd());
        balanceSavingBankDto.setWorkPhone(headerBalanceSavingBank.getGbagetlfo());
        balanceSavingBankDto.setAccount(headerBalanceSavingBank.getCamcancta());
//        balanceSavingBankDto.setInitDate(detailTransactionSavingBankDtoList.get(0).getTransactionDate());
//        balanceSavingBankDto.setEndDate(detailTransactionSavingBankDtoList.get(detailTransactionSavingBankDtoList.size()-1).getTransactionDate());
        balanceSavingBankDto.setCurrency(headerBalanceSavingBank.getCamcacmon().equals(1)?"BOLIVIANOS":"DOLARES");
        balanceSavingBankDto.setDetailTransactionList(detailTransactionSavingBankDtoList);

        return balanceSavingBankDto;
    }


}
