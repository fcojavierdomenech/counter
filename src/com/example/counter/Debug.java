package com.example.counter;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

public class Debug {
	private static boolean enabled;
	private static final String LOG_TAG = "debugging";
	
	public Debug ()
	{
	}

	public static void enable()
	{
		enabled = true;
	}

	public static void disable()
	{
		enabled = false;
	}

	public static void print(String str)
	{
		if(enabled)
		{
			System.out.println(str);
		}
	}

	public static void print(Cursor cursor)
	{
		if(enabled)
		{
			System.out.println(DatabaseUtils.dumpCursorToString(cursor));
		}
	}

	public static void log(String str)
	{
		if(enabled)
		{
			Log.d(LOG_TAG,str);
		}
	}

	public static void log(Cursor cursor)
	{
		if(enabled)
		{
			Log.d(LOG_TAG,DatabaseUtils.dumpCursorToString(cursor));
		}
	}
}
