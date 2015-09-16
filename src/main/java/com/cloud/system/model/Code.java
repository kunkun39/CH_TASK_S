package com.cloud.system.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_sys_code")
public class Code implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String code;
	private String name;
	private String coderule;
	private Integer currentsn;
	private String restmode;
	private String reststr;
	private Date resttime;

	@Id
	@Column(length = 36)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(length = 100, nullable = false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(length = 100, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 100, nullable = false)
	public String getCoderule() {
		return coderule;
	}

	public void setCoderule(String coderule) {
		this.coderule = coderule;
	}

	public Integer getCurrentsn() {
		return currentsn;
	}

	public void setCurrentsn(Integer currentsn) {
		this.currentsn = currentsn;
	}

	@Column(length = 100)
	public String getRestmode() {
		return restmode;
	}

	public void setRestmode(String restmode) {
		this.restmode = restmode;
	}

	@Column(length = 100)
	public String getReststr() {
		return reststr;
	}

	public void setReststr(String reststr) {
		this.reststr = reststr;
	}

	@Temporal(TemporalType.DATE)
	public Date getResttime() {
		return resttime;
	}

	public void setResttime(Date resttime) {
		this.resttime = resttime;
	}

}
