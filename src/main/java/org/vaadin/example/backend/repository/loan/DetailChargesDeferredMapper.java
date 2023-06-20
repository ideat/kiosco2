package org.vaadin.example.backend.repository.loan;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.vaadin.example.backend.entity.loan.DetailChargesDeferred;

import java.util.List;

@Mapper
public interface DetailChargesDeferredMapper {

    @Select(" select prdipnpre, prdipcarg, prdipfpag, " +
            " prdipcuot, prdipfreg, prdippref, prdipccon," +
            " prdipmrcb, prdipfpro " +
            " from prdip " +
            " where prdipnpre = #{loanNumber} " +
            " and prdipcarg = #{code}" +
            " and prdipmrcb = 0 " +
            " order by prdipcarg, prdipfpag ")
    List<DetailChargesDeferred> findByLoanNumberAndCode(@Param("loanNumber") Integer loanNumber,
                                                        @Param("code") Integer code);
}
