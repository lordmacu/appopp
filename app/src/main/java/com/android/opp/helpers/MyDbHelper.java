package com.android.opp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.opp.models.Interest;
import com.android.opp.models.ItemEncuesta;
import com.android.opp.models.User;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

/**
 * Created by camilo on 20/6/17.
 */



public class MyDbHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "survey4.db";
    private static final int DATABASE_VERSION = 1;

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // you can use an alternate constructor to specify a database location
        // (such as a folder on the sd card)
        // you must ensure that this folder is available and you have permission
        // to write to it
        //super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);

    }


    public void insertNewAnuncio(ItemEncuesta encuesta) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put("encuesta", encuesta.getEncuesta());
        insertValues.put("imagenes", encuesta.getImagenes());
        insertValues.put("desde", encuesta.getDesde());
        insertValues.put("hasta", encuesta.getHasta());
        insertValues.put("gender", encuesta.getGender());
        insertValues.put("cantidad", encuesta.getCantidadPersonas());
        insertValues.put("intereses", encuesta.getIntereses());
        insertValues.put("localizacion", encuesta.getLocalizacion());

        database.insert("survey", null, insertValues);
        database.close();


        Log.v("encuestas",getEncuestas().toString());

    }


    public void removeUsers() {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        database.execSQL("DELETE FROM user");
        database.close();
    }


    public void removeInterests() {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        database.execSQL("DELETE FROM intereses");
        database.close();

    }

    public void insertInterest(String interes) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put("id_interest", interes);
        database.insert("intereses", null, insertValues);

        database.close();

        Log.v("getInterest",getInterest().toString());


    }

    public void insertUser(User user) {
        removeUsers();
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put("name", user.getImage());
        insertValues.put("image", user.getName());
        insertValues.put("id", user.getId());
        insertValues.put("type", user.getType());


        database.insert("user", null, insertValues);
        database.close();

        Log.v("encuestas",getUser().toString());


    }

    public User getUser(){
        ArrayList<User> usuarios= new ArrayList<User>();
        Log.v("get  ","getUsers");
        String selectQuery = "SELECT * FROM user";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        User user= new User();

        if (cursor.moveToFirst()) do {


            user.setId(cursor.getString(0));
            user.setUser_id(cursor.getString(1));
            user.setName(cursor.getString(3));
            user.setImage(cursor.getString(2));
            user.setType(cursor.getString(4));



        } while (cursor.moveToNext());

        return user;
    }



    public ArrayList<Interest> getInterestByid(String id){
        ArrayList<Interest> interestArray= new ArrayList<Interest>();
        Log.v("get  ","getInterestByid");
        String selectQuery = "SELECT * FROM intereses where id_interest=1";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) do {

            Interest item= new Interest();
            item.setId(cursor.getString(0));
            item.setId_interest(cursor.getString(1));



            interestArray.add(item);
        } while (cursor.moveToNext());

        Log.v("total:",interestArray.size()+""+interestArray.toString());

        return interestArray;
    }

    public ArrayList<Interest> getInterest(){
        ArrayList<Interest> interestArray= new ArrayList<Interest>();
        Log.v("get  ","getintereses");
        String selectQuery = "SELECT * FROM intereses";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) do {

            Interest item= new Interest();
            item.setId(cursor.getString(0));
            item.setId_interest(cursor.getString(1));



            interestArray.add(item);
            Log.v("interest-->",item.toString());
        } while (cursor.moveToNext());

        return interestArray;
    }

    public ArrayList<ItemEncuesta> getEncuestas(){
        ArrayList<ItemEncuesta> encuestas= new ArrayList<ItemEncuesta>();
        Log.v("get  ","getencuestas");
        String selectQuery = "SELECT * FROM survey where estado =1";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) do {

            ItemEncuesta item= new ItemEncuesta();
            item.setEncuesta(cursor.getString(0));
            item.setImagenes(cursor.getString(1));
            item.setDesde(cursor.getString(2));
            item.setHasta(cursor.getString(3));
            item.setGender(cursor.getString(4));
            item.setCantidadPersonas(cursor.getString(5));
            item.setIntereses(cursor.getString(6));
            item.setLocalizacion(cursor.getString(7));


            encuestas.add(item);
            Log.v("producto-->",item.toString());
        } while (cursor.moveToNext());

        return encuestas;
    }



}