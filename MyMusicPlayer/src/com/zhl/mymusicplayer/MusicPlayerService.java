package com.zhl.mymusicplayer;
import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

/**
 * 
 */

/**
 * @author dehoo-ZhongHeliang 2013-3-19下午22:22:51
 * @version jdk 1.6.0_03; sdk 4.2.0
 */
public class MusicPlayerService extends Service
{
	private final IBinder mBinder = new LocalBinder();
	private MediaPlayer mMediaPlayer = null;
	// 命名广播的好习惯
	public static final String PLAYER_PREPARE_END = "com.zhl.musicplayerservice.prepared";
	public static final String PLAYER_COMPLETED = "com.zhl.com.yarin.musicplayerservice.playcompleted";
	
	MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener()
	{
		public void onCompletion(MediaPlayer mp)
		{
			broadcastEvent(PLAYER_COMPLETED);
		}
	};
	
	MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener()
	{
		public void onPrepared(MediaPlayer mp)
		{
			broadcastEvent(PLAYER_PREPARE_END);
		}
	};
	
	private void broadcastEvent(String what)
	{
		Intent intent = new Intent(what);
		sendBroadcast(intent);
	}
	
	public void onCreate()
	{
		super.onCreate();
		
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnPreparedListener(mPreparedListener);
		mMediaPlayer.setOnCompletionListener(mCompletionListener);
	}
	
	public class LocalBinder extends Binder
	{
		public MusicPlayerService getService()
		{
			return MusicPlayerService.this;
		}
	}

	public IBinder onBind(Intent arg0)
	{
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	public void setDataSource(String path)
	{
		try
		{
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(path);
			mMediaPlayer.prepare();
		}
		catch (IllegalArgumentException e)
		{
			return;
		}
		catch (IOException e)
		{
			return;
		}
	}
	
	public void start()
	{
		mMediaPlayer.start();
	}
	
	public void stop()
	{
		mMediaPlayer.stop();
	}
	public void pause()
	{
		mMediaPlayer.pause();
	}
	
	public boolean isPlaying()
	{
		return mMediaPlayer.isPlaying();
	}
	
	public int getDuration()
	{
		return mMediaPlayer.getDuration();
	}
	
	public int getPosition()
	{
		return mMediaPlayer.getCurrentPosition();
	}
	
	public long seek(long whereto)
	{
		mMediaPlayer.seekTo((int)whereto);
		return whereto;
	}

}
