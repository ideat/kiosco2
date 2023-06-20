package org.vaadin.example.backend.service.loan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.backend.entity.loan.ChargesDeferred;
import org.vaadin.example.backend.repository.loan.ChargesDeferredMapper;

import java.util.List;
import java.util.Optional;

@Service
public class ChargesDeferredService {

    @Autowired
    private ChargesDeferredMapper chargesDeferredMapper;

    public List<ChargesDeferred> chargesDeferredList(Integer loanNumber){
        return chargesDeferredMapper.findByLoanNumber(loanNumber);
    }

    public Optional<ChargesDeferred> findInterestByLoanNumber(Integer loanNumber){
        return chargesDeferredMapper.findInterestByLoanNumber(loanNumber);
    }
}
