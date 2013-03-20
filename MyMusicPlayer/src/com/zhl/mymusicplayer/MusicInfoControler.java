/**
 * 
 */
package com.zhl.mymusicplayer;

import android.database.Cursor;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * @author dehoo-ZhongHeliang 2013-3-19下午21:44:32
 * @version jdk 1.6.0_03; sdk 4.2.0
 */
public class MusicInfoControler
{
	private static MusicInfoControler mInstance = null;
	private MusicPlayerApp pApp = null;

	public static MusicInfoControler getInstance(MusicPlayerApp app)
	{
		if (mInstance == null)
		{
			mInstance = new MusicInfoControler(app);
		}
		return mInstance;
	}
	
	public MusicInfoControler(MusicPlayerApp app)
	{
		pApp = app;
	}
	
	public MusicPlayerApp getMusicPlayer()
	{
		return pApp;
	}
	
	/* 查询指定信息 */
	private Cursor query(Uri uri,String[] prjs,String selection,String[] selectArgs,String order)
	{
		ContentResolver resolver = pApp.getContentResolver();
		if (resolver == null)
		{
			return null;
		}
		return resolver.query(uri,prjs,selection,selectArgs,order);
	}
	/* 得到所有歌曲 */
	public Cursor getAllSongs()
	{
		return query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
	}
}
