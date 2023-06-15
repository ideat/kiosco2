package org.vaadin.example.backend.repository.savingBank;

import org.vaadin.example.backend.entity.savingBank.BalanceAccount;
import org.vaadin.example.backend.entity.savingBank.SavingBankBalance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface SavingBankMapper {

    @Select("select b.camcancta account,c.catrnftra transaction_date,c.catrnhora transaction_time,c.catrnagen agency, " +
            "d.catmvdesc description, " +
            "round(decode(c.catrnimpo,(select e.catrnimpo " +
            "      from catrn e " +
            "      where e.catrnncta=c.catrnncta " +
            "      and e.catrnpref=c.catrnpref " +
            "      and e.catrncorr=c.catrncorr " +
            "      and e.catrnftra=c.catrnftra " +
            "      and e.catrnntra=c.catrnntra " +
            "      and e.catrnstat=0 " +
            "      and catrnimpo>=0),c.catrnimpo,(select e.catrnimpo " +
            "                                     from catrn e " +
            "                                     where e.catrnncta=c.catrnncta " +
            "                                     and e.catrnpref=c.catrnpref " +
            "                                     and e.catrncorr=c.catrncorr " +
            "                                     and e.catrnftra=c.catrnftra " +
            "                                     and e.catrnntra=c.catrnntra " +
            "                                     and e.catrnstat=0 " +
            "                                     and catrnimpo<0),c.catrnimpo*0),2) debit, " +
            "round(decode(c.catrnimpo,(select e.catrnimpo " +
            "      from catrn e " +
            "      where e.catrnncta=c.catrnncta " +
            "      and e.catrnpref=c.catrnpref " +
            "      and e.catrncorr=c.catrncorr " +
            "      and e.catrnftra=c.catrnftra " +
            "      and e.catrnntra=c.catrnntra " +
            "      and e.catrnstat=0 " +
            "      and catrnimpo>=0),c.catrnimpo*0,(select e.catrnimpo " +
            "                                       from catrn e " +
            "                                       where e.catrnncta=c.catrnncta " +
            "                                       and e.catrnpref=c.catrnpref " +
            "                                       and e.catrncorr=c.catrncorr " +
            "                                       and e.catrnftra=c.catrnftra " +
            "                                       and e.catrnntra=c.catrnntra " +
            "                                       and e.catrnstat=0 " +
            "                                       and catrnimpo<0),c.catrnimpo*-1),2) asset,(b.camcasact*-1) balance," +
            " gbagenomb full_name, gbagendid id_card " +
            " from gbage a,camca b, catrn c,catmv d " +
            " where a.gbagecage=b.camcacage " +
            " and b.camcancta=c.catrnncta " +
            " and c.catrnpref=d.catmvpref " +
            " and c.catrncorr=d.catmvcorr " +
            " and b.camcancta= #{account} "+
            " and c.catrnstat=0" +
            " order by 2,3 ")
    List<SavingBankBalance> getSavingBankBalance(@Param("account") String account);

    @Select("select b.camcancta account,c.catrnftra transaction_date,c.catrnhora transaction_time,c.catrnagen agency, " +
            "d.catmvdesc description, " +
            "round(decode(c.catrnimpo,(select e.catrnimpo " +
            "      from catrn e " +
            "      where e.catrnncta=c.catrnncta " +
            "      and e.catrnpref=c.catrnpref " +
            "      and e.catrncorr=c.catrncorr " +
            "      and e.catrnftra=c.catrnftra " +
            "      and e.catrnntra=c.catrnntra " +
            "      and e.catrnstat=0 " +
            "      and catrnimpo>=0),c.catrnimpo,(select e.catrnimpo " +
            "                                     from catrn e " +
            "                                     where e.catrnncta=c.catrnncta " +
            "                                     and e.catrnpref=c.catrnpref " +
            "                                     and e.catrncorr=c.catrncorr " +
            "                                     and e.catrnftra=c.catrnftra " +
            "                                     and e.catrnntra=c.catrnntra " +
            "                                     and e.catrnstat=0 " +
            "                                     and catrnimpo<0),c.catrnimpo*0),2) debit, " +
            "round(decode(c.catrnimpo,(select e.catrnimpo " +
            "      from catrn e " +
            "      where e.catrnncta=c.catrnncta " +
            "      and e.catrnpref=c.catrnpref " +
            "      and e.catrncorr=c.catrncorr " +
            "      and e.catrnftra=c.catrnftra " +
            "      and e.catrnntra=c.catrnntra " +
            "      and e.catrnstat=0 " +
            "      and catrnimpo>=0),c.catrnimpo*0,(select e.catrnimpo " +
            "                                       from catrn e " +
            "                                       where e.catrnncta=c.catrnncta " +
            "                                       and e.catrnpref=c.catrnpref " +
            "                                       and e.catrncorr=c.catrncorr " +
            "                                       and e.catrnftra=c.catrnftra " +
            "                                       and e.catrnntra=c.catrnntra " +
            "                                       and e.catrnstat=0 " +
            "                                       and catrnimpo<0),c.catrnimpo*-1),2) asset,(b.camcasact*-1) balance," +
            " gbagenomb full_name, gbagendid id_card " +
            " from gbage a,camca b, catrn c,catmv d " +
            " where a.gbagecage=b.camcacage " +
            " and b.camcancta=c.catrnncta " +
            " and c.catrnpref=d.catmvpref " +
            " and c.catrncorr=d.catmvcorr " +
            " and b.camcancta= #{account} "+
            " and c.catrnftra between #{initDate} and #{endDate} " +
            " and c.catrnstat=0" +
            " order by 2,3 ")
    List<SavingBankBalance> getSavingBankBalanceBetweenDates(@Param("account") String account,
                                                             @Param("initDate") Date initDate,
                                                             @Param("endtDate") Date endtDate);

    @Select("select b.camcancta account,c.catrnftra transaction_date,c.catrnhora transaction_time,c.catrnagen agency, " +
            "d.catmvdesc description, " +
            "round(decode(c.catrnimpo,(select e.catrnimpo " +
            "      from catrn e " +
            "      where e.catrnncta=c.catrnncta " +
            "      and e.catrnpref=c.catrnpref " +
            "      and e.catrncorr=c.catrncorr " +
            "      and e.catrnftra=c.catrnftra " +
            "      and e.catrnntra=c.catrnntra " +
            "      and e.catrnstat=0 " +
            "      and catrnimpo>=0),c.catrnimpo,(select e.catrnimpo " +
            "                                     from catrn e " +
            "                                     where e.catrnncta=c.catrnncta " +
            "                                     and e.catrnpref=c.catrnpref " +
            "                                     and e.catrncorr=c.catrncorr " +
            "                                     and e.catrnftra=c.catrnftra " +
            "                                     and e.catrnntra=c.catrnntra " +
            "                                     and e.catrnstat=0 " +
            "                                     and catrnimpo<0),c.catrnimpo*0),2) debit, " +
            "round(decode(c.catrnimpo,(select e.catrnimpo " +
            "      from catrn e " +
            "      where e.catrnncta=c.catrnncta " +
            "      and e.catrnpref=c.catrnpref " +
            "      and e.catrncorr=c.catrncorr " +
            "      and e.catrnftra=c.catrnftra " +
            "      and e.catrnntra=c.catrnntra " +
            "      and e.catrnstat=0 " +
            "      and catrnimpo>=0),c.catrnimpo*0,(select e.catrnimpo " +
            "                                       from catrn e " +
            "                                       where e.catrnncta=c.catrnncta " +
            "                                       and e.catrnpref=c.catrnpref " +
            "                                       and e.catrncorr=c.catrncorr " +
            "                                       and e.catrnftra=c.catrnftra " +
            "                                       and e.catrnntra=c.catrnntra " +
            "                                       and e.catrnstat=0 " +
            "                                       and catrnimpo<0),c.catrnimpo*-1),2) asset,(b.camcasact*-1) balance, " +
            " gbagenomb full_name, gbagendid id_card " +
            " from gbage a,camca b, catrn c,catmv d " +
            " where a.gbagecage=b.camcacage " +
            " and b.camcancta=c.catrnncta " +
            " and c.catrnpref=d.catmvpref " +
            " and c.catrncorr=d.catmvcorr " +
            " and b.camcancta= #{account} "+
            " and c.catrnstat=0" +
            " order by 2,3 desc " +
            " limit #{movement}")
    List<SavingBankBalance> getSavingBankBalanceLastNMovement(@Param("account") String account,
                                                              @Param("movement") Integer movement);

    @Select(" select catrnncta account,sum(catrnimpo) balance" +
            " from catrn " +
            " where catrnstat=0 " +
            " and catrnncta = #{account} " +
            " and c.catrnftra < #{initDate}" +
            " group by 1 ")
    BalanceAccount getBalanceToInitDate(@Param("account") String account,
                                       @Param("initDate") Date initDate);

    @Select(" select first(#{numberRegister}) catrnncta account, sum(catrnimpo) balance " +
            " from catrn " +
            " where catrnstat=0 " +
            " and catrnncta = #{account} " +
            " group by 1 ")
    BalanceAccount getBalanceNRegisters(@Param("account") String account,
                                       @Param("numberRegister") Integer initDate);
}
