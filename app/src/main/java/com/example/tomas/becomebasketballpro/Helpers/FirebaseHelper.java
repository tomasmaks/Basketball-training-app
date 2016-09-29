//package com.example.tomas.becomebasketballpro.Helpers;
//
//import android.content.Context;
//import android.widget.ListView;
//
//import com.example.tomas.becomebasketballpro.Fragments.ArticleListFragment;
//import com.example.tomas.becomebasketballpro.Model.ArticleModel;
//import com.example.tomas.becomebasketballpro.Model.MotivationModel;
//import com.firebase.client.Firebase;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseException;
//import com.google.firebase.database.DatabaseReference;
//import com.google.gson.Gson;
//
//import java.util.ArrayList;
//
///**
// * Created by Tomas on 23/09/2016.
// */
//public class FirebaseHelper {
//
//    DatabaseReference db;
//    Boolean saved=null;
//    ArrayList<ArticleModel> articleModel = new ArrayList<>();
//    ArticleListFragment.ArticleAdapter adapter;
//    ListView lv;
//    Firebase firebase;
//    String DB_URL;
//    Context c;
//
//
//    public FirebaseHelper(DatabaseReference db) {
//        this.db = db;
//    }
//
//    //WRITE
//    public Boolean save(ArticleModel articleModel)
//    {
//        if(articleModel==null)
//        {
//            saved=false;
//        }else
//        {
//            try
//            {
//                db.child("ArticleModel").push().setValue(articleModel);
//                saved=true;
//
//            }catch (DatabaseException e)
//            {
//                e.printStackTrace();
//                saved=false;
//            }
//        }
//
//        return saved;
//    }
//
//    //READ
//    public ArrayList<ArticleModel> retrieve()
//    {
//        db.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                fetchData(dataSnapshot);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                fetchData(dataSnapshot);
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        return articleModel;
//    }
//
//    private void fetchData(DataSnapshot dataSnapshot)
//    {
//        articleModel.clear();
//
//        for (DataSnapshot ds : dataSnapshot.getChildren())
//        {
//
//            ArticleModel articleModelList = new ArticleModel();
//            articleModelList.setTitle(ds.getValue(ArticleModel.class).getTitle());
//            articleModelList.setBody(ds.getValue(ArticleModel.class).getBody());
//
//            articleModel.add(articleModelList);
//        }
//        if(articleModel.size()>0)
//        {
//            adapter=new ArticleListFragment.ArticleAdapter(c,articleModel);
//            lv.setAdapter(adapter);
//        }
//
//
//
//    }
//}