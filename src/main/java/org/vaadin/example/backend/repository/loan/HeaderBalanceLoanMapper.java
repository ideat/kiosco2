package org.vaadin.example.backend.repository.loan;

import org.vaadin.example.backend.entity.loan.HeaderBalanceLoan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HeaderBalanceLoanMapper {
    
    @Select(" select prmprnpre, prmprcage, gbagenomb, prmprplzo," +
            " prmprcmon, prtcrdesc, prmprmapt, prmprmdes, prmprfdes," +
            " p1.prcondesc prmprstat, prmpruplz, prmprfvac " +
            " from gbage" +
            " inner join prmpr on gbagecage = prmprcage" +
            " inner join prtcr on prtcrtcre = prmprtcre" +
            " inner join prcon p1 on p1.prconpref = 4 and p1.prconcorr= prmprstat " +
            " where prmprnpre = #{loanNumber}" +
            " and prmprstat in (2,3,4,5,6)")
    HeaderBalanceLoan getHeaderLoan(@Param("loanNumber") Integer loanNumber);


}
