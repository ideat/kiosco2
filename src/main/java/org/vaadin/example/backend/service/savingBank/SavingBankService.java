package org.vaadin.example.backend.service.savingBank;

import org.vaadin.example.backend.entity.savingBank.BalanceAccount;
import org.vaadin.example.backend.entity.savingBank.SavingBankBalance;
import org.vaadin.example.backend.entity.savingBank.SavingBankClient;
import org.vaadin.example.backend.repository.savingBank.SavingBankClientMapper;
import org.vaadin.example.backend.repository.savingBank.SavingBankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SavingBankService {

    @Autowired
    SavingBankMapper savingBankMapper;

    @Autowired
    SavingBankClientMapper saveBankClientMapper;


    public List<SavingBankBalance> getSavingBankBalanceFull(String account){
        List<SavingBankBalance> savingBankBalanceList = savingBankMapper.getSavingBankBalance(account);
        List<SavingBankBalance> auxList = new ArrayList<>();
        for(SavingBankBalance s: savingBankBalanceList){
            Double balance = s.getAssets() - s.getDebit();
            s.setBalance(balance);
            auxList.add(s);
        }
        return auxList;
    }

    public List<SavingBankBalance> getSavingBankBalanceBetweenDates(String account, Date initDate, Date endDate){
        BalanceAccount balanceAccount = savingBankMapper.getBalanceToInitDate(account,initDate);

        List<SavingBankBalance> auxList = savingBankMapper.getSavingBankBalanceBetweenDates(account, initDate, endDate);
        List<SavingBankBalance> savingBankBalanceList = new ArrayList<>();

        Double balance = balanceAccount.getBalance();
        for(SavingBankBalance s: auxList){
            balance = balance + s.getAssets() - s.getDebit();
            s.setBalance(balance);
            savingBankBalanceList.add(s);
        }
        return savingBankBalanceList;
    }

    public List<SavingBankBalance> getSavingBankBalanceLastNMovement(String account, Integer movement){

        List<SavingBankBalance> totalList = savingBankMapper.getSavingBankBalance(account);
        Integer total = totalList.size();

        BalanceAccount balanceAccount = savingBankMapper.getBalanceNRegisters(account,(total - movement-1));

        List<SavingBankBalance> auxList = savingBankMapper.getSavingBankBalanceLastNMovement(account, movement);
        List<SavingBankBalance> savingBankBalanceList = new ArrayList<>();

        Double balance = balanceAccount.getBalance();
        for(SavingBankBalance s: auxList){
            balance = balance + s.getAssets() - s.getDebit();
            s.setBalance(balance);
            savingBankBalanceList.add(s);
        }
        return savingBankBalanceList;
    }

    public List<SavingBankClient> getSavingBankClient(Integer codeClient){
        List<SavingBankClient> savingBankClientList = saveBankClientMapper.getSavingBanksClient(codeClient);

        for(SavingBankClient s: savingBankClientList){

        }
        return savingBankClientList;
    }
}
