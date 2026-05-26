package com.porong.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionResponseDto {
    private Long collectionId;      // collection_books 테이블의 id
    private Long popupId;           // collection_books 테이블의 popup_id
    private String popupTitle;      // popups 테이블의 title
    private Long keyringId;         // collection_books 테이블의 keyring_id
    private String keyringImageUrl; // keyrings 테이블의 keyring_image 매핑용
    private String createdAt;       // collection_books 테이블의 created_at
}