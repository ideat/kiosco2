package org.vaadin.example.backend.repository.savingBank;

import org.vaadin.example.backend.entity.savingBank.SavingBankClient;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SavingBankClientMapper {
    @Select(" SELECT distinct g.gbagenomb full_name, b.camcancta account, ct.catcadesc product_name,gb.gbconabre currency, (b.camcasact)*-1 balance,c.cacondesc state,     " +
            "round(decode(b.camcatpca,     " +
            "(select cc.catcatpca     " +
            "from catca cc     " +
            "where cc.catcatpca= b.camcatpca     " +
            "and cc.catcatpca not in(select p.caprptpca     " +
            "                      from caprp p     " +
            "                      where p.caprptpca=cc.catcatpca)     " +
            "and b.camcatasa=0),d.canidtasa,(select cc.catcatpca     " +
            "                                 from catca cc     " +
            "                              where cc.catcatpca= b.camcatpca     " +
            "                              and cc.catcatpca not in(select p.caprptpca     " +
            "                                                      from caprp p     " +
            "                                                      where p.caprptpca=cc.catcatpca)     " +
            "                              and b.camcatasa>0),b.camcatasa,(select cc.catcatpca     " +
            "                                                              from catca cc     " +
            "                                                              where cc.catcatpca= b.camcatpca     " +
            "                                                              and cc.catcatpca in(select p.caprptpca     " +
            "                                                                                  from caprp p     " +
            "                                                                                  where p.caprptpca=cc.catcatpca)),d.canidtasa),2) rate     " +
            "                                                                                                                   " +
            "from camca b, canid d, gbage g, cacon c, catca ct, gbcon gb, cafir cf     " +
            "where g.gbagecage = b.camcacage    " +
            " and cf.cafirncta = b.camcancta " +
            "and ct.catcatpca = b.camcatpca     " +
            "and c.caconpref = 4 and c.caconcorr = b.camcastat     " +
            "and b.camcatpca = d.canidtpca     " +
            "and b.camcacmon = d.canidcmon     " +
            "and d.canidmrcb = 0      " +
            "and gb.gbconpfij = 5 and gb.gbconcorr = b.camcacmon     " +
            "and b.camcastat in (1,2)     " +
            "and cf.cafircage = #{codeClient} " +
            " and cf.cafirstat = 0 ")
    List<SavingBankClient> getSavingBanksClient(@Param("codeClient") Integer codeClient);


    @Select(" select gbagenomb full_name, camcancta account, catcadesc product_name, " +
            " gbconabre currency, (camcasact)*-1 balance, cacondesc state " +
            " from camca " +
            " inner join gbage on gbagecage = camcacage " +
            " inner join catca on catcatpca = camcatpca " +
            " inner join gbcon on gbconpfij = 5 and gbconcorr = camcacmon " +
            " inner join cacon on caconpref = 4 and caconcorr = camcastat" +
            " where camcacage = #{codeClient} " +
            " and camcastat in (1,2) ")
    List<SavingBankClient> getSavingBanksClient2(@Param("codeClient") Integer codeClient);

    
}
