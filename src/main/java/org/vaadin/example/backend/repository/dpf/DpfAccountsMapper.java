package org.vaadin.example.backend.repository.dpf;

import org.vaadin.example.backend.entity.dpf.DpfAccounts;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DpfAccountsMapper {

    @Select(" select pfmdpndep number_dpf, pfmdpncrt number_certificate, pfmdpfvto expire_date,      " +
            " pfmdpfern renovation_date, pfmdpplzo term, pfmdpfreg register_date, gbagecage code_client,      " +
            " gbconabre currency, gbagenomb full_name, pfmdpcapi amount,     " +
            " pfhtsdesc product, pfdtstasa rate     " +
            " from gbage      " +
            " inner join pfmdp on gbagecage = pfmdpcage      " +
            " inner join pfhts on pfmdptdep = pfhtstdep and pfhtscmon = pfmdpcmon and pfmdpplzo between pfhtsplzi and pfhtsplzf     " +
            " inner join pftdp on pfmdptdep = pftdptdep     " +
            " inner join gbcon on gbconpfij = 5 and gbconcorr = pfmdpcmon      " +
            " inner join pfdts on pfdtsnive = pfhtsnive     " +
            " where pfmdpstat = 1 " +
            " and pfmdpmrcb = 0" +
            " and pfmdpcage = #{codeClient}")
    List<DpfAccounts> getDpfsByClient(@Param("codeClient") Integer codeClient);



}
