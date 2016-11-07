package com.feicui.android.contacts.sqlite;

import java.util.ArrayList;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.feicui.android.contacts.entity.PhoneNumberEntity;
import com.feicui.android.contacts.entity.PhoneTypeEntity;

/**
 *
 * @author G505
 * @description SQLite帮助类
 */

public class MyOpenHelper extends SQLiteOpenHelper{
	//数据库版本
	public final static int DATABASE_VERSION = 1;
	//数据库名称
	private static String DATABASE_NAME = "commonnum.db";
	//电话类型的表名
	private static String TABLE_NAME_CLASSLIST = "classlist";
	//用于存放电话类型的list
	private ArrayList<PhoneTypeEntity> classlist = new ArrayList<PhoneTypeEntity>();
	//用于存放相应的电话信息的list
	private ArrayList<PhoneNumberEntity> msglist = new ArrayList<PhoneNumberEntity>();
	//数据库
	private SQLiteDatabase DB;
	//游标
	private Cursor cursor;
	public MyOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

	/**
	 * @description 查询classlist表的方法
	 *
	 */
	public ArrayList<PhoneTypeEntity> selectClass(){
		DB = this.getReadableDatabase();
		cursor = DB.query(TABLE_NAME_CLASSLIST, new String[]{"*"}, null, null, null, null, null);
		classlist.add(new PhoneTypeEntity("本地电话"));
		while(cursor.moveToNext()){
			PhoneTypeEntity type = new PhoneTypeEntity();
			type.setTypeName((cursor.getString(cursor.getColumnIndexOrThrow("name"))));
			classlist.add(type);
		}
		DB.close();
		return classlist;
	}
	/**
	 * 查询相应表名的表的信息
	 * @param tableName 要查询的表名
	 *
	 */
	public ArrayList<PhoneNumberEntity> selectMsg(String tableName){
		DB = this.getReadableDatabase();
		cursor = DB.query(tableName, new String[]{"*"}, null, null, null, null, null);
		while(cursor.moveToNext()){
			PhoneNumberEntity msg = new PhoneNumberEntity();
			msg.setPhoneName(cursor.getString(2));
			msg.setPhoneNumber(cursor.getString(1));
			msglist.add(msg);
		}
		DB.close();
		return msglist;
	}
}
