package org.vaadin.example.backend.repository.savingBank;

import org.vaadin.example.backend.entity.savingBank.HeaderBalanceSavingBank;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HeaderBalanceSavingBankMapper {

    @Select(" SELECT g.gbagenomb,g.gbagecage,g.gbagedir1, g.gbagedir2, g.gbagetlfo,g.gbagetlfd,   " +
            "b.camcancta, b.camcafpig, b.camcacmon,  c.cacondesc,  " +
            "round(decode(b.camcatpca, " +
            "(select cc.catcatpca " +
            "from catca cc " +
            "where cc.catcatpca= b.camcatpca " +
            "and cc.catcatpca not in(select p.caprptpca " +
            "                      from caprp p " +
            "                      where p.caprptpca=cc.catcatpca) " +
            "and b.camcatasa=0),d.canidtasa,(select cc.catcatpca " +
            "             from catca cc " +
            "          where cc.catcatpca= b.camcatpca " +
            "          and cc.catcatpca not in(select p.caprptpca " +
            "                                                      from caprp p " +
            "                                                      where p.caprptpca=cc.catcatpca) " +
            "          and b.camcatasa>0),b.camcatasa,(select cc.catcatpca " +
            "                                                              from catca cc " +
            "                                                              where cc.catcatpca= b.camcatpca " +
            "                                                              and cc.catcatpca in(select p.caprptpca " +
            "                                                                                  from caprp p " +
            "                                                                                  where p.caprptpca=cc.catcatpca)),d.canidtasa),2) tasa_prod " +
            "                                                                                           " +
            "from camca b, canid d, gbage g, cacon c  " +
            "where g.gbagecage = b.camcacage " +
            "and c.caconpref = 4 and c.caconcorr = b.camcastat " +
            "and b.camcatpca = d.canidtpca " +
            "and b.camcacmon = d.canidcmon " +
            "and d.canidmrcb = 0  " +
            "and b.camcancta = #{account}")
    HeaderBalanceSavingBank findByAccount(@Param("account") String account);


}
