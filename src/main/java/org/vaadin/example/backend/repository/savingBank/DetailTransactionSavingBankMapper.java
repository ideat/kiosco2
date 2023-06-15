package org.vaadin.example.backend.repository.savingBank;

import org.vaadin.example.backend.entity.savingBank.DetailTransactionSanvingBank;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface DetailTransactionSavingBankMapper {

    @Select(" select catrnntra, catrnftra, catrnndoc,  catmvdesc, catrnimpo " +
            " from catrn  " +
            " inner join catmv on catmvpref = catrnpref and catmvcorr = catrncorr " +
            " where catrnncta = #{account} " +
            " and catrnstat = 0 " +
            " order by catrnntra")
    List<DetailTransactionSanvingBank> findAllByAccount(@Param("account") String account);

    @Select("select catrnntra, catrnftra, catrnndoc,  catmvdesc, catrnimpo " +
            "from catrn  " +
            "inner join catmv on catmvpref = catrnpref and catmvcorr = catrncorr " +
            "where catrnncta = #{account} " +
            " and catrnstat = 0 " +
            " and catrnftra between #{initDate} and #{endDate} " +
            "order by catrnntra")
    List<DetailTransactionSanvingBank> findByAccountAndRangeDates(@Param("account") String account,
                                                                  @Param("initDate") Date initDate,
                                                                  @Param("endDate") Date endDate);

    @Select(" select FIRST(5) catrnntra, catrnftra, catrnndoc,  catmvdesc, catrnimpo " +
            " from catrn  " +
            " inner join catmv on catmvpref = catrnpref and catmvcorr = catrncorr " +
            " where catrnncta = #{account} " +
            " and catrnstat = 0 " +
            " order by catrnntra desc ")
    List<DetailTransactionSanvingBank> findByAccountAndNumberRecords(@Param("account") String account,
                                                                     @Param("numberRecords") Integer numberRecords);
}
