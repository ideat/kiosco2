package org.vaadin.example.backend.repository.loan;

import org.vaadin.example.backend.entity.loan.ToDiffer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ToDifferMapper {

    @Select("select prdifnpre, prdiffreg, prdifsald, prdifcarg " +
            " from prdif " +
            " where prdifnpre = #{loanNumber} " +
            " and prdifmrcb = 0")
    List<ToDiffer> findByLoanNumber(@Param("loanNumber") Integer loanNumber);
}
