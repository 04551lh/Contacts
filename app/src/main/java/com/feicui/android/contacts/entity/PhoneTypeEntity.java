package com.feicui.android.contacts.entity;

/**
 * @descriprion �绰����ʵ����
 * @author G505
 *
 */
public class PhoneTypeEntity {
	//�绰��������
	private String typeName;
	public PhoneTypeEntity() {
	}
	public PhoneTypeEntity(String type) {
		typeName = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	
}
