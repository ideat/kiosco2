package org.vaadin.example.backend.repository.loan;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ParamDeferredMapper {

    List<ParamDeferredMapper> findAll();


}
