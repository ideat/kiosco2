package org.vaadin.example.backend.repository.loan;

import org.vaadin.example.backend.entity.loan.DeferredPaymentPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DeferredPaymentPlanMapper {

    @Select(" select a.prdipfreg,a.prdipfpag,a.prdipcarg,round(a.prdipcuot,2) " +
            " prdipcuot,a.prdipmpag   " +
            " from prdip a   " +
            " where a.prdipnpre= #{loanNumber}   " +
            " order by 1,2")
    List<DeferredPaymentPlan> findByLoanNumber(@Param("loanNumber") Integer loanNumber);
}
