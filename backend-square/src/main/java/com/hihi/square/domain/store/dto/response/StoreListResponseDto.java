package com.hihi.square.domain.store.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreListResponseDto {

    private Integer scbId;
    private Integer storeId;
    private String storeName;
    private String content;
    private String storeAddress;
    private String mainMenu;
    private String logo;
    private Boolean isOpened;
    private Float latitude;
    private Float longitude;
}
