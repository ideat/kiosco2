package org.vaadin.example.backend.repository.loan;

import org.vaadin.example.backend.entity.loan.HeaderPaymentPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HeaderPaymentPlanMapper {

    @Select(" select prmprnpre, prmprmapt, prmprsald, prmprfdes, p2.prcondesc prmprstat, prmprcage, " +
            " gbagenomb, gbagendid, prtcrdesc, prmprppgi, prmprppgk, p1.prcondesc, prmprcmon, prmprplzo," +
            " prmpruplz " +
            " from prmpr " +
            " inner join gbage on prmprcage = gbagecage " +
            " inner join prtcr on prtcrtcre = prmprtcre " +
            " inner join prcon p1 on (p1.prconpref = 2 and p1.prconcorr= prmprfpag) " +
            " inner join prcon p2 on (p2.prconpref = 4 and p2.prconcorr = prmprstat) " +
            " where prmprnpre = #{numberLoan} ")
    HeaderPaymentPlan findByNumberLoan(@Param("numberLoan") Integer numberLoan);
}
