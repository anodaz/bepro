package com.anodaz.bepro.Service;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;

public class DBSqlite extends SQLiteOpenHelper {
    public DBSqlite(Context context) {
        super(context, Constants.DBNAME, null, Constants.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS item(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,date TEXT,image TEXT,status TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("Drop table if EXISTS item");
        //db.execSQL("Drop table if EXISTS BankBlood");
        onCreate(db);
    }
    public int additem(String date, String image, String status){
        int idr=0;
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        //contentValues.put("id",id);
        contentValues.put("date",date);
        contentValues.put("image",image);
        contentValues.put("status",status);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            idr = Math.toIntExact(db.insert("item", null, contentValues));
        }
        return idr;


    }
    public boolean isadd(int id){
        boolean isadd=false;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from item WHERE id="+id,null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            isadd=true;
            res.moveToNext();
        }
        return isadd;
    }
    public ArrayList<Item> getallp(){
        ArrayList<Item> rentalProperties=new ArrayList<Item>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from item",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            rentalProperties.add(new Item(res.getInt(res.getColumnIndex("id")),res.getString(res.getColumnIndex("date")),res.getString(res.getColumnIndex("image")),Boolean.parseBoolean(res.getString(res.getColumnIndex("status")))));
            res.moveToNext();
        }
        return rentalProperties;
    }
}
