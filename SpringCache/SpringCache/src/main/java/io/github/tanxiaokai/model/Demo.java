package io.github.tanxiaokai.model;

import java.io.Serializable;
import java.util.Date;

public class Demo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String value;
	private Date createTime;

	public Demo() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String toString() {
		return "Demo[id=" + id + " , value=" + value + " , createTime=" + createTime + "]";
	}
}
