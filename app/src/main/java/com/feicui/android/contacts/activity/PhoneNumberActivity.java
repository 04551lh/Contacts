package com.feicui.android.contacts.activity;

import java.util.ArrayList;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.feicui.android.contacts.R;
import com.feicui.android.contacts.adapter.PhoneNumberAdapter;
import com.feicui.android.contacts.entity.PhoneNumberEntity;
import com.feicui.android.contacts.sqlite.MyOpenHelper;


/**
 * @author G505
 * @description 展示列表的页面
 */
public class PhoneNumberActivity extends BaseActivity implements OnItemClickListener {

    //显示电话号码的列表
    ListView lv_phoneNumber;
    //电话号码列表的适配器
    PhoneNumberAdapter pnAdapter;
    //loading界面
    LinearLayout ll_loading;
    //自定义的数据库
    private MyOpenHelper mhelper;
    //展示页面适配器
    PhoneNumberAdapter numberAdapter;
    //用于存放电话信息的list
    ArrayList<PhoneNumberEntity> mlist = new ArrayList<PhoneNumberEntity>();

    @Override
    protected int setContent() {
        return R.layout.activity_phone_number;
    }

    @Override
    protected void initView() {
        lv_phoneNumber = (ListView) findViewById(R.id.lv_phoneNumber);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);

        //开始装载界面资源
        new daaBaseInport().execute();
    }

    /**
     * 得到点击的listview所对应的信息
     */
    private void getMsg() {
        mhelper = new MyOpenHelper(this);
        Intent intent = getIntent();
        //调用selectMsg传入相应的表名并接收相应信息（存放在list中）
        mlist = mhelper.selectMsg(intent.getStringExtra("tableName"));
    }
    @Override
    protected void setListener() {
        lv_phoneNumber.setOnItemClickListener(this);
    }

    //异步初始化操作任务类
    class InitTask extends AsyncTask<Void, Void, Void> {

        //任务启动后在异步线程中执行的代码，不可操作UI
        @Override
        protected Void doInBackground(Void... params) {
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
            lv_phoneNumber.setAdapter(pnAdapter);

        }
    }

    /**
     * @description 装载ListView
     */
    private void initList() {
        getMsg();
        numberAdapter = new PhoneNumberAdapter(this, mlist);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //点击项的电话名字
        String name = mlist.get(position).getPhoneName();
        //点击项的电话号码
        final String number = mlist.get(position).getPhoneNumber();
        //创建一个对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //标题
        builder.setTitle("警告");
        //对话框信息
        builder.setMessage("您将要拨打： " + name + "\n" + "TEL: "+ number);
        //点击拨打按钮时，拨打相应电话
        builder.setPositiveButton("拨打", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //电话号码
                Uri data = Uri.parse("tel:" + number);
                //拨打相应的电话
                Intent intent = new Intent("android.intent.action.CALL", data);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        //显示对话框
        builder.show();
    }
    class daaBaseInport extends AsyncTask<Void, Void, Void>{
        //任务启动后在异步线程中执行的代码，不可操作UI
        @Override
        protected Void doInBackground(Void... params) {
            //装载ListView
            initList();
            return null;
        }
        //任务启动之前执行的代码，可操作UI
        @Override
        protected void onPreExecute() {
            //让loading界面显示
            ll_loading.setVisibility(View.VISIBLE);
        }
        //任务完成之后执行的代码，可操作UI
        @Override
        protected void onPostExecute(Void aVoid) {
            //隐藏loading界面
            ll_loading.setVisibility(View.GONE);
            //给列表设置适配器
            lv_phoneNumber.setAdapter(numberAdapter);
        }
    }
}
