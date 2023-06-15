package org.vaadin.example.backend.repository.loan;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.vaadin.example.backend.entity.loan.ChargesDeferred;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ChargesDeferredMapper {

    @Select("select prdifnpre, prdiffreg, prdifcarg, prdifmori, " +
            " prdifmapr, prdifcuot, prdifsald, prdiffulp, prdifglos, " +
            " prdiffmrc, prdiffapl, prdifccap " +
            " from prdif" +
            " where prdifnpre = #{loanNumber}")
    List<ChargesDeferred> findByLoanNumber(@Param("loanNumber") Integer loanNumber);

    @Select("select distinct prdifnpre, prdiffreg, prdifcarg, prdifmori, " +
            " prdifmapr, prdifcuot, prdifsald, prdiffulp, prdifglos, " +
            " prdiffmrc, prdiffapl, prdifccap " +
            " from prdif " +
            " inner join prpdd on prpddinpr = prdifcarg" +
            " where prdifnpre = #{loanNumber}")
    Optional<ChargesDeferred> findInterestByLoanNumber(@Param("loanNumber") Integer loanNumber);


    @Select("select distinct prdifnpre, prdiffreg, prdifcarg, prdifmori, " +
            " prdifmapr, prdifcuot, prdifsald, prdiffulp, prdifglos, " +
            " prdiffmrc, prdiffapl, prdifccap " +
            " from prdif " +
            " inner join prpsg on prpsgcarn = prdifcarg" +
            " where prdifnpre = #{loanNumber}")
    List<ChargesDeferred> findApportionmentChargeByLoanNumber(@Param("loanNumber") Integer loanNumber);

    @Select("select distinct prdifnpre, prdiffreg, prdifcarg, prdifmori, " +
            " prdifmapr, prdifcuot, prdifsald, prdiffulp, prdifglos, " +
            " prdiffmrc, prdiffapl, prdifccap " +
            " from prdif " +
            " inner join prdsg on prdsgcarn = prdifcarg" +
            " where prdifnpre = #{loanNumber}")
    List<ChargesDeferred> findSureChargeByLoanNumber(@Param("loanNumber") Integer loanNumber);
}
