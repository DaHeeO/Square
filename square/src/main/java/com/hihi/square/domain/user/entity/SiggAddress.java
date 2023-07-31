package com.hihi.square.domain.user.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

// @Entity
// @Data
// @SuperBuilder
// @NoArgsConstructor
// @AllArgsConstructor
// @Table(name="sigg_address")
public class SiggAddress {
	@Id
	@GeneratedValue
	@Column(name="asi_id")
	private int asiId;

	@Column(name="adm_code")
	private String amdCode;
	private String name;

	@ManyToOne
	@JoinColumn(name = "asd_id")
	private SidoAddress sidoAddress;

	@OneToMany(mappedBy = "asi_id")
	private List<EmdAddress> emdAddressList;

}
