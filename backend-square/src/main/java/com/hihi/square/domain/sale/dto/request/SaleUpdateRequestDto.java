package com.hihi.square.domain.sale.dto.request;

import com.hihi.square.domain.board.entity.Status;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SaleUpdateRequestDto {
    private Integer salId;
    private String name; // 타임세일 명
    private LocalDateTime startAt; // 시작 일/시간
    private LocalDateTime finishedAt; // 마지막 일/시간
    private Integer price; // 총 가격 -> 팔 가격
    private List<SaleMenuFormDto> menus = new ArrayList<>(); // 메뉴 리스트
    @Enumerated(EnumType.STRING)
    private Status status;

}
