package com.example.tomas.becomebasketballpro.DBHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tomas.becomebasketballpro.Listeners.ArticleListener;
import com.example.tomas.becomebasketballpro.Listeners.FitnessListener;
import com.example.tomas.becomebasketballpro.Model.ArticleModel;
import com.example.tomas.becomebasketballpro.Model.FitnessTrainingModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas on 06/09/2016.
 */
//public class FitnessDbHandler extends SQLiteOpenHelper implements FitnessListener {
//
//
//    private static final int DB_VERSION = 1;
//    private static final String DB_NAME = "FitnessDatabase.db";
//    private static final String TABLE_CATEGORY = "fitness_category";
//    private static final String KEY_IDS = "_ids";
//    private static final String KEY_CATEGORY = "_title";
//    private static final String KEY_CATTHUMB = "catThumb";
//
//    private SQLiteDatabase db;
//
//    String CREATE_TABLE_CATEGORY = "CREATE TABLE "+TABLE_CATEGORY+" ("+KEY_IDS+" INTEGER PRIMARY KEY,"+KEY_CATEGORY+" TEXT,"+KEY_CATTHUMB+" TEXT)";
//    String DROP_TABLE_CATEGORY = "DROP TABLE IF EXISTS "+TABLE_CATEGORY;
//
//    public FitnessDbHandler(Context context) {
//        super(context, DB_NAME, null, DB_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(CREATE_TABLE_CATEGORY);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL(DROP_TABLE_CATEGORY);
//        onCreate(db);
//    }
//
//    @Override
//    public List<FitnessTrainingModel> getAllCategories() {
//        List<FitnessTrainingModel> fitnessTrainingModelList = null;
//        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery(selectQuery, null);
//
//        if (c.moveToFirst()) {
//            do {
//                FitnessTrainingModel values = new FitnessTrainingModel();
//                values.setIds(c.getString((c.getColumnIndex(KEY_IDS))));
//                values.setCategory((c.getString(c.getColumnIndex(KEY_CATEGORY))));
//                values.setCatThumb(c.getString(c.getColumnIndex(KEY_CATTHUMB)));
//                fitnessTrainingModelList.add(values);
//            } while (c.moveToNext());
//        }
//        c.close();
//        return fitnessTrainingModelList;
//    }
//    public void deleteCategoryTable() {
//        if (db == null || !db.isOpen())
//            db = getWritableDatabase();
//        db.delete(TABLE_CATEGORY, null, null);;
//    }
//
//    @Override
//    public void addCategory(FitnessTrainingModel fitnessTrainingModel) {
//        List<FitnessTrainingModel> fitnessTrainingModelList = null;
//        SQLiteDatabase db = this.getWritableDatabase();
//        try{
//            ContentValues values = new ContentValues();
//            values.put(KEY_IDS, fitnessTrainingModel.getIds());
//            values.put(KEY_CATEGORY, fitnessTrainingModel.getCategory());
//            values.put(KEY_CATTHUMB, fitnessTrainingModel.getCatThumb());
//            db.insert(TABLE_CATEGORY, null, values);
//            db.close();
//        }catch (Exception e){
//            Log.e("problem",e+"");
//        }
//    }
//
//}
