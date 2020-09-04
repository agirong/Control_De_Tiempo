package com.example.timingrecord.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManager {
    private DbHelper helper;
    private SQLiteDatabase sqLiteDatabase;

    public DataBaseManager(Context context){
        this.helper=DbHelper.getInstance(context);
    }
    //para abrir la base de datos en modo escritura
    public SQLiteDatabase openWriteableDB(){
        if (sqLiteDatabase==null || !sqLiteDatabase.isOpen())
            sqLiteDatabase=this.helper.getWritableDatabase();
        return sqLiteDatabase;
    }
    //para abrir la base de datos en modo lectura
    public SQLiteDatabase openReableDB(){
        if (sqLiteDatabase==null || !sqLiteDatabase.isOpen())
            sqLiteDatabase=this.helper.getReadableDatabase();
        return sqLiteDatabase;
    }
    //metodo para  cerrar la base de datos .close
    public void closeDB(SQLiteDatabase db){
        if (db!=null)
            db.close();
    }
    //para crear la tabla
    public  static String timeTable="CREATE TABLE time(id INTEGER NOT NULL PRIMARY KEY,"+
            "time TEXT(100) NOT NULL,"+
            "date TEXT(100) NOT NULL,"+
            "distance TEXT(100) NOT NULL,"+
            "description TEXT(100) NOT NULL)";

}
