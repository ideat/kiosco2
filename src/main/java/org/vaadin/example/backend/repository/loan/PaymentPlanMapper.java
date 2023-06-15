package org.vaadin.example.backend.repository.loan;

import org.vaadin.example.backend.entity.loan.PaymentPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface PaymentPlanMapper {

    @Select(" select * from prppg where prppgnpre = #{numberLoan}")
    List<PaymentPlan> findByNumberLoan(@Param("numberLoan") Integer numberLoan);

    @Select(" select * from prppg " +
            " where prppgnpre = #{numberLoan} " +
            " and prppgfech < #{dayPayment}")
    List<PaymentPlan> findByNumberLoanAndDatePreviuos(@Param("numberLoan") Integer numberLoan,
                                                      @Param("datePayment") Date dayPayment);

    @Select(" select * from prppg " +
            " where prppgnpre = #{numberLoan} " +
            " and prppgfech > #{dayPayment}")
    List<PaymentPlan> findByNumberLoanAndDateSubsequent(@Param("numberLoan") Integer numberLoan,
                                              @Param("datePayment") Date dayPayment);
}
