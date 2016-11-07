package com.feicui.android.contacts.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicui.android.contacts.R;
import com.feicui.android.contacts.entity.PhoneTypeEntity;


public class PhoneTypeAdapter extends BaseAdapter{
	//待装载数据，为电话类型实体类列表
	ArrayList<PhoneTypeEntity> myData;
	//上下文
	Context mContext;
	/**
	 * @description 本适配器构造器，传入电话类型列表的实体类以装载数据
	 *
	 * @param data    载入的数据源
	 * @param content 上下文
	 */
	public PhoneTypeAdapter(Context content,ArrayList<PhoneTypeEntity> data) {
		mContext = content;
		myData = data;
	}
	@Override
	public int getCount() {
		return myData.size();
	}
	@Override
	public Object getItem(int position) {
		return myData.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//声明ViewHolder
		ViewHolder holder = null;
		//若为第一次显示当前item
		if(convertView == null){
			//获取布局加载器
			LayoutInflater inflater = LayoutInflater.from(mContext);
			//使用布局加载器加载item布局
			convertView = inflater.inflate(R.layout.item_phone_type_list, null);
			//保存ViewHolder的状态，存在当前convertView中
			holder = new ViewHolder();
			holder.tv_typeName = (TextView) convertView.findViewById(R.id.tv_typeName);
			holder.iv_arrowRight = (ImageView) convertView.findViewById(R.id.iv_arrowRight);
			convertView.setTag(holder);

		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_typeName.setText(myData.get(position).getTypeName());
		return convertView;
	}
	/**
	 * 用于优化性能的ViewHolder，保存Item布局中的控件
	 */
	public class ViewHolder {
		public TextView tv_typeName;//电话类型名称
		public ImageView iv_arrowRight;//右箭头
	}
}
