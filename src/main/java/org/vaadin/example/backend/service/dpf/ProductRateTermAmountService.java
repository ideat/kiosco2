package org.vaadin.example.backend.service.dpf;

import org.vaadin.example.backend.entity.dpf.dto.ProductRateTermAmount;
import org.vaadin.example.backend.repository.loan.ProductRateTermAmountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductRateTermAmountService {

    @Autowired
    private ProductRateTermAmountMapper mapper;

    public List<ProductRateTermAmount> findAll() {
        return mapper.findAll();
    }
}
