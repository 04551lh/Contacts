package com.feicui.android.contacts.activity;


import android.content.Intent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.feicui.android.contacts.R;


public class SplashActivity extends BaseActivity{
	//开屏页图片
	ImageView iv_splash;
	//淡入动画
	AlphaAnimation alphaAnim;
	protected int setContent() {
		return R.layout.activity_splash;
	}

	@Override
	protected void initView() {
		iv_splash = (ImageView) findViewById(R.id.iv_splash);
	}

	@Override
	protected void setListener() {

	}

	/**
	 * @description 加载动画
	 */
	private void initAnimation(){
		//淡入动画
		alphaAnim = new AlphaAnimation(0f, 1f);
		//设置动画时间
		alphaAnim.setDuration(3000);
		//设置动画播放结束后的监听
		alphaAnim.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				//跳转到主页
				Intent intent = new  Intent(SplashActivity.this, MainActivity.class);
				//开启
				startActivity(intent);
				//关闭开屏页
				finish();
			}
		});
		//开启动画
		iv_splash.startAnimation(alphaAnim);
	}

	/**
	 * 当前页是否已获得焦点
	 *
	 * @param hasFocus true代表获得焦点，false相反
	 */

	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus){
			initAnimation();
		}
	}
}
