package com.porong.backend.mapper;

import com.porong.backend.dto.response.CollectionResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CollectionMapper {

    @Select("SELECT cb.id AS collectionId, " +
            "       cb.popup_id AS popupId, " +
            "       p.title AS popupTitle, " +
            "       cb.keyring_id AS keyringId, " +
            "       k.keyring_image_url AS keyringImageUrl, " +
            "       cb.created_at AS createdAt " +
            "FROM collection_books cb " +
            "JOIN popups p ON cb.popup_id = p.id " +
            "JOIN keyrings k ON cb.keyring_id = k.id " +
            "WHERE cb.user_id = #{userId} " +
            "ORDER BY cb.created_at DESC")
    List<CollectionResponseDto> selectMyCollections(@Param("userId") Long userId);
}