package com.hihi.square.domain.store.dto.request;

import java.util.ArrayList;
import java.util.List;

import com.hihi.square.domain.image.dto.request.ImageRequestDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StoreNoticeUpdateRequestDto {

	private Integer snoId;

	private String content;

	private Integer usrId;

	private String state;

	private List<ImageRequestDto> images = new ArrayList<>();

}
