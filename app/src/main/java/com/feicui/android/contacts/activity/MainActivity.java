package com.feicui.android.contacts.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.feicui.android.contacts.R;
import com.feicui.android.contacts.adapter.PhoneTypeAdapter;
import com.feicui.android.contacts.entity.PhoneTypeEntity;
import com.feicui.android.contacts.sqlite.MyOpenHelper;


public class MainActivity extends BaseActivity implements OnItemClickListener{
	//电话类型列表
	ListView lv_phoneType;
	//是否双击退出计时状态
	boolean is_exit = false;
	//第一次点击退出的时间戳
	long l_firstClickTime;
	//第二次点击退出的时间戳
	long l_secondClickTime;
	//电话类型适配器
	PhoneTypeAdapter ptAdapter;
	//SQLite自定义帮助类
	MyOpenHelper mHelper;
	//loading界面
	LinearLayout ll_loading;
	//要存放数据库的路径
	private static String DATABASE_PATH = "/data/data/com.feicui.android.contacts/databases";
	//数据库名
	private static String DABABASE_NAME = "commonnum.db";
	ArrayList<PhoneTypeEntity> list = new ArrayList<PhoneTypeEntity>();
	@Override
	protected int setContent() {
		// TODO Auto-generated method stub
		return R.layout.activity_main;
	}

	@Override
	protected void initView() {
		lv_phoneType = (ListView) findViewById(R.id.lv_phoneType);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		requestPermission();
		//启动初始化异步任务
		new InitTask().execute();
	}

	@Override
	protected void setListener() {
		lv_phoneType.setOnItemClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//双击退出
		doubleColickExit(keyCode, event);
		return true;
	}
	/**
	 * @description 初始化电话类型数据列表
	 */
	private void initList(){
		//通过构造函数实例化
		mHelper = new MyOpenHelper(this);
		//调用相应的方法，往集合中传入数据
		list = mHelper.selectClass();
		ptAdapter = new PhoneTypeAdapter(this,list);
	}
	/**
	 * @description 双击退出函数
	 * @param keyCode
	 * @param event
	 */
	private void doubleColickExit(int keyCode,KeyEvent event){
		//当用户第一次点击返回钮时
		if(keyCode == KeyEvent.KEYCODE_BACK && is_exit == false){
			//设置记录标志为true
			is_exit = true;
			//获得第一次点击的时间戳
			l_firstClickTime = System.currentTimeMillis();
			Toast.makeText(this, getString(R.string.exit_toast),Toast.LENGTH_SHORT).show();
		}
		//用户第二次点击返回钮
		else if(keyCode == KeyEvent.KEYCODE_BACK && is_exit == true){
			//记录下第二次点击退出的时间
			l_secondClickTime = System.currentTimeMillis();
			//时间差在两秒之内，退出程序
			if(l_secondClickTime - l_firstClickTime <2000){
				finish();
			}
			else {
				//重置记录退出时间标志
				is_exit = false;
				//超出2000毫秒时，重新开始本函数逻辑
				doubleColickExit(keyCode, event);
			}
		}
	}

	/**
	 * @description 从raw文件夹中导入随APK发布的数据库
	 */
	private void importDatabase(){
		try {
			//创建数据库目录，若数据库目录不存在，创建单层目录
			File file = new File(DATABASE_PATH);
			File file2 = new File(DATABASE_PATH, DABABASE_NAME);
			//判断文件是否存在，如不存在则创建该文件，存在就直接返回
			if(!file.exists()){
				file.mkdir();
				if(!file2.exists()){
					file2.createNewFile();
					//获得自带数据库的输入流
					InputStream ip = this.getAssets().open(DABABASE_NAME);
					//创建将被导入的数据库输出流
					FileOutputStream fop = new FileOutputStream(file2);
					//创建缓冲区
					byte [] buffer = new byte[1024];

					//将数据读入缓冲区，并写入输出流
					while(ip.read(buffer) != -1){
						//将缓冲区中的数据写入输出流
						fop.write(buffer);
						//重置缓冲区
						buffer = new byte[1024];
					}

					ip.close();
					fop.flush();
					fop.close();
				}
			}
			else{
				return;
			}
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//异步初始化操作任务类
	class InitTask extends AsyncTask<Void, Void, Void> {

		//任务启动后在异步线程中执行的代码，不可操作UI
		@Override
		protected Void doInBackground(Void... params) {
			//转移数据库文件
			importDatabase();
			//装载ListView
			initList();
			return null;
		}

		//任务启动之前执行的代码，可操作UI
		@Override
		protected void onPreExecute() {
			//让loding界面显示
			ll_loading.setVisibility(View.VISIBLE);
		}

		//任务完成之后执行的代码，可操作UI
		@Override
		protected void onPostExecute(Void aVoid) {
			//隐藏loding界面
			ll_loading.setVisibility(View.GONE);
			//给列表设置适配器
			lv_phoneType.setAdapter(ptAdapter);

		}
	}
	private void requestPermission(){
		if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
			ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},0);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode){
			case 0:
				if(grantResults[0]==PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(MainActivity.this,"权限申请成功", Toast.LENGTH_SHORT).show();
				}
				else{
					finish();
				}
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent;
		String type = list.get(position).getTypeName();
		if(type.equals("本地电话")){
			//当点击本地电话时 启动本地拨号界面
			intent = new Intent(Intent.ACTION_DIAL);
			startActivity(intent);
		}else{
			//当点击其他项时
			intent = new Intent(this, PhoneNumberActivity.class);
			//把点击的item的position（从1开始）组合成一个表名并传到下个界面
			intent.putExtra("tableName","table" + position );
			startActivity(intent);
		}
	}
}
