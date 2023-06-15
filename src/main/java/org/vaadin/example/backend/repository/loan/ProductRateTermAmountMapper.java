package org.vaadin.example.backend.repository.loan;

import org.vaadin.example.backend.entity.dpf.dto.ProductRateTermAmount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductRateTermAmountMapper {

    @Select(" select pfhtsdesc product_name, pfhtscmon currency, pfhtsplzi init_term, " +
            "pfhtsplzf end_term, pfdtstasa rate, pfdtsmoni init_amount, pfdtsmonf end_amount " +
            "from pfdts  " +
            "inner join pfhts on pfdtsnive = pfhtsnive     " +
            "order by 2,3")
    List<ProductRateTermAmount> findAll();

}
