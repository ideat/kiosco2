package org.vaadin.example.backend.repository.loan;

import org.vaadin.example.backend.entity.loan.LoanAccounts;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LoanAccountsMapper {

    @Select(" select prmprcage code_client, prmprnpre number_loan, prmprsald balance,  " +
            "gbconabre currency, prtcrdesc name_product, prcondesc state, prmprfvor expired_date, (t.prtsatbas + t.prtsasprd) rate  " +
            "from prmpr  " +
            "inner join prtcr on prtcrtcre = prmprtcre  " +
            "inner join prcon on prconpref = 4 and prconcorr= prmprstat  " +
            "inner join gbcon on (gbconpfij = 10 and gbconcorr = prmprcmon)    " +
            "inner join prtsa t on t.prtsanpre = prmprnpre and t.prtsafvig =  ( " +
            "select max(prtsafvig) " +
            "from prtsa " +
            "where prtsanpre = prmprnpre) " +
            "where prmprcage = #{codeClient}  " +
            "and prmprstat in (2,3,4,5,6)")
    List<LoanAccounts> findByCodeClient(@Param("codeClient") Integer codeClient);
}
