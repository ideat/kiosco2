package org.vaadin.example.backend.repository.client;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface IdimgMapper {

    @Select(" select idimgfirm " +
            " from idimg " +
            " where idimgcage=#{codeClient} " +
            " and idimgtimg=1" )
    Optional<byte[]> getImageClient(@Param("codeClient") Integer codeClient);
}
