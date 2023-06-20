package org.vaadin.example.backend.service.loan;

import org.vaadin.example.backend.entity.loan.DeferredPaymentPlan;
import org.vaadin.example.backend.entity.loan.HeaderPaymentPlan;
import org.vaadin.example.backend.entity.loan.LoanRate;
import org.vaadin.example.backend.entity.loan.PaymentPlan;
import org.vaadin.example.backend.entity.loan.dto.DeferredPaymentPlanDto;
import org.vaadin.example.backend.entity.loan.dto.DetailPaymentPlanDto;
import org.vaadin.example.backend.entity.loan.dto.PaymentPlanDto;
import org.vaadin.example.backend.repository.loan.DeferredPaymentPlanMapper;
import org.vaadin.example.backend.repository.loan.HeaderPaymentPlanMapper;
import org.vaadin.example.backend.repository.loan.LoanRateMapper;
import org.vaadin.example.backend.repository.loan.PaymentPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PaymentPlanService {

    @Autowired
    private HeaderPaymentPlanMapper headerPaymentPlanMapper;

    @Autowired
    private PaymentPlanMapper paymentPlanMapper;

    @Autowired
    private LoanRateMapper loanRateMapper;

    @Autowired
    private DeferredPaymentPlanMapper deferredPaymentPlanMapper;



    public PaymentPlanDto getPaymentPlan(Integer loanNumber){

        HeaderPaymentPlan headerPaymentPlan = headerPaymentPlanMapper.findByNumberLoan(loanNumber);
        List<PaymentPlan> paymentPlanList = paymentPlanMapper.findByNumberLoan(loanNumber);
        List<LoanRate> loanRateList = loanRateMapper.findByLoanNumber(loanNumber);
        List<DeferredPaymentPlan> deferredPaymentPlanList = deferredPaymentPlanMapper.findByLoanNumber(loanNumber);

        List<DetailPaymentPlanDto> detailPaymentPlanDtoList = new ArrayList<>();
        Double balance = headerPaymentPlan.getPrmprmapt();

        DetailPaymentPlanDto firstDetail = new DetailPaymentPlanDto();
        firstDetail.setPaymentDate(null);
        firstDetail.setInstallmentNumber(0);
        firstDetail.setPrincipal(0.0);
        firstDetail.setInterest(0.0);
        firstDetail.setFee(0.0);
        firstDetail.setCharge(0.0);
        firstDetail.setTotal(0.0);
        firstDetail.setBalance(balance);

        detailPaymentPlanDtoList.add(firstDetail);

        for(PaymentPlan p: paymentPlanList){
            DetailPaymentPlanDto dto = new DetailPaymentPlanDto();

            dto.setPaymentDate(p.getPrppgfech());
            dto.setInstallmentNumber(p.getPrppgnpag());
            dto.setPrincipal(p.getPrppgcapi());
            dto.setInterest(p.getPrppginte());
            dto.setFee(p.getPrppgcapi() + p.getPrppginte());
            Double prppgcarg = p.getPrppgcarg() !=null ?p.getPrppgcarg():0.0;
            Double prppggral = p.getPrppggral()!=null? p.getPrppggral():0.0;
            Double prppgsegu = p.getPrppgsegu()!=null?p.getPrppgsegu():0.0;
            Double prppgotro = p.getPrppgotro()!=null?p.getPrppgotro():0.0;


            dto.setCharge(prppgcarg + prppggral + prppgsegu + prppgotro);
            dto.setTotal(p.getPrppgtota());
            balance = balance - p.getPrppgcapi();
            dto.setBalance(balance);

            detailPaymentPlanDtoList.add(dto);
        }

        List<DeferredPaymentPlanDto> deferredPaymentPlanDtoList = new ArrayList<>();
        List<DeferredPaymentPlan> principalList = deferredPaymentPlanList.stream()
                .filter(f -> f.getPrdipcarg().equals(420) ||
                        f.getPrdipcarg().equals(421) || f.getPrdipcarg().equals(424) || f.getPrdipcarg().equals(425))
                .collect(Collectors.toCollection( () ->
                        new TreeSet<>(Comparator.comparing(DeferredPaymentPlan::getPrdipfreg))))
                .stream().collect(Collectors.toList());

       principalList.sort(Comparator.comparing(DeferredPaymentPlan::getPrdipfreg));


        int i = 0;
        for(DeferredPaymentPlan d: principalList){
            DeferredPaymentPlanDto dto = new DeferredPaymentPlanDto();

            dto.setDeferredDate(d.getPrdipfreg());
            dto.setExpireDate(d.getPrdipfpag());
            Double principal = deferredPaymentPlanList.stream()
                    .filter(f -> f.getPrdipfreg().equals(d.getPrdipfreg()) && (f.getPrdipcarg().equals(420) ||
                            f.getPrdipcarg().equals(421) || f.getPrdipcarg().equals(424) || f.getPrdipcarg().equals(425)))
                    .mapToDouble(DeferredPaymentPlan::getPrdipcuot).sum();
            Double interest = deferredPaymentPlanList.stream()
                    .filter(f -> f.getPrdipfreg().equals(d.getPrdipfreg()) && (f.getPrdipcarg().equals(422) ||
                            f.getPrdipcarg().equals(423) || f.getPrdipcarg().equals(426) || f.getPrdipcarg().equals(427)))
                    .mapToDouble(DeferredPaymentPlan::getPrdipcuot).sum();
            i+=1;
            dto.setSecuence(i);
            dto.setPrincipal(principal);
            dto.setInterest(interest);
            dto.setFee(principal + interest);
            dto.setTotal(principal + interest);
            dto.setCharge(0.0);
            dto.setIsPayment(d.getPrdipmpag());

            deferredPaymentPlanDtoList.add(dto);
        }

        List<DeferredPaymentPlan> interestList = deferredPaymentPlanList.stream()
                .filter(f -> f.getPrdipcarg().equals(422) ||
                        f.getPrdipcarg().equals(423) || f.getPrdipcarg().equals(426) || f.getPrdipcarg().equals(427))
                .collect(Collectors.toCollection( () ->
                        new TreeSet<>(Comparator.comparing(DeferredPaymentPlan::getPrdipfreg))))
                .stream().collect(Collectors.toList());

        for(DeferredPaymentPlan deferredPaymentPlan:interestList){
            Optional<DeferredPaymentPlanDto> optionalDeferredPaymentPlan = deferredPaymentPlanDtoList.stream()
                    .filter(d -> d.getDeferredDate().equals(deferredPaymentPlan.getPrdipfreg()))
                    .findFirst();

            if(optionalDeferredPaymentPlan.isEmpty()){
                DeferredPaymentPlanDto dto = new DeferredPaymentPlanDto();
                dto.setDeferredDate(deferredPaymentPlan.getPrdipfreg());
                dto.setExpireDate(deferredPaymentPlan.getPrdipfpag());
                dto.setPrincipal(0.0);
                Double interest = deferredPaymentPlanList.stream()
                        .filter(f -> f.getPrdipfreg().equals(deferredPaymentPlan.getPrdipfreg()) && (f.getPrdipcarg().equals(422) ||
                                f.getPrdipcarg().equals(423) || f.getPrdipcarg().equals(426) || f.getPrdipcarg().equals(427)))
                        .mapToDouble(DeferredPaymentPlan::getPrdipcuot).sum();
                dto.setInterest(interest);
                dto.setTotal(interest);
                dto.setCharge(0.0);
                dto.setFee(interest);
                dto.setIsPayment(deferredPaymentPlan.getPrdipmpag());
                deferredPaymentPlanDtoList.add(dto);
            }
        }

        deferredPaymentPlanDtoList = deferredPaymentPlanDtoList.stream()
                .sorted(Comparator.comparing(DeferredPaymentPlanDto::getDeferredDate))
                .collect(Collectors.toList());

        List<DeferredPaymentPlanDto> finalDeferredPaymentPlanDtos = deferredPaymentPlanDtoList;
        IntStream.range(0,deferredPaymentPlanDtoList.size())
                .forEach(p -> finalDeferredPaymentPlanDtos.get(p).setSecuence(p+1));

        PaymentPlanDto paymentPlanDto = new PaymentPlanDto();
        paymentPlanDto.setLoanNumber(headerPaymentPlan.getPrmprnpre());
        paymentPlanDto.setCodeClient(headerPaymentPlan.getPrmprcage());
        paymentPlanDto.setFullName(headerPaymentPlan.getGbagenomb());
        paymentPlanDto.setAmount(headerPaymentPlan.getPrmprmapt());
        paymentPlanDto.setCurrency(headerPaymentPlan.getPrmprcmon().equals(1)?"Bs.":"$us.");
        paymentPlanDto.setCurrencyName(headerPaymentPlan.getPrmprcmon().equals(1)?"BOLIVIANOS.":"DOLARES AMERICANOS");
        paymentPlanDto.setTypeLoan(headerPaymentPlan.getPrtcrdesc());
        paymentPlanDto.setState(headerPaymentPlan.getPrmprstat());
        paymentPlanDto.setRate(loanRateList.get(0).getPrtsatbas());
        Integer term = 0;
        if(headerPaymentPlan.getPrmpruplz().equals(1)){
            term = (headerPaymentPlan.getPrmprplzo()/360)*12;
        }else if(headerPaymentPlan.getPrmpruplz().equals(2)){
            term = headerPaymentPlan.getPrmprplzo()/30;
        }else {
            term = headerPaymentPlan.getPrmprplzo();
        }

        paymentPlanDto.setTerm(term);
        paymentPlanDto.setDisbursementDate(headerPaymentPlan.getPrmprfdes());
        paymentPlanDto.setPaymentPeriodPrincipal(headerPaymentPlan.getPrmprppgk());
        paymentPlanDto.setPaymentPeriodInterest(headerPaymentPlan.getPrmprppgi());
        paymentPlanDto.setFeeType(headerPaymentPlan.getPrcondesc());
        paymentPlanDto.setDetailPaymentPlanDtoList(detailPaymentPlanDtoList);
        paymentPlanDto.setDeferredPaymentPlanDtoList(finalDeferredPaymentPlanDtos);

        return paymentPlanDto;
    }
}
