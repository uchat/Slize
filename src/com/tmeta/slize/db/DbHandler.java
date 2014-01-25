/*
 * Copyright (C) 2014 tmeta.com
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


package com.tmeta.slize.db;

import com.tmeta.slize.Config;
import com.tmeta.slize.booster.IBoosterExecutable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHandler extends SQLiteOpenHelper {
	
	
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "slizedb";
	
	private static final String DB_TABLE_AWARD = "award";
	private static final String DB_TABLE_CONFIG_NAME = "programstat";
	private static final String DB_TABLE_SCORE_20M = "score20m";
	private static final String DB_TABLE_SCORE_60S = "score60s";
	private static final String DB_TABLE_SCORE_120S = "score120s";
	private static final String DB_TABLE_BOOSTER = "booster";

	private static final String DB_COL_ID = "id";
	
	/* booster */
	private static final String DB_COL_BOOSTER_ID  = "boosterid";
	private static final String DB_COL_BOOSTER_NUM = "boosternum";
	
	
	/* award */
	private static final String DB_COL_AWARD_ID = "awardid";
	
	/* programstat */
	private static final String DB_COL_TOTALPOINT = "totalpoint";	
	private static final String DB_COL_NUMPLAYED = "numplayed";
	private static final String DB_COL_NUMOPENED = "numopened";
	private static final String DB_COL_NUMPLAYED_20M = "numplayed20m";
	private static final String DB_COL_NUMPLAYED_60S = "numplayed60s";
	private static final String DB_COL_NUMPLAYED_120S = "numplayed120s";
	private static final String DB_COL_SOUND = "sound";
	private static final String DB_COL_VIBRATE = "vibrate";
	private static final String DB_COL_RATED = "rated";
	private static final String DB_COL_NUMINVITED = "numinvited";
	
	/* score20m, score60s, score120s */
	private static final String DB_COL_SCORE = "score";
	private static final String DB_COL_DATE = "datetime";
	
	
	public String _devId = null;
	private int vibrate_state = -1;

	
	public DbHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String tbBooster = "CREATE TABLE " + DB_TABLE_BOOSTER + "("
				+ DB_COL_ID       + " INTEGER PRIMARY KEY," 
				+ DB_COL_BOOSTER_ID + " INTEGER,"
				+ DB_COL_BOOSTER_NUM + " INTEGER" + ")";
		db.execSQL(tbBooster);
		
		String tbAward = "CREATE TABLE " + DB_TABLE_AWARD + "("
				+ DB_COL_ID       + " INTEGER PRIMARY KEY," 
				+ DB_COL_AWARD_ID + " INTEGER" + ")";
		db.execSQL(tbAward);

		String tbConfig = "CREATE TABLE " + DB_TABLE_CONFIG_NAME + "("
				+ DB_COL_ID + " INTEGER PRIMARY KEY," 
				+ DB_COL_TOTALPOINT + " INTEGER,"
				+ DB_COL_NUMPLAYED + " INTEGER," 
				+ DB_COL_NUMOPENED + " INTEGER," 
				+ DB_COL_NUMPLAYED_20M + " INTEGER," 
				+ DB_COL_NUMPLAYED_60S + " INTEGER," 
				+ DB_COL_NUMPLAYED_120S + " INTEGER," 
				+ DB_COL_SOUND + " INTEGER," 
				+ DB_COL_RATED + " INTEGER,"
				+ DB_COL_NUMINVITED + " INTEGER,"
				+ DB_COL_VIBRATE + " INTEGER" + ")";
		db.execSQL(tbConfig);
		
		
		/* http://androidcookbook.com/Recipe.seam?recipeId=413 */
		String tbScore20m = "CREATE TABLE " + DB_TABLE_SCORE_20M + "("
				+ DB_COL_ID +    " INTEGER PRIMARY KEY," 
				+ DB_COL_SCORE + " INTEGER,"
				+ DB_COL_DATE + "  TIMESTAMP NOT NULL DEFAULT current_timestamp" + ")";
		db.execSQL(tbScore20m);
		
		String tbScore60s = "CREATE TABLE " + DB_TABLE_SCORE_60S + "("
				+ DB_COL_ID +    " INTEGER PRIMARY KEY," 
				+ DB_COL_SCORE + " INTEGER,"
				+ DB_COL_DATE + "  TIMESTAMP NOT NULL DEFAULT current_timestamp" + ")";
		db.execSQL(tbScore60s);		
		
		String tbScore120s = "CREATE TABLE " + DB_TABLE_SCORE_120S + "("
				+ DB_COL_ID +    " INTEGER PRIMARY KEY," 
				+ DB_COL_SCORE + " INTEGER,"
				+ DB_COL_DATE + "  TIMESTAMP NOT NULL DEFAULT current_timestamp" + ")";
		db.execSQL(tbScore120s);		
		
		
		this.mInitDbRow(db);
	}
		

	private void mInitDbRow(SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put(DB_COL_SOUND, 1);
		values.put(DB_COL_TOTALPOINT, 0);
		values.put(DB_COL_RATED, 0);
		values.put(DB_COL_VIBRATE, 1);
		values.put(DB_COL_NUMPLAYED, 0);
		values.put(DB_COL_NUMOPENED, 0);
		values.put(DB_COL_NUMPLAYED_20M, 0);
		values.put(DB_COL_NUMPLAYED_60S, 0);
		values.put(DB_COL_NUMPLAYED_120S, 0);
		values.put(DB_COL_NUMINVITED, 0);
		db.insert(DB_TABLE_CONFIG_NAME, null, values);
	
		for (IBoosterExecutable.BoosterTyp typ : IBoosterExecutable.BoosterTyp.values()) {
			int i = typ.ordinal();
			values = new ContentValues();
			values.put(DB_COL_BOOSTER_ID, i);
			values.put(DB_COL_BOOSTER_NUM, 0);
			db.insert(DB_TABLE_BOOSTER, null, values);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		;
	}
	
	
	public int getNumOpened() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(DB_TABLE_CONFIG_NAME, 
				new String[] { DB_COL_NUMOPENED }, null, null, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			return cursor.getInt(0);
		}
		return 0;
	}
	
	public void recordGameOpen() {
		int c = getNumOpened() + 1;
		try {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(DB_COL_NUMOPENED, c);

			db.update(DB_TABLE_CONFIG_NAME, values, DB_COL_ID + "=?",
					new String[] { "1" });
		} catch (Exception e) {
			;
		}
	}
		
	public int[] getNumPlayed() {
		SQLiteDatabase db = this.getReadableDatabase();
		int[] ret = new int[4];
		Cursor cursor = db.query(DB_TABLE_CONFIG_NAME, 
				new String[] { DB_COL_NUMPLAYED, DB_COL_NUMPLAYED_20M, DB_COL_NUMPLAYED_60S, DB_COL_NUMPLAYED_120S }, 
				null,
				null, 
				null, 
				null, 
				null,
				null);
		if (cursor != null) {
			if (cursor.getCount() == 0) {
				cursor.close();
				return null;
			}
			cursor.moveToFirst();
			ret[0] = cursor.getInt(0); // all num played
			ret[1] = cursor.getInt(1); // 20m num played
			ret[2] = cursor.getInt(2); // 60s num played
			ret[3] = cursor.getInt(3); // 120s num played
			cursor.close();
			return ret;
		}
		return null;
	}
	
	public void issueAward(int id) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			String selectQuery = "SELECT * FROM " + DB_TABLE_AWARD + " WHERE " + DB_COL_AWARD_ID + "=?";
			Cursor cursor = db.rawQuery(selectQuery, new String[]{id+""});
			if (cursor != null) {
				if (cursor.getCount() == 0) {
					cursor.close();
					ContentValues values = new ContentValues();
					values.put(DB_COL_AWARD_ID, id);
					db.insert(DB_TABLE_AWARD, null, values);
				} else {
					cursor.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public int[] getIssuedAwards() {
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM " + DB_TABLE_AWARD;
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			int size = cursor.getCount();
			if (size > 0) {
				int[] ret = new int[size];
				cursor.moveToFirst();
				int i = 0;
				while (cursor.isAfterLast() == false) {
					ret[i] = cursor.getInt(1);
					cursor.moveToNext();
					i++;
				}
				return ret;
			}
		}
		return null;
	}
	
	
	public int getBoosterNum(IBoosterExecutable.BoosterTyp typ) {
		int i = typ.ordinal();
		int ret = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT " + DB_COL_BOOSTER_NUM + " FROM " + DB_TABLE_BOOSTER + " WHERE " + DB_COL_BOOSTER_ID + "=?";
		Cursor cursor = db.rawQuery(selectQuery, new String[]{i+""});
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				ret = Integer.parseInt(cursor.getString(0));
			}
			cursor.close();
		}
		return ret;
	}
	
	
	public void updateBooster(IBoosterExecutable.BoosterTyp typ, int v) {
		int i = typ.ordinal();
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(DB_COL_BOOSTER_NUM, v);
			db.update(DB_TABLE_BOOSTER, values, DB_COL_BOOSTER_ID + "=?",
					new String[] { i+"" });
		} catch (Exception e) {
			;
		}
	}
	
	
	public void recordPlay(int all, int v20m, int v60s, int v120s) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(DB_COL_NUMPLAYED, all);
			values.put(DB_COL_NUMPLAYED_20M, v20m);
			values.put(DB_COL_NUMPLAYED_60S, v60s);
			values.put(DB_COL_NUMPLAYED_120S, v120s);
			db.update(DB_TABLE_CONFIG_NAME, values, DB_COL_ID + "=?",
					new String[] { "1" });
		} catch (Exception e) {
			;
		}
	}
	
	public void recordScore(int mode, int score) {
		String dbname = (mode == Config.MODE_20M) ? DB_TABLE_SCORE_20M 
												  : (mode == Config.MODE_60S ? DB_TABLE_SCORE_60S : DB_TABLE_SCORE_120S);
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DB_COL_SCORE, score);
		db.insert(dbname, null, values);
	}
	
	public int getTotalPoint() {
		int ret = 0;
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			String selectQuery = "SELECT  \"" + DB_COL_TOTALPOINT + "\" FROM "
					+ DB_TABLE_CONFIG_NAME;
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor == null) {
				return 0;
			}
			if (cursor.moveToFirst()) {
				ret = Integer.parseInt(cursor.getString(0));
			}
			cursor.close();
		} catch (Exception e) {
		}
		return ret;
	}
	
	public void updateTotalPoint(int point) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(DB_COL_TOTALPOINT, point);
			db.update(DB_TABLE_CONFIG_NAME, values, DB_COL_ID + "=?",
					new String[] { "1" });
		} catch (Exception e) {
			;
		}
	}
	
	
	public int getUserWeekTopScore(int mode, int[] scores) {		
		String dbname = (mode == Config.MODE_20M) ? DB_TABLE_SCORE_20M 
				  : (mode == Config.MODE_60S ? DB_TABLE_SCORE_60S : DB_TABLE_SCORE_120S);
		int size = scores.length;
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + dbname + 
				" WHERE date(" + DB_COL_DATE + ") >= DATE('now', '-7 days') ORDER BY " + DB_COL_SCORE + " DESC LIMIT " + size;
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor == null || cursor.getCount() <= 0)
			return 0;
		cursor.moveToFirst();
		int count = cursor.getCount();
		int i = 0;
		while (! cursor.isAfterLast()) {
			scores[i] = cursor.getInt(1);
			cursor.moveToNext();
			i++;
		}
		cursor.close();
		return count;
	}
	
	public int getUserTopScore(int mode, int[] scores) {
		String dbname = (mode == Config.MODE_20M) ? DB_TABLE_SCORE_20M 
				  : (mode == Config.MODE_60S ? DB_TABLE_SCORE_60S : DB_TABLE_SCORE_120S);
		int size = scores.length;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(dbname, new String[] {DB_COL_SCORE}, null, null, null, null, DB_COL_SCORE + " DESC", size + "");
		if (cursor == null || cursor.getCount() <= 0)
			return 0;
		cursor.moveToFirst();
		int count = cursor.getCount();
		int i = 0;
		while (! cursor.isAfterLast()) {
			scores[i] = cursor.getInt(0);
			cursor.moveToNext();
			i++;
		}
		cursor.close();
		return count;
	}
	
		
	public boolean isVibrationEnable() {
		
		if (vibrate_state != -1)
			return vibrate_state == 1;

		boolean ret = true;
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			String selectQuery = "SELECT  \"" + DB_COL_VIBRATE + "\" FROM "
					+ DB_TABLE_CONFIG_NAME;
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor == null) {
				return true;
			}
			if (cursor.moveToFirst()) {
				int v = Integer.parseInt(cursor.getString(0));
				ret = v == 1;
			}
			cursor.close();		
			vibrate_state = ret ? 1 : 0;
		} catch (Exception e) {
			ret = true;
		}
		return ret;
		
	}
	
	public int setVibrationEnable(int flag) {
		int ret = -1;
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(DB_COL_VIBRATE, flag);
			vibrate_state = flag;
			ret = db.update(DB_TABLE_CONFIG_NAME, values, DB_COL_ID + "=?",
					new String[] { "1" });
		} catch (Exception e) {
			;
		}
		return ret;
		
	}
	
	public boolean isSoundEnable() {
		boolean ret = true;
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			String selectQuery = "SELECT  \"" + DB_COL_SOUND + "\" FROM "
					+ DB_TABLE_CONFIG_NAME;
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor == null) {
				return true;
			}
			if (cursor.moveToFirst()) {
				int v = Integer.parseInt(cursor.getString(0));
				ret = v == 1;
			}
			cursor.close();
		} catch (Exception e) {
			ret = true;
		}
		
		return ret;
	}
	
	public int setSoundEnable(int flag) {
		int ret = -1;
		try {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(DB_COL_SOUND, flag);

			ret = db.update(DB_TABLE_CONFIG_NAME, values, DB_COL_ID + "=?",
					new String[] { "1" });
		} catch (Exception e) {
			;
		}
		return ret;

	}	
}
