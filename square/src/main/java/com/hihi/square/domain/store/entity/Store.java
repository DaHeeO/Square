package com.hihi.square.domain.store.entity;

<<<<<<< HEAD
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hihi.square.domain.user.entity.EmdAddress;
import com.hihi.square.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
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
=======
import com.hihi.square.domain.store.dto.request.StoreUpdateRequestDto;
import com.hihi.square.domain.user.entity.EmdAddress;
import com.hihi.square.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="store")
@DiscriminatorValue("UA02")
public class Store extends User {
	@ManyToOne
	@JoinColumn(name="aem_id")
	private EmdAddress emdAddress;

	private String address;
	@Column(name="store_name")
	private String storeName;
	@Column(name="store_phone")
>>>>>>> 5c5fec79860f2613543dbfd4cb240edcebdb6ad1
	private String storePhone;
	private String content;
	@Enumerated(EnumType.STRING)
	private BankType bank;
	private String account;

<<<<<<< HEAD
=======
	public void updateStoreInfo(StoreUpdateRequestDto request, EmdAddress emdAddress) {
		this.emdAddress = emdAddress;
		this.address = request.getAddress();
		this.storeName = request.getStoreName();
		this.storePhone = request.getStorePhone();
		this.content = request.getContent();
		this.bank = request.getBank();
		this.account = request.getAccount();

	}
>>>>>>> 5c5fec79860f2613543dbfd4cb240edcebdb6ad1
}
