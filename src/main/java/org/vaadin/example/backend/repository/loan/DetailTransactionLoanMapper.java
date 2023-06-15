package org.vaadin.example.backend.repository.loan;

import org.vaadin.example.backend.entity.loan.DetailTransactionLoan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DetailTransactionLoanMapper {

    @Select("select prtdtntra, prtdtttrn, prtdtnpre, prtdtftra, prtdtpref, prtdtccon, prtdtimpp, prtdtdesc  " +
            "from prtdt  " +
            "where prtdtnpre = #{loanNumber}  " +
            "and prtdtmrcb = 0  " +
            "order by prtdtftra, prtdtitem")
    List<DetailTransactionLoan> findDetailTransactionByLoanNumber(@Param("loanNumber") Integer loanNumber);
}
