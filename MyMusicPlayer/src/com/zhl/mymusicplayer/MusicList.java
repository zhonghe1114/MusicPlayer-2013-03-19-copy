package com.zhl.mymusicplayer;

import com.zhl.mymusicplayer.MusicInfoControler;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 */

/**
 * @author dehoo-ZhongHeliang 2013-3-19下午20:43:14
 * @version jdk 1.6.0_03; sdk 4.2.0
 */
public class MusicList extends ListActivity
{
	private MusicPlayerService mMusicPlayerService = null;
	private MusicInfoControler mMusicInfoControler = null;
	private Cursor mCursor = null;
	
	private TextView mTextView = null;
	private Button mPlayPauseButton = null;
	private Button mStopButton = null;
	private ServiceConnection mPlaybackConnection = new ServiceConnection()
	{
		
		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			mMusicPlayerService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			mMusicPlayerService = ((MusicPlayerService.LocalBinder)service).getService();
		}
	};
	
	// 处理广播
		protected BroadcastReceiver mPlayerEvtReceiver = new BroadcastReceiver()
		{

			@Override
			public void onReceive(Context context, Intent intent)
			{
				String action = intent.getAction();
				if (action.equals(MusicPlayerService.PLAYER_PREPARE_END))
				{
					mTextView.setVisibility(View.INVISIBLE);
					mPlayPauseButton.setVisibility(View.VISIBLE);
					mStopButton.setVisibility(View.VISIBLE);
					mPlayPauseButton.setText(R.string.pause);
				}
				else if(action.equals(MusicPlayerService.PLAYER_COMPLETED))
				{
					mPlayPauseButton.setText(R.string.play);
				}
			}
			
		};
		
		
		private OnClickListener onClickListener = new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				switch (v.getId())
				{
				case R.id.play_pause_btn:
					if (mMusicPlayerService != null && mMusicPlayerService.isPlaying())
					{
						mMusicPlayerService.pause();
						mPlayPauseButton.setText(R.string.pause);
					}else if (mMusicPlayerService != null)
					{
						mMusicPlayerService.start();
						mPlayPauseButton.setText(R.string.play);
					}
					break;
				case R.id.stop_btn:
					if (mMusicPlayerService != null)
					{
						mTextView.setVisibility(View.VISIBLE);
						mPlayPauseButton.setVisibility(View.INVISIBLE);
						mStopButton.setVisibility(View.INVISIBLE);
						mMusicPlayerService.stop();
					}
					break;

				default:
					break;
				}
			}
		};
		
		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.main);
			
			MusicPlayerApp musicPlayerApp = (MusicPlayerApp)getApplication();
			mMusicInfoControler = (musicPlayerApp).getMusicInfoControler();
			
			// 绑定服务
			startService(new Intent(this, MusicPlayerService.class));
			bindService(new Intent(this, MusicPlayerService.class), mPlaybackConnection, Context.BIND_AUTO_CREATE);
			
			mTextView = (TextView)findViewById(R.id.show_text);
			mPlayPauseButton = (Button)findViewById(R.id.play_pause_btn);
			mStopButton = (Button)findViewById(R.id.stop_btn);
			
			mPlayPauseButton.setOnClickListener(onClickListener);
			mStopButton.setOnClickListener(onClickListener);
			
			IntentFilter filter = new IntentFilter();
			filter.addAction(MusicPlayerService.PLAYER_PREPARE_END);
			filter.addAction(MusicPlayerService.PLAYER_COMPLETED);
			registerReceiver(mPlayerEvtReceiver, filter);
		}
		
		protected void onResume()
		{
			super.onResume();
			
			mCursor = mMusicInfoControler.getAllSongs();
			
			ListAdapter adapter = new MusicListAdapter(this, android.R.layout.simple_expandable_list_item_2, mCursor, new String[]{}, new int[]{});
			setListAdapter(adapter);
		}
		
		
		
		@Override
		protected void onListItemClick(ListView l, View v, int position, long id)
		{
			super.onListItemClick(l, v, position, id);
			if (mCursor == null || mCursor.getCount() == 0)
			{
				return;
			}
			mCursor.moveToPosition(position);
			String url = mCursor.getString(mCursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
			mMusicPlayerService.setDataSource(url);
			mMusicPlayerService.start();
		}

		@Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        menu.add(Menu.NONE, Menu.FIRST+1, 1, "退出");
	        menu.add(Menu.NONE, Menu.FIRST+2, 2, "关于钟哥");
	        return true;
	    }

		@Override
		public boolean onOptionsItemSelected(MenuItem item)
		{
			switch (item.getItemId())
			{
			case Menu.FIRST+1:
				unregisterReceiver(mPlayerEvtReceiver);
				unbindService(mPlaybackConnection);
				if (mMusicPlayerService != null)
				{
					mMusicPlayerService.stop();
				}
				SystemClock.sleep(500);
				MusicList.this.finish();
				break;
			case Menu.FIRST+2:
				Toast.makeText(this, "钟哥只是个传说···",Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
			return super.onOptionsItemSelected(item);
		}

		
}

class MusicListAdapter extends SimpleCursorAdapter
{
	public MusicListAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to)
	{
		super(context, layout, c, from, to);
	}
	public void bindView(View view, Context context, Cursor cursor)
	{
		// TODO Auto-generated method stub
		super.bindView(view, context, cursor);
		
		TextView titleView = (TextView)view.findViewById(android.R.id.text1);
		TextView artistView = (TextView)view.findViewById(android.R.id.text2);
		
		titleView.setText(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
		artistView.setText(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
	}
	
	public static String makeTimeString(long milliSecs)
	{
		StringBuffer sb = new StringBuffer();
		long m = milliSecs/(60*1000);
		sb.append(m<10?"0"+m:m);
		sb.append(":");
		long s = (milliSecs%(60 * 1000)) / 1000;
		sb.append(s < 10 ? "0"+s : s);
		
		return sb.toString();
	}
}

