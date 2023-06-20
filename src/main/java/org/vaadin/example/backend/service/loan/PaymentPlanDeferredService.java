package org.vaadin.example.backend.service.loan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.backend.entity.loan.*;
import org.vaadin.example.backend.entity.loan.dto.*;
import org.vaadin.example.backend.repository.loan.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PaymentPlanDeferredService {

    @Autowired
    private HeaderPaymentPlanMapper headerPaymentPlanMapper;

    @Autowired
    private ChargesDeferredMapper chargesDeferredMapper;

    @Autowired
    private PaymentPlanMapper paymentPlanMapper;

    @Autowired
    private LoanRateMapper loanRateMapper;

    @Autowired
    private DetailChargesDeferredMapper detailChargesDeferredMapper;



    public PaymentPlanDeferredDto getPaymentPlanDeferret(Integer loanNumber, Date cutDate){

        HeaderPaymentPlan headerPaymentPlan = headerPaymentPlanMapper.findByNumberLoan(loanNumber);
        List<PaymentPlan> paymentPlanList = paymentPlanMapper.findByNumberLoanAndDatePreviuos(loanNumber, cutDate);
        List<LoanRate> loanRateList = loanRateMapper.findByLoanNumber(loanNumber);
        Optional<PaymentPlan> paymentPlanDate = paymentPlanMapper.findByNumberLoanAndDate(loanNumber,cutDate);
        List<ChargesDeferred> chargesDeferredList = chargesDeferredMapper.findByLoanNumber(loanNumber);


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
            DetailPaymentPlanDto detailPaymentPlanDto = new DetailPaymentPlanDto();

            detailPaymentPlanDto.setPaymentDate(p.getPrppgfech());
            detailPaymentPlanDto.setInstallmentNumber(p.getPrppgnpag());
            detailPaymentPlanDto.setPrincipal(p.getPrppgcapi());
            detailPaymentPlanDto.setInterest(p.getPrppginte());
            detailPaymentPlanDto.setFee(p.getPrppgcapi() + p.getPrppginte());
            Double prppgcarg = p.getPrppgcarg() !=null ?p.getPrppgcarg():0.0;
            Double prppggral = p.getPrppggral()!=null? p.getPrppggral():0.0;
            Double prppgsegu = p.getPrppgsegu()!=null?p.getPrppgsegu():0.0;
            Double prppgotro = p.getPrppgotro()!=null?p.getPrppgotro():0.0;


            detailPaymentPlanDto.setCharge(prppgcarg + prppggral + prppgsegu + prppgotro);
            detailPaymentPlanDto.setTotal(p.getPrppgtota());
            balance = balance - p.getPrppgcapi();
            detailPaymentPlanDto.setBalance(balance);

            detailPaymentPlanDtoList.add(detailPaymentPlanDto);

        }



        //Get deferred fields header
//        Optional<ChargesDeferred> chargesDeferred = chargesDeferredList.stream()
//                .filter(c -> c.getPrdifcarg().intValue()>=430 && c.getPrdifcarg().intValue()<=452 )
//                .findFirst();
        Optional<ChargesDeferred> chargesDeferred = chargesDeferredMapper.findInterestByLoanNumber(loanNumber);

        List<DetailChargesDiferredPaymentPlanDto> detailChargesDiferredPaymentPlanDtos = new ArrayList<>();

        HeaderChargeDeferred headerChargeDeferred = new HeaderChargeDeferred();
        List<HeaderChargeDeferred> headerChargeDeferredList = new ArrayList<>();

        if(chargesDeferred.isPresent()) {
            headerChargeDeferred.setBalanceInterest(chargesDeferred.get().getPrdifmori());
            headerChargeDeferred.setInterestDeferred(0.0);
            headerChargeDeferred.setTotalInterest(chargesDeferred.get().getPrdifmori());
            headerChargeDeferred.setNumberFeesInterest(chargesDeferred.get().getPrdifccap());
            headerChargeDeferred.setAmountFeeInterest(chargesDeferred.get().getPrdifcuot());
            headerChargeDeferred.setNumberCurrentFee(paymentPlanDate.isPresent()?paymentPlanDate.get().getPrppgnpag():0);


//            List<ChargesDeferred> chargesDeferred1 = chargesDeferredList.stream()
//                    .filter(c -> (c.getPrdifcarg().intValue() >= 66 && c.getPrdifcarg().intValue() <= 77)
//                            || (c.getPrdifcarg().intValue() >= 510 && c.getPrdifcarg().intValue() <= 553))
//                    .collect(Collectors.toList());
            List<ChargesDeferred> chargesDeferred1 = chargesDeferredMapper.findApportionmentChargeByLoanNumber(loanNumber);
            if (chargesDeferred1.size() > 0) {

                Double balanceDeferred = chargesDeferred1.stream()
                        .mapToDouble(ChargesDeferred::getPrdifmori).sum();

                Double totalDeferred = balanceDeferred;


                headerChargeDeferred.setBalanceDeferred(balanceDeferred);
                headerChargeDeferred.setTotalChargeToDeferred(totalDeferred);
                headerChargeDeferred.setNumberFeesCharges(chargesDeferred1.get(0).getPrdifccap());
                headerChargeDeferred.setChargeToDeferred(0.0);
                headerChargeDeferred.setNumberFeesCharges(chargesDeferred1.get(0).getPrdifccap());
                Double amountFee = chargesDeferred1.stream()
                                .mapToDouble(ChargesDeferred::getPrdifcuot).sum();
                headerChargeDeferred.setAmountFeeCharges(amountFee);

            }

            headerChargeDeferred.setEndPeriodGrace(chargesDeferred.get().getPrdiffreg());
            headerChargeDeferredList.add(headerChargeDeferred);

            List<PaymentPlan> paymentPlanSubsequent = paymentPlanMapper.findByNumberLoanAndDateSubsequent(loanNumber, cutDate);
            List<DetailChargesDeferred> detailChargesDeferredList = detailChargesDeferredMapper.findByLoanNumberAndCode(loanNumber,
                    chargesDeferred.get().getPrdifcarg());

            Double feeChargeDeferred = chargesDeferred1.stream()
                    .mapToDouble(ChargesDeferred::getPrdifcuot).sum();

            Double totalChargeDeferred = chargesDeferred1.stream()
                    .mapToDouble(ChargesDeferred::getPrdifmori).sum();

            Integer numberChargeDeferred = chargesDeferred1.get(0).getPrdifccap();

            Double lastFee= totalChargeDeferred - (feeChargeDeferred*(numberChargeDeferred-1));


            for (PaymentPlan paymentPlan : paymentPlanSubsequent) {
                DetailChargesDiferredPaymentPlanDto detailChargesDiferredPaymentPlanDto = new DetailChargesDiferredPaymentPlanDto();
                Optional<DetailChargesDeferred> detailChargesDeferredOptional =
                        detailChargesDeferredList.stream()
                                .filter(d -> d.getPrdipfpag().equals(paymentPlan.getPrppgfech()))
                                .findFirst();

                detailChargesDiferredPaymentPlanDto.setSequence(paymentPlan.getPrppgnpag());
                detailChargesDiferredPaymentPlanDto.setDeferredDate(paymentPlan.getPrppgfech());
                detailChargesDiferredPaymentPlanDto.setExpireDate(null);
                detailChargesDiferredPaymentPlanDto.setPrincipal(paymentPlan.getPrppgcapi());
                detailChargesDiferredPaymentPlanDto.setInterest(paymentPlan.getPrppginte());

                Double interesDeferred = detailChargesDeferredOptional.isPresent() ? detailChargesDeferredOptional.get().getPrdipcuot() : 0.0;

                detailChargesDiferredPaymentPlanDto.setInterestDeferred(interesDeferred);

                double fee = paymentPlan.getPrppgcapi() + paymentPlan.getPrppginte() + interesDeferred;
                detailChargesDiferredPaymentPlanDto.setFee(fee);
                detailChargesDiferredPaymentPlanDto.setCharges(paymentPlan.getPrppgsegu());

                if(detailChargesDeferredOptional.isPresent()){
                    numberChargeDeferred--;
                    detailChargesDiferredPaymentPlanDto.setChargesDeferred(numberChargeDeferred>0?feeChargeDeferred:
                            numberChargeDeferred==0?lastFee:0.0);
                }else{
                    detailChargesDiferredPaymentPlanDto.setChargesDeferred(0.0);
                }


                detailChargesDiferredPaymentPlanDto.setChargesDeferred(numberChargeDeferred>0? feeChargeDeferred:0.0);

                detailChargesDiferredPaymentPlanDto.setTotal(fee + paymentPlan.getPrppgsegu() + feeChargeDeferred);
                balance = balance - paymentPlan.getPrppgcapi();
                detailChargesDiferredPaymentPlanDto.setBalance(balance);

                detailChargesDiferredPaymentPlanDtos.add(detailChargesDiferredPaymentPlanDto);
            }
        }
        //Principal Deferred
        chargesDeferred = Optional.empty();
        chargesDeferred = chargesDeferredList.stream()
                .filter(c -> c.getPrdifcarg().intValue()==424
                        || c.getPrdifcarg().intValue()==425
                        || c.getPrdifcarg().intValue()==420
                        || c.getPrdifcarg().intValue()==421
                        || (c.getPrdifcarg().intValue()>=460 && c.getPrdifcarg().intValue()<=466))
                .findFirst();
        List<DetailChargesDeferred> principalDeferredList = new ArrayList<>();
        Double principalPending = 0.0;
        if(!chargesDeferred.isEmpty()) {
            principalDeferredList = detailChargesDeferredMapper.findByLoanNumberAndCode(loanNumber,
                    chargesDeferred.get().getPrdifcarg());
            principalPending = chargesDeferredList.stream()
                    .filter(c -> c.getPrdifcarg().intValue() == 424
                            || c.getPrdifcarg().intValue() == 425
                            || c.getPrdifcarg().intValue() == 420
                            || c.getPrdifcarg().intValue() == 421
                            || (c.getPrdifcarg().intValue() >= 460 && c.getPrdifcarg().intValue() <= 466))
                    .mapToDouble(ChargesDeferred::getPrdifsald).sum();

        }

        //Interest Deferred
        chargesDeferred = Optional.empty();
        chargesDeferred = chargesDeferredList.stream()
                .filter(c -> c.getPrdifcarg().intValue()==422
                        || c.getPrdifcarg().intValue()==423
                        || c.getPrdifcarg().intValue()==426
                        || c.getPrdifcarg().intValue()==427)
                .findFirst();
        Double interestPending = chargesDeferredList.stream()
                .filter(c -> c.getPrdifcarg().intValue()==422
                        || c.getPrdifcarg().intValue()==423
                        || c.getPrdifcarg().intValue()==426
                        || c.getPrdifcarg().intValue()==427)
                .mapToDouble(ChargesDeferred::getPrdifsald).sum();

        List<DetailChargesDeferred> interestDeferredList = new ArrayList<>();
        if(!chargesDeferred.isEmpty()){
            interestDeferredList = detailChargesDeferredMapper.findByLoanNumberAndCode(loanNumber,
                    chargesDeferred.get().getPrdifcarg());

        }

        List<ChargesDeferred> chargesDeferredList1 = chargesDeferredMapper.findSureChargeByLoanNumber(loanNumber);
        Double charges = 0.0;
        Integer ccap = 0;
        Double feeCharges = 0.0;
        Double lastFee = 0.0;
        if(chargesDeferredList1.size()>0){
            charges = chargesDeferredList1.stream()
                    .mapToDouble(ChargesDeferred::getPrdifmori).sum();
            ccap = chargesDeferredList1.get(0).getPrdifccap();
            feeCharges =  Math.round((charges / ccap)*100.0)/100.0;
            lastFee = charges - (feeCharges*(ccap-1));
        }

        List<DetailFeeChargesDeferredDto> detailFeeChargesDeferredDtoList = new ArrayList<>();

        int sec=0;

        for (DetailChargesDeferred detailChargesDeferred : principalDeferredList) {
            sec++;
            DetailFeeChargesDeferredDto detailFeeChargesDeferredDto = new DetailFeeChargesDeferredDto();
            Optional<DetailFeeChargesDeferredDto> existPayPrincipal = detailFeeChargesDeferredDtoList.stream()
                            .filter(d -> d.getDeferredDate().equals(detailChargesDeferred.getPrdipfreg()))
                                    .findFirst();
            if(existPayPrincipal.isEmpty()) {
                detailFeeChargesDeferredDto.setSequence(sec);
                detailFeeChargesDeferredDto.setDeferredDate(detailChargesDeferred.getPrdipfreg());
                detailFeeChargesDeferredDto.setDueDate(detailChargesDeferred.getPrdipfpag());
                detailFeeChargesDeferredDto.setPrincipal(detailChargesDeferred.getPrdipcuot());
                detailFeeChargesDeferredDto.setProcessDate(detailChargesDeferred.getPrdipfpro());

                DetailChargesDeferred interestDeferred = interestDeferredList.stream()
                        .filter(interest -> interest.getPrdipfreg().equals(detailChargesDeferred.getPrdipfreg()))
                        .findFirst().get();
                detailFeeChargesDeferredDto.setInterest(interestDeferred.getPrdipcuot());
                detailFeeChargesDeferredDto.setFee(detailChargesDeferred.getPrdipcuot() + interestDeferred.getPrdipcuot());
                ccap--;
                detailFeeChargesDeferredDto.setCharges(ccap > 0 ? feeCharges :
                        ccap == 0 ? lastFee : 0.0);

                detailFeeChargesDeferredDto.setTotal(detailChargesDeferred.getPrdipcuot() +
                        interestDeferred.getPrdipcuot() + detailFeeChargesDeferredDto.getCharges());

                detailFeeChargesDeferredDto.setPrincipalPending(principalPending);
                detailFeeChargesDeferredDto.setInterestPending(interestPending);
                detailFeeChargesDeferredDto.setFeePending(principalPending + interestPending);
                detailFeeChargesDeferredDto.setChargesPending(charges);
                detailFeeChargesDeferredDto.setTotalPending(principalPending + interestPending + charges);

                detailFeeChargesDeferredDtoList.add(detailFeeChargesDeferredDto);
            }else{
                detailFeeChargesDeferredDtoList.stream()
                        .filter(d -> d.getDeferredDate().equals(detailChargesDeferred.getPrdipfreg()))
                        .forEach(d -> d.setPrincipal(d.getPrincipal()+detailChargesDeferred.getPrdipcuot()));
                detailFeeChargesDeferredDtoList.stream()
                        .filter(d -> d.getDeferredDate().equals(detailChargesDeferred.getPrdipfreg()))
                        .forEach(d -> d.setFee(d.getFee()+detailChargesDeferred.getPrdipcuot()));
                detailFeeChargesDeferredDtoList.stream()
                        .filter(d -> d.getDeferredDate().equals(detailChargesDeferred.getPrdipfreg()))
                        .forEach(d -> d.setTotal(d.getTotal()+detailChargesDeferred.getPrdipcuot()));

            }

        }


        for (DetailChargesDeferred detailChargesDeferred : interestDeferredList) {

            Optional<DetailFeeChargesDeferredDto> optionalDetailFeeChargesDeferredDto = detailFeeChargesDeferredDtoList.stream()
                    .filter(d -> d.getDeferredDate().equals(detailChargesDeferred.getPrdipfreg()))
                    .findFirst();
            if(optionalDetailFeeChargesDeferredDto.isEmpty()) {
                sec++;
                DetailFeeChargesDeferredDto detailFeeChargesDeferredDto = new DetailFeeChargesDeferredDto();
                detailFeeChargesDeferredDto.setSequence(sec);
                detailFeeChargesDeferredDto.setDeferredDate(detailChargesDeferred.getPrdipfreg());
                detailFeeChargesDeferredDto.setDueDate(detailChargesDeferred.getPrdipfpag());
                detailFeeChargesDeferredDto.setProcessDate(detailChargesDeferred.getPrdipfpro());

                detailFeeChargesDeferredDto.setPrincipal(0.0);

                detailFeeChargesDeferredDto.setInterest(detailChargesDeferred.getPrdipcuot());
                detailFeeChargesDeferredDto.setFee(detailChargesDeferred.getPrdipcuot());
                ccap--;
                detailFeeChargesDeferredDto.setCharges(ccap > 0 ? feeCharges :
                        ccap == 0 ? lastFee : 0.0);

                detailFeeChargesDeferredDto.setTotal(detailChargesDeferred.getPrdipcuot() +
                        detailFeeChargesDeferredDto.getCharges());

                detailFeeChargesDeferredDto.setPrincipalPending(principalPending);
                detailFeeChargesDeferredDto.setInterestPending(interestPending);
                detailFeeChargesDeferredDto.setFeePending(principalPending + interestPending);
                detailFeeChargesDeferredDto.setChargesPending(charges);
                detailFeeChargesDeferredDto.setTotalPending(principalPending + interestPending + charges);

                detailFeeChargesDeferredDtoList.add(detailFeeChargesDeferredDto);
            }
        }

        detailFeeChargesDeferredDtoList = detailFeeChargesDeferredDtoList.stream()
                .sorted(Comparator.comparing(DetailFeeChargesDeferredDto::getProcessDate))
                .collect(Collectors.toList());
        List<DetailFeeChargesDeferredDto> finalDetailFeeChargesDeferredDtoList = detailFeeChargesDeferredDtoList;
        IntStream.range(0,detailFeeChargesDeferredDtoList.size())
                .forEach(i -> finalDetailFeeChargesDeferredDtoList.get(i).setSequence(i+1));



        PaymentPlanDeferredDto paymentPlanDeferredDto = new PaymentPlanDeferredDto();
        paymentPlanDeferredDto.setLoanNumber(headerPaymentPlan.getPrmprnpre());
        paymentPlanDeferredDto.setCodeClient(headerPaymentPlan.getPrmprcage());
        paymentPlanDeferredDto.setFullName(headerPaymentPlan.getGbagenomb());
        paymentPlanDeferredDto.setAmount(headerPaymentPlan.getPrmprmapt());
        paymentPlanDeferredDto.setCurrency(headerPaymentPlan.getPrmprcmon().equals(1)?"Bs.":"$us.");
        paymentPlanDeferredDto.setCurrencyName(headerPaymentPlan.getPrmprcmon().equals(1)?"BOLIVIANOS.":"DOLARES AMERICANOS");
        paymentPlanDeferredDto.setTypeLoan(headerPaymentPlan.getPrtcrdesc());
        paymentPlanDeferredDto.setState(headerPaymentPlan.getPrmprstat());
        paymentPlanDeferredDto.setRate(loanRateList.get(0).getPrtsatbas());
        Integer term = 0;
        if(headerPaymentPlan.getPrmpruplz().equals(1)){
            term = (headerPaymentPlan.getPrmprplzo()/360)*12;
        }else if(headerPaymentPlan.getPrmpruplz().equals(2)){
            term = headerPaymentPlan.getPrmprplzo()/30;
        }else {
            term = headerPaymentPlan.getPrmprplzo();
        }

        paymentPlanDeferredDto.setTerm(term);
        paymentPlanDeferredDto.setDisbursementDate(headerPaymentPlan.getPrmprfdes());
        paymentPlanDeferredDto.setPaymentPeriodPrincipal(headerPaymentPlan.getPrmprppgk());
        paymentPlanDeferredDto.setPaymentPeriodInterest(headerPaymentPlan.getPrmprppgi());
        paymentPlanDeferredDto.setFeeType(headerPaymentPlan.getPrcondesc());
        paymentPlanDeferredDto.setDetailPaymentPlanDtoList(detailPaymentPlanDtoList);
        paymentPlanDeferredDto.setHeaderChargeDeferredList(headerChargeDeferredList);
        paymentPlanDeferredDto.setDetailChargesDiferredPaymentPlanDtoList(detailChargesDiferredPaymentPlanDtos);
        paymentPlanDeferredDto.setDetailFeeChargesDeferredDtoList(finalDetailFeeChargesDeferredDtoList);

        List<PaymentPlan> paymentPlans = paymentPlanMapper.findByNumberLoan(loanNumber);

        Double accumulateCapital = paymentPlans.stream()
                .mapToDouble(PaymentPlan::getPrppgcapi).sum();
        Double accumulateInterest = paymentPlans.stream()
                .mapToDouble(PaymentPlan::getPrppginte).sum();

        Double accumulateFee = accumulateCapital + accumulateInterest;

        Double accumulateCharges = paymentPlans.stream()
                .mapToDouble(PaymentPlan::getPrppgsegu).sum();
        Double accumulateOthers = paymentPlans.stream()
                .mapToDouble(PaymentPlan::getPrppgotro).sum();
        accumulateCharges = accumulateOthers + accumulateCharges;
        Double accumulateTotal = accumulateFee + accumulateCharges;

        paymentPlanDeferredDto.setAccumulateCapital(accumulateCapital);
        paymentPlanDeferredDto.setAccumulateInterest(accumulateInterest);
        paymentPlanDeferredDto.setAccumulateFee(accumulateFee);
        paymentPlanDeferredDto.setAccumulateCharges(accumulateCharges);
        paymentPlanDeferredDto.setAccumulateTotal(accumulateTotal);

        return paymentPlanDeferredDto;
    }

    private List<ChargesDeferred> getChargesDeferredLoan(Integer loanNumber){
        return chargesDeferredMapper.findByLoanNumber(loanNumber);
    }

}
