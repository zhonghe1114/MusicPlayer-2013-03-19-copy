package com.zhl.mymusicplayer;

import android.os.Bundle;
import android.app.Activity;
import android.app.Application;
import android.view.Menu;


/**
 * @author dehoo-ZhongHeliang 2013-3-19下午21:22:15
 * @version jdk 1.6.0_03; sdk 4.2.0
 */
public class MusicPlayerApp extends Application
{

	private MusicInfoControler mMusicInfoControler = null;
	
	public void onCreate()
	{
		super.onCreate();
		
		mMusicInfoControler = MusicInfoControler.getInstance(this); // 得到实例
	}
	
	public MusicInfoControler getMusicInfoControler()
	{
		return mMusicInfoControler;
	}

}
