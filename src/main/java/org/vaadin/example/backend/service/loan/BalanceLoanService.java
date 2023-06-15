package org.vaadin.example.backend.service.loan;

import org.vaadin.example.backend.entity.loan.DetailTransactionLoan;
import org.vaadin.example.backend.entity.loan.HeaderBalanceLoan;
import org.vaadin.example.backend.entity.loan.MasterTransaction;
import org.vaadin.example.backend.entity.loan.ToDiffer;
import org.vaadin.example.backend.entity.loan.dto.BalanceLoanDto;
import org.vaadin.example.backend.entity.loan.dto.DetailTransactionDto;
import org.vaadin.example.backend.repository.loan.DetailTransactionLoanMapper;
import org.vaadin.example.backend.repository.loan.HeaderBalanceLoanMapper;
import org.vaadin.example.backend.repository.loan.MasterTransactionMapper;
import org.vaadin.example.backend.repository.loan.ToDifferMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BalanceLoanService {

    @Autowired
    HeaderBalanceLoanMapper headerBalanceLoanMapper;

    @Autowired
    MasterTransactionMapper masterTransactionMapper;

    @Autowired
    DetailTransactionLoanMapper detailTransactionLoanMapper;

    @Autowired
    ToDifferMapper toDifferMapper;

    public BalanceLoanDto getBalanceLoan(Integer loanNumber){

        HeaderBalanceLoan headerBalanceLoan = headerBalanceLoanMapper.getHeaderLoan(loanNumber);
        List<MasterTransaction> masterTransactionList = masterTransactionMapper.findMasterTransactionByLoanNumber(loanNumber);
        List<DetailTransactionLoan> detailTransactionLoanList = detailTransactionLoanMapper.findDetailTransactionByLoanNumber(loanNumber);
        List<ToDiffer> toDifferList = toDifferMapper.findByLoanNumber(loanNumber);

        List<DetailTransactionDto> detailTransactionDtoList = new ArrayList<>();
        Date initialDate = headerBalanceLoan.getPrmprfdes();
        Double balance = 0.0;
        for(MasterTransaction m:masterTransactionList){

            List<DetailTransactionLoan> auxDetail = detailTransactionLoanList.stream()
                    .filter(f -> f.getPrtdtntra().equals(m.getPrhtrntra()))
                    .collect(Collectors.toList());
            DetailTransactionDto detailTransactionDto = new DetailTransactionDto();

            Double capital = auxDetail.stream()
                    .filter(f ->  f.getPrtdtpref().equals(20) && f.getPrtdtccon().equals(1) ||
                            (f.getPrtdtpref().equals(21) && (f.getPrtdtccon().equals(420) || f.getPrtdtccon().equals(421) ||
                                    f.getPrtdtccon().equals(424) || f.getPrtdtccon().equals(425))))
                    .mapToDouble(DetailTransactionLoan::getPrtdtimpp).sum();
            Double interest = auxDetail.stream()
                    .filter(f -> f.getPrtdtpref().equals(20) && f.getPrtdtccon().equals(2) ||
                            (f.getPrtdtpref().equals(21) && (f.getPrtdtccon().equals(422) || f.getPrtdtccon().equals(423) ||
                                    f.getPrtdtccon().equals(426) || f.getPrtdtccon().equals(427))))
                    .mapToDouble(DetailTransactionLoan::getPrtdtimpp).sum();
            Double penal = auxDetail.stream()
                    .filter(f -> f.getPrtdtccon().intValue()>=44 && f.getPrtdtccon().intValue()<=55)
                    .mapToDouble(DetailTransactionLoan::getPrtdtimpp).sum();
            Double form1 = auxDetail.stream()
                    .filter(f -> f.getPrtdtttrn().equals(1) && f.getPrtdtccon().intValue()>=56 && f.getPrtdtccon().intValue()<=59)
                    .mapToDouble(DetailTransactionLoan::getPrtdtimpp).sum();
            Double secure =  auxDetail.stream()
                    .filter(f -> f.getPrtdtpref().equals(21) && f.getPrtdtttrn().equals(2) &&
                            (f.getPrtdtccon().intValue()>=26 && f.getPrtdtccon().intValue()<=28) ||
                            (f.getPrtdtccon().intValue()>=31 && f.getPrtdtccon().intValue()<=33))
                    .mapToDouble(DetailTransactionLoan::getPrtdtimpp).sum();
            Double judic = auxDetail.stream()
                    .filter(f -> f.getPrtdtpref().equals(21) && f.getPrtdtttrn().equals(2) && f.getPrtdtccon().intValue()>=21 &&
                            f.getPrtdtccon().intValue()<=25)
                    .mapToDouble(DetailTransactionLoan::getPrtdtimpp).sum();
            Long days = 0l;

            days = Duration.between(initialDate.toInstant(), auxDetail.get(0).getPrtdtftra().toInstant()).toDays();

//            Double auxBalance = auxDetail.stream()
//                    .filter(f -> f.getPrtdtpref().equals(20) && f.getPrtdtccon().equals(1) )
//                    .mapToDouble(DetailTransaction::getPrtdtimpp).sum();

            Double auxOther = auxDetail.stream()
                    .filter(f -> f.getPrtdtpref().equals(21) && (f.getPrtdtccon().equals(428) || f.getPrtdtccon().equals(429)))
                    .mapToDouble(DetailTransactionLoan::getPrtdtimpp).sum();

            balance = capital + balance;

            detailTransactionDto.setPaymentDate(m.getPrhtrftra());
            detailTransactionDto.setTransactionNumber(m.getPrhtrntra());
            detailTransactionDto.setDaysPayment(days.intValue());
            detailTransactionDto.setPaidInCapital(capital);
            detailTransactionDto.setPaidInInterest(interest);
            detailTransactionDto.setPaidInInterestPenal(penal);
            detailTransactionDto.setPaidInForm(form1);
            detailTransactionDto.setPaidInSecure(secure);
            detailTransactionDto.setPaidInJudic(judic);
            detailTransactionDto.setPaidInTotal(m.getPrhtrimpt());
            detailTransactionDto.setPaidInOther(auxOther);
            detailTransactionDto.setBalance(balance);

            detailTransactionDtoList.add(detailTransactionDto);

            initialDate = auxDetail.get(0).getPrtdtftra();
        }

        BalanceLoanDto balanceLoanDto = new BalanceLoanDto();
        balanceLoanDto.setNumberLoan(headerBalanceLoan.getPrmprnpre());
        balanceLoanDto.setFullNameClient(headerBalanceLoan.getGbagenomb());
        Integer term = 0;
        if(headerBalanceLoan.getPrmpruplz().equals(2)){
            term = headerBalanceLoan.getPrmprplzo()/30;
            balanceLoanDto.setTerm(term.toString().concat(" MES(ES)"));
        }else  if(headerBalanceLoan.getPrmpruplz()==3) {
            balanceLoanDto.setTerm(headerBalanceLoan.getPrmprplzo().toString().concat(" DIAS"));
        }
        balanceLoanDto.setCurrency(headerBalanceLoan.getPrmprcmon().equals(1)?"Bs.":"$us.");
        balanceLoanDto.setTypeLoan(headerBalanceLoan.getPrtcrdesc());
        balanceLoanDto.setAmountApproved(headerBalanceLoan.getPrmprmapt());
        balanceLoanDto.setAmountDisbursed(headerBalanceLoan.getPrmprmdes());
        balanceLoanDto.setDisbursementDate(headerBalanceLoan.getPrmprfdes());
        balanceLoanDto.setState(headerBalanceLoan.getPrmprstat());
        balanceLoanDto.setDetailTransactionDtoList(detailTransactionDtoList);
        Double differCapital = 0.0;
        Double differInterest = 0.0;
        if(toDifferList.size()>0) {
          differCapital = toDifferList.stream()
                    .filter(f -> f.getPrdifcarg().equals(420) || f.getPrdifcarg().equals(421) ||
                            f.getPrdifcarg().equals(424) || f.getPrdifcarg().equals(425))
                    .mapToDouble(ToDiffer::getPrdifsald).sum();
          differInterest = toDifferList.stream()
                    .filter(f -> f.getPrdifcarg().equals(422) || f.getPrdifcarg().equals(423) ||
                            f.getPrdifcarg().equals(426) || f.getPrdifcarg().equals(427))
                    .mapToDouble(ToDiffer::getPrdifsald).sum();
        }
        balanceLoanDto.setToDifferCapital(differCapital);
        balanceLoanDto.setToDifferInterest(differInterest);
        balanceLoanDto.setToDifferDate(headerBalanceLoan.getPrmprfvac());
        balanceLoanDto.setToDifferTotal(differCapital + differInterest);
        return balanceLoanDto;
    }
}
