package com.hihi.square.domain.sale.dto.response;

import com.hihi.square.domain.board.entity.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class StoreSaleDetailDto {
    private Integer id;
    private String name;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private LocalDateTime realFinishedAt;
    private Integer price;
    private Status status;
    private List<MenuDto> menus = new ArrayList<>();
}
