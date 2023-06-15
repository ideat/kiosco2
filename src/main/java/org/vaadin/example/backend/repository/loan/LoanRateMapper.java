package org.vaadin.example.backend.repository.loan;

import org.vaadin.example.backend.entity.loan.LoanRate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LoanRateMapper {

    @Select(" select prtsanpre, prtsafvig, prtsatbas  " +
            "from prtsa  " +
            "where prtsafvig = (select max(p.prtsafvig)  " +
            "          from prtsa p    " +
            "          where prtsanpre = #{loanNumber})  " +
            "and prtsanpre = #{loanNumber}")
    List<LoanRate> findByLoanNumber(@Param("loanNumber") Integer loanNumber);
}
