package com.kakaopv.app;

import java.io.*;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ProfileFileRepository
{
	private static ProfileFileRepository fileRepository;	
	private Vector<ProfileImageData> datas;
	
	private final String TAG = "ProrfileRepo";
	
	public static ProfileFileRepository getInstance()
	{
		if(fileRepository == null)
			fileRepository = new ProfileFileRepository();
		return fileRepository;
	}
	
	private ProfileFileRepository()
	{
		datas = new Vector<ProfileImageData>();
	}
	
	protected final String [] dirFilters = {"5502042_1","5502007_2","emoticon_dir"};
	protected final String profilePath = "full_profile_image";
	
	private boolean filteringDir(String path)
	{
//		for(int i=0;i<dirFilters.length;i++)
//		{
//			if(path.contains(dirFilters[i]))
//			{
//				return false;
//			}
//		}
//		return true;
		return path.contains(profilePath);
	}
	
	public void searchFile(String path)
	{
		datas = new Vector<ProfileImageData>();
		findFile(path);
	}
	
	// tail recursive 로 이미지 파일 리스트 Vector 에 저장 Thread 로 수행
	private void findFile(String path)
	{
		File f = new File(path);
		if(f.isDirectory())
		{
			File [] flist = f.listFiles();
			for(int i=0; i < flist.length ;i++)
			{
				if(filteringDir(flist[i].getPath()))
					findFile(flist[i].getPath());
			}
		}
		else
		{
			if(f.canRead())
			{
				FileInputStream fileInputStream = null;
				BufferedInputStream bufferedInputStream = null;
				try
				{
					fileInputStream = new FileInputStream(f);
					bufferedInputStream = new BufferedInputStream(fileInputStream);
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 2;
					Bitmap bm = BitmapFactory.decodeStream(bufferedInputStream,null,options);
					if(bm != null)
					{	
						Bitmap bsm = resizeBitmap(bm, 100);
						datas.add(new ProfileImageData(path, bsm));
					}
					if(bufferedInputStream != null)
						bufferedInputStream.close();
					if(fileInputStream != null)
						fileInputStream.close();
				}
				catch (FileNotFoundException e)
				{
					Log.e(TAG,"FileNotFound",e);
				}
				catch (IOException e)
				{
					Log.e(TAG,"IOException",e);
				}
			}
		}
	}
	
	// Resize along to width.
	private Bitmap resizeBitmap(Bitmap bitmap, int limitWidth)
	{
		double width = bitmap.getWidth();	
		if(width == limitWidth)
			return bitmap;
		else
		{
			double resizeRatio = (limitWidth - width) / width;
			int changedWidth = (int) (width * ( 1.0 + resizeRatio));
			int changedHeight = (int) (bitmap.getHeight() * ( 1.0 + resizeRatio));
			return Bitmap.createScaledBitmap(bitmap, changedWidth, changedHeight, false);
		}
	}
	
	public Vector<ProfileImageData> getDatas()
	{
		return datas;
	}
	
	class ProfileImageData
	{
		private String filepath;
		private Bitmap bitmap;
		
		public ProfileImageData(String filepath, Bitmap bitmap)
		{
			this.filepath = filepath;
			this.bitmap = bitmap;
		}
		
		public String getFilepath()
		{
			return filepath;
		}
		public Bitmap getBitmap()
		{
			return bitmap;
		}
	}
}