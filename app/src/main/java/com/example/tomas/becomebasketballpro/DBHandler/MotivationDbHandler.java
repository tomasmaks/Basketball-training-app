package com.example.tomas.becomebasketballpro.DBHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tomas.becomebasketballpro.Listeners.ArticleListener;
import com.example.tomas.becomebasketballpro.Listeners.MotivationListener;
import com.example.tomas.becomebasketballpro.Model.ArticleModel;
import com.example.tomas.becomebasketballpro.Model.MotivationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas on 05/09/2016.
 */
public class MotivationDbHandler extends SQLiteOpenHelper implements MotivationListener {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "MotivationDatabase.db";
    private static final String TABLE_NAME = "motivation_table";
    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "_title";
    private static final String KEY_BODY = "_body";
    private static final String KEY_THUMB = "_thumb";
    private static final String KEY_PHOTO = "_photo";
    private static final String KEY_PUBLISHED_DATE = "_published_date";
    private SQLiteDatabase db;
    String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+KEY_ID+" INTEGER PRIMARY KEY,"+KEY_THUMB+" TEXT,"+KEY_PHOTO+" TEXT)";
    String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;

    public MotivationDbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void deleteTable() {
        if (db == null || !db.isOpen())
            db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);;
    }

    @Override
    public void addMotivation(MotivationModel motivationModel) {
        List<MotivationModel> motivationModelList = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put(KEY_THUMB, motivationModel.getThumbnail());
            values.put(KEY_PHOTO, motivationModel.getImage());
            db.insert(TABLE_NAME, null, values);
            db.close();
        }catch (Exception e){
            Log.e("problem",e+"");
        }
    }


    @Override
    public List<MotivationModel> getAllMotivation() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<MotivationModel> motivationModelList = null;

        try{
            motivationModelList = new ArrayList<MotivationModel>();
            String QUERY = "SELECT * FROM "+TABLE_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            if(!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    MotivationModel motivationModel = new MotivationModel();
                    motivationModel.setId(cursor.getInt(0));
                    motivationModel.setThumbnail(cursor.getString(1));
                    motivationModel.setImage(cursor.getString(2));
                    motivationModelList.add(motivationModel);

                }
                cursor.close();

            }

        }catch (Exception e){
            Log.e("error",e+"");
        }
        return motivationModelList;
    }

    @Override
    public int getMotivationCount() {
        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String QUERY = "SELECT * FROM "+TABLE_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            cursor.close();
            return num;
        }catch (Exception e){
            Log.e("error",e+"");
        }
        return 0;
    }

    public MotivationModel getMotivation(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] {KEY_ID}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        MotivationModel motivationModel = null;
        if ((cursor != null) && cursor.moveToFirst()) {

            motivationModel = new MotivationModel(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
            // return contact
        }
        return motivationModel;
    }
}
