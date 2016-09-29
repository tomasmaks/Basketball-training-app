package com.example.tomas.becomebasketballpro.DBHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tomas.becomebasketballpro.Listeners.ArticleListener;
import com.example.tomas.becomebasketballpro.Model.ArticleModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas on 30/08/2016.
 */
public class ArticleDbHandler extends SQLiteOpenHelper implements ArticleListener {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "ArticleDatabase.db";
    private static final String TABLE_NAME = "article_table";
    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "_title";
    private static final String KEY_BODY = "_body";
    private static final String KEY_THUMB = "_thumb";
    private static final String KEY_PHOTO = "_photo";
    private static final String KEY_VIDEO = "_video";
    private static final String KEY_PUBLISHED_DATE = "_published_date";
    private SQLiteDatabase db;
    String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+KEY_ID+" INTEGER PRIMARY KEY,"+KEY_TITLE+" TEXT,"+KEY_BODY+" TEXT,"+KEY_THUMB+" TEXT,"+KEY_PHOTO+" TEXT,"+KEY_VIDEO+" TEXT,"+KEY_PUBLISHED_DATE+" TEXT)";
    String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;

    public ArticleDbHandler(Context context) {
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
    public void addArticle(ArticleModel articleModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, articleModel.getTitle());
            values.put(KEY_BODY, articleModel.getBody());
            values.put(KEY_THUMB, articleModel.getThumbnail());
            values.put(KEY_PHOTO, articleModel.getImage());
            values.put(KEY_VIDEO, articleModel.getVideoURI());
            values.put(KEY_PUBLISHED_DATE, articleModel.getData());
            db.insert(TABLE_NAME, null, values);
            db.close();
        }catch (Exception e){
            Log.e("problem",e+"");
        }
    }


    @Override
    public List<ArticleModel> getAllArticle() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<ArticleModel> articleModelList = null;

        try{
            articleModelList = new ArrayList<ArticleModel>();
            String QUERY = "SELECT * FROM "+TABLE_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            if(!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    ArticleModel articleModel = new ArticleModel();
                    articleModel.setId(cursor.getString(0));
                    articleModel.setTitle(cursor.getString(1));
                    articleModel.setBody(cursor.getString(2));
                    articleModel.setThumbnail(cursor.getString(3));
                    articleModel.setImage(cursor.getString(4));
                    articleModel.setVideoURI(cursor.getString(5));
                    articleModel.setData(cursor.getString(6));
                    articleModelList.add(articleModel);

                }
                cursor.close();

            }

        }catch (Exception e){
            Log.e("error",e+"");
        }
        return articleModelList;
    }

    @Override
    public int getArticleCount() {
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

}
