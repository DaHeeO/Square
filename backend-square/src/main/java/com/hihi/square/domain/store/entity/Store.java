package com.hihi.square.domain.store.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hihi.square.domain.store.dto.request.StoreUpdateRequestDto;
import com.hihi.square.domain.user.entity.EmdAddress;
import com.hihi.square.domain.user.entity.User;
import com.hihi.square.domain.user.repository.EmdAddressRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store")
@DiscriminatorValue("UA02")
public class Store extends User {
	@ManyToOne
	@JoinColumn(name = "aem_id")
	private EmdAddress emdAddress;

	private String address;
	@Column(name = "store_name")
	private String storeName;
	@Column(name = "store_phone")
	private String storePhone;
	private String content;
	@Enumerated(EnumType.STRING)
	private BankType bank;
	private String account;
	// 가게 로고 이미지
	private String logo;
	@Column(name = "open_time")
	private String openTime;
	private String banner;

	// @OneToMany(mappedBy = "store")
	// // @JoinColumn
	// @Builder.Default
	// private List<StoreBusinessDay> storeBusinessDayList = new ArrayList<>();

	private Float latitude;
	private Float longitude;
	private String hashtags;
	@Column(name = "is_opened")
	private Boolean isOpened;

	public void updateStoreInfo(StoreUpdateRequestDto request, EmdAddressRepository emdAddressRepository) {
		this.address = request.getAddress();
		this.storeName = request.getStoreName();
		this.storePhone = request.getStorePhone();
		this.content = request.getContent();
		this.bank = request.getBank();
		this.account = request.getAccount();
		this.hashtags = request.getHashtags();
		if (request.getBanner() != null){
			this.banner = request.getBanner();
		}
		if(request.getLogo() != null) {
			this.logo = request.getLogo();
		}
		if (request.getLatitude() != null) {
			this.latitude = request.getLatitude();
			this.longitude = request.getLongitude();
			this.emdAddress = emdAddressRepository.findByBCode(request.getBCode()).get();
		}
	}

	public void updateOpen(boolean isOpened) {
		this.isOpened = isOpened;
	}
}
