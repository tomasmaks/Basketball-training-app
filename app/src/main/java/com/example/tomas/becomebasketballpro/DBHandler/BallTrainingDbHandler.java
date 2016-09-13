package com.example.tomas.becomebasketballpro.DBHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tomas.becomebasketballpro.Listeners.BallTrainingListener;
import com.example.tomas.becomebasketballpro.Listeners.FitnessListener;
import com.example.tomas.becomebasketballpro.Model.BallTrainingModel;
import com.example.tomas.becomebasketballpro.Model.FitnessTrainingModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas on 13/09/2016.
 */
public class BallTrainingDbHandler extends SQLiteOpenHelper implements BallTrainingListener {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "BallTrainingDatabase.db";

    private static final String TABLE_CATEGORY = "ballTraining_category";

    private static final String KEY_IDS = "_ids";
    private static final String KEY_CATEGORY = "_category";
    private static final String KEY_CATTHUMB = "_catThumb";

    private SQLiteDatabase db;

    String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + " ("+KEY_IDS+" INTEGER PRIMARY KEY,"+KEY_CATEGORY+" TEXT,"+KEY_CATTHUMB+" TEXT)";
    String DROP_TABLE_CATEGORY = "DROP TABLE IF EXISTS " + TABLE_CATEGORY;

    public BallTrainingDbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_CATEGORY);
        onCreate(db);
    }
    @Override
    public List<BallTrainingModel> getAllCategories() {
        List<BallTrainingModel> ballTrainingModelList = null;
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;
        ballTrainingModelList = new ArrayList<BallTrainingModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                BallTrainingModel values = new BallTrainingModel();
                values.setIds(c.getString((c.getColumnIndex(KEY_IDS))));
                values.setCategory((c.getString(c.getColumnIndex(KEY_CATEGORY))));
                values.setCatThumb(c.getString(c.getColumnIndex(KEY_CATTHUMB)));

                // adding to todo list
                ballTrainingModelList.add(values);
            } while (c.moveToNext());
        }
        c.close();
        return ballTrainingModelList;
    }
    public void deleteCategoryTable() {
        if (db == null || !db.isOpen())
            db = getWritableDatabase();
        db.delete(TABLE_CATEGORY, null, null);;
    }

    @Override
    public void addCategory(BallTrainingModel ballTrainingModel) {
        List<BallTrainingModel> ballTrainingModelList = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put(KEY_IDS, ballTrainingModel.getIds());
            values.put(KEY_CATEGORY, ballTrainingModel.getCategory());
            values.put(KEY_CATTHUMB, ballTrainingModel.getCatThumb());
            db.insert(TABLE_CATEGORY, null, values);
            db.close();
        }catch (Exception e){
            Log.e("problem",e+"");
        }
    }


}
