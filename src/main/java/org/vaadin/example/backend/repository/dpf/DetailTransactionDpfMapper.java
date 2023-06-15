package org.vaadin.example.backend.repository.dpf;

import org.vaadin.example.backend.entity.dpf.DetailTransactionDpf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper 
public interface DetailTransactionDpfMapper {
    
    @Select(" select pftdtndep, pftdtftra, pftdtttrn, pftdtitem, pftdtpref, pftdtccon, pftdtdesc, pftdtimpp " +
            " from pftdt " +
            " inner join pfcon on pfconpfij = 4 and pfconcorr = pftdtttrn " +
            " where pftdtndep = #{account}  " +
            " and pftdtmrcb = 0  " +
             "order by pftdtftra ")
    List<DetailTransactionDpf> findByAccount(@Param("account") String account);
}
