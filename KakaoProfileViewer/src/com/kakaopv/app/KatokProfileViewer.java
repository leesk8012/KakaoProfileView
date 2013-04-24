/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kakaopv.app;

import com.kakaopv.app.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * A grid that displays a set of framed photos.
 * 
 */
public class KatokProfileViewer extends Activity
{
	private final String findPath = "//sdcard//Android//Data//com.kakao.talk//cache";
//	private final String findPath = "//sdcard//external_sd//Pictures/Instagram//";
	private String TAG = "KatokProfileViewer";
	
	private ImageAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_2);

		Log.w(TAG,"Find " + findPath);
		ProfileFileRepository.getInstance().searchFile(findPath);
		Log.w(TAG,"Done " + findPath);
		GridView g = (GridView) findViewById(R.id.myGrid);
		adapter = new ImageAdapter(getApplicationContext());
		g.setAdapter(adapter);
		g.setOnItemClickListener(itemClickListener);
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
		{
			// TODO Image Open
			Intent intent = new Intent(getApplicationContext(), DetailView.class);
			intent.putExtra("ImageIndex", position);
			startActivity(intent);
		}
	};
	
	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
	}
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	// Adapter
	public class ImageAdapter extends BaseAdapter
	{
		public ImageAdapter(Context c)
		{
			mContext = c;
		}
		// 현재 Item 의 개수.
		public int getCount()
		{
			Log.e(TAG,"getCount()");
			return ProfileFileRepository.getInstance().getDatas().size();
		}
		// Item(Bitmap) 가져옴.
		public Object getItem(int position)
		{
			Log.e(TAG,"getItem() "+position);
			return ProfileFileRepository.getInstance().getDatas().get(position).getBitmap();
		}
		// Item ID 는 위치
		public long getItemId(int position)
		{
			Log.e(TAG,"getItemId() "+position);
			return position;
		}
		// 각 위치에 ImageView 불러옴
		public View getView(int position, View convertView, ViewGroup parent)
		{
//			Log.w(TAG,"GetView");
			ImageView imageView;
			if (convertView == null)
			{
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
				imageView.setAdjustViewBounds(false);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			}
			else
			{
				imageView = (ImageView) convertView;
			}			
			Bitmap bm = ProfileFileRepository.getInstance().getDatas().elementAt(position).getBitmap();
			if(bm != null)
			{
				imageView.setImageBitmap(bm);
			}
			return imageView;
		}
		private Context mContext;
	}

}
