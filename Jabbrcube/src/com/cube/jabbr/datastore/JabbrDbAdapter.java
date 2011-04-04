package com.cube.jabbr.datastore;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;


public class JabbrDbAdapter {

	private sqlitehelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "JabbrDbAdapter";
    
	public void newLoc(int uid, int loc){
		String sql = "SELECT * FROM tbl_preferences WHERE user_id='"+uid+"';";
		Cursor cursor = mDb.rawQuery(sql, null);
		if (cursor.getCount() > 0){
			String updateSql = "UPDATE tbl_preferences SET loc_id='"+loc+"' WHERE user_id ='"+uid+"';";
			mDb.rawQuery(updateSql, null);
		}
    	else{
    		String updateSql = "INSERT INTO tbl_preferences (loc_id, user_id, context_id) VALUES ('" +loc+ "', '" + uid + "', '0');";
			mDb.rawQuery(updateSql, null);
    	}
    		
	}
	
	public void newContext(int uid, int context){
		String sql = "SELECT * FROM tbl_preferences WHERE user_id='"+uid+"';";
		Cursor cursor = mDb.rawQuery(sql, null);
		if (cursor.getCount() > 0){
			String updateSql = "UPDATE tbl_preferences SET context_id='"+context+"' WHERE user_id ='"+uid+"';";
			mDb.rawQuery(updateSql, null);
		}
    	else{
    		String updateSql = "INSERT INTO tbl_preferences (loc_id, user_id, context_id) VALUES ('0', '" + uid + "', '"+context+"');";
			mDb.rawQuery(updateSql, null);
    	}
	}
	
	private static class sqlitehelper extends SQLiteOpenHelper {
	
		public sqlitehelper(Context context) {
			super(context, "androidPersistance", null, 1);
	
		}
	
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE IF NOT EXISTS tbl_preferences ("
					+ BaseColumns._ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, loc_id INTEGER, context_id INTEGER)");
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Steps to upgrade the database for the new version ...
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
		}
		
		
	}
}
