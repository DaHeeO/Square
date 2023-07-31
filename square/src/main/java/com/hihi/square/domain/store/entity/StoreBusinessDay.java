package com.hihi.square.domain.store.entity;

import java.sql.Time;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@Table(name="store_business_day")
public class StoreBusinessDay {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="sbd_id")
	private Integer sbdId;

	@JoinColumn(name = "usr_id")
	@ManyToOne
	private Store store;

	@Enumerated(EnumType.STRING)
	private DayType day;

	@Enumerated(EnumType.STRING)
	private BusinessDayType type;

	@Column(name="started_at")
	private Time startedAt;

	@Column(name="finished_at")
	private Time finishedAt;

}
