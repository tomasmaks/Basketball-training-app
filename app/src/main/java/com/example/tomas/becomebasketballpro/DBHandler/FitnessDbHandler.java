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
public class FitnessDbHandler extends SQLiteOpenHelper implements FitnessListener {


    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "FitnessDatabase.db";

    private static final String TABLE_CATEGORY = "fitness_category";
    private static final String TABLE_EXERCISE = "fitness_exercise";
    private static final String TABLE_EXERCISE_CATEGORY = "fitness_exercise_category";

    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "_name";
    private static final String KEY_BODY = "_body";
    private static final String KEY_THUMB = "_thumb";
    private static final String KEY_DESCRIPTION = "_description";
    private static final String KEY_VIDEO = "_video";
    private static final String CAT_ID = "_cat_id";

    private static final String KEY_IDS = "_ids";
    private static final String KEY_CATEGORY = "_title";
    private static final String KEY_CATTHUMB = "catThumb";

    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_EXERCISE_ID = "exercise_id";

    private SQLiteDatabase db;
    String CREATE_TABLE_EXERCISE = "CREATE TABLE "+TABLE_EXERCISE+" ("+KEY_ID+" INTEGER PRIMARY KEY,"+KEY_NAME+" TEXT,"+KEY_DESCRIPTION+" TEXT,"+KEY_BODY+" TEXT,"+KEY_THUMB+" TEXT,"+KEY_VIDEO+" TEXT,"+CAT_ID+" TEXT)";
    String DROP_TABLE_EXERCISE = "DROP TABLE IF EXISTS "+TABLE_EXERCISE;

    String CREATE_TABLE_CATEGORY = "CREATE TABLE "+TABLE_CATEGORY+" ("+KEY_IDS+" INTEGER PRIMARY KEY,"+KEY_CATEGORY+" TEXT,"+KEY_CATTHUMB+" TEXT)";
    String DROP_TABLE_CATEGORY = "DROP TABLE IF EXISTS "+TABLE_CATEGORY;

    public FitnessDbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EXERCISE);
        db.execSQL(CREATE_TABLE_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_EXERCISE);
        db.execSQL(DROP_TABLE_CATEGORY);
        onCreate(db);
    }

    /*
  * getting all todos
  * */
    @Override
    public List<FitnessTrainingModel> getAllCategories() {
        List<FitnessTrainingModel> fitnessTrainingModelList = null;
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;
        fitnessTrainingModelList = new ArrayList<FitnessTrainingModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                FitnessTrainingModel values = new FitnessTrainingModel();
                values.setIds(c.getString((c.getColumnIndex(KEY_IDS))));
                values.setCategory((c.getString(c.getColumnIndex(KEY_CATEGORY))));
                values.setCatThumb(c.getString(c.getColumnIndex(KEY_CATTHUMB)));

                // adding to todo list
                fitnessTrainingModelList.add(values);
            } while (c.moveToNext());
        }
        c.close();
        return fitnessTrainingModelList;
    }
    public void deleteCategoryTable() {
        if (db == null || !db.isOpen())
            db = getWritableDatabase();
        db.delete(TABLE_CATEGORY, null, null);;
    }

    @Override
    public void addCategory(FitnessTrainingModel fitnessTrainingModel) {
        List<FitnessTrainingModel> fitnessTrainingModelList = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put(KEY_IDS, fitnessTrainingModel.getIds());
            values.put(KEY_CATEGORY, fitnessTrainingModel.getCategory());
            values.put(KEY_CATTHUMB, fitnessTrainingModel.getCatThumb());
            db.insert(TABLE_CATEGORY, null, values);
            db.close();
        }catch (Exception e){
            Log.e("problem",e+"");
        }
    }


    /*
   * getting all todos
   * */
//    @Override
//    public List<FitnessTrainingModel> getAllExercisesById() {
//        List<FitnessTrainingModel> fitnessTrainingModelList = null;
//        fitnessTrainingModelList = new ArrayList<FitnessTrainingModel>();
//
//        //String selectQuery = "SELECT * FROM " + TABLE_EXERCISE;
//        //String selectQuery = "SELECT * FROM " + TABLE_EXERCISE + " te, " + TABLE_CATEGORY + " tc WHERE tc." + KEY_IDS + " = " + id;
//        String selectQuery = "SELECT * FROM " + TABLE_EXERCISE + " te, " + TABLE_CATEGORY + " tc WHERE tc." + KEY_IDS + " = " + "te." + CAT_ID;
//
//        //String selectQuery = "SELECT * FROM " + TABLE_EXERCISE + " te INNER JOIN " + TABLE_CATEGORY + " tc ON te." + CAT_ID + " = tc." + KEY_IDS + " WHERE tc." + KEY_IDS + " = " + category_id;
//        //db.rawQuery(query, new String[]{String.valueOf(category_id)})
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery(selectQuery, null);
//        //new String[]{String.valueOf(category_id)}
//        // looping through all rows and adding to list
//        if (c.moveToFirst()) {
//            do {
//                FitnessTrainingModel values = new FitnessTrainingModel();
//                values.setId(c.getString((c.getColumnIndex(KEY_ID))));
//                values.setName((c.getString(c.getColumnIndex(KEY_NAME))));
//                values.setBody(c.getString(c.getColumnIndex(KEY_BODY)));
//                values.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
//                values.setThumb(c.getString(c.getColumnIndex(KEY_THUMB)));
//                values.setVideoURI(c.getString(c.getColumnIndex(KEY_VIDEO)));
//                values.setCatId(c.getString(c.getColumnIndex(CAT_ID)));
//
//                // adding to todo list
//                fitnessTrainingModelList.add(values);
//            } while (c.moveToNext());
//        }
//        c.close();
//        return fitnessTrainingModelList;
//    }
//
//
//
//
//    public void deleteExerciseTable() {
//        if (db == null || !db.isOpen())
//            db = getWritableDatabase();
//        db.delete(TABLE_EXERCISE, null, null);;
//    }
//
//    @Override
//    public void addExercise(FitnessTrainingModel fitnessTrainingModel) {
//        List<FitnessTrainingModel> fitnessTrainingModelList = null;
//        SQLiteDatabase db = this.getWritableDatabase();
//        try{
//            ContentValues values = new ContentValues();
//            values.put(KEY_ID, fitnessTrainingModel.getId());
//            values.put(KEY_NAME, fitnessTrainingModel.getName());
//            values.put(KEY_BODY, fitnessTrainingModel.getBody());
//            values.put(KEY_DESCRIPTION, fitnessTrainingModel.getDescription());
//            values.put(KEY_THUMB, fitnessTrainingModel.getThumb());
//            values.put(KEY_VIDEO, fitnessTrainingModel.getVideoURI());
//            values.put(CAT_ID, fitnessTrainingModel.getCatId());
//            db.insert(TABLE_EXERCISE, null, values);
//
//
//            db.close();
//        }catch (Exception e){
//            Log.e("problem",e+"");
//        }
//    }
}
