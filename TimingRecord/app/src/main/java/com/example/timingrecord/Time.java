package com.example.timingrecord;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.util.Log;

import com.example.timingrecord.bd.DataBaseManager;

import java.util.ArrayList;

public class Time {
    private int id;
    private String time;
    private String date;
    private String distance;
    private String description;

    //variables locales para recibir de la base de datos
    private static DataBaseManager dataBaseManager;
    private static SQLiteDatabase database;
    //variable que sirve para hacer la consulta a la tabla especifica de la db en este caso Time
    private static final String table="time";

    //constructor de la clase inicializador de variables
    public Time(){
        id=0;
        time="";
        date="";
        distance="";
        description="";
    }

    public Time(int id, String time, String date, String distance, String description){
        this.id=id;
        this.time=time;
        this.date=date;
        this.distance=distance;
        this.description=description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //para abrir la base de datos
    public Time(Context context, boolean write){
        dataBaseManager=new DataBaseManager(context);
        if(write)
            database=dataBaseManager.openWriteableDB();
        else
            database=dataBaseManager.openReableDB();
    }
    //metodo para CONSULTAR todos los registros que estan en la tabla time de la DB
    public ArrayList<Time> getAllTime(){
        try {
            ArrayList<Time> allTime=new ArrayList<>();
            Cursor cursor=database.rawQuery("select * from "+table,null);
            while(cursor.moveToNext()){
                Time time=new Time(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
                allTime.add(time);
            }
            dataBaseManager.closeDB(database);
            return allTime;
        }catch (Exception ex){
            Log.e("Time/getAllTime",ex.toString());
            return null;
        }
    }
    //metodo para OBTENER el ID de la tabla
    public Time getTime(int id){
        try {
            Cursor cursor = database.rawQuery("select * from " + table + " where id= '" +id+ "'",null);
            while(cursor.moveToFirst()){
                return new Time(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
            }
            dataBaseManager.closeDB(database);
        }catch (Exception ex){
            Log.e("Time/getTime",ex.toString());
            return null;
        }
        return null;
    }

    //INSERTAR un dato a la base de datos para GUARDAR
    public boolean save(Time time){
        try {
            ContentValues values=new ContentValues();
            values.put("time",time.getTime());
            values.put("date",time.getDate());
            values.put("distance",time.getDistance());
            values.put("description",time.getDescription());
            boolean result=database.insert(table,null,values)!=0;
            dataBaseManager.closeDB(database);
            return  result;
        }catch (Exception ex){
            Log.e("Time/Save",ex.toString());
            return false;
        }
    }
    //ELIMINAR un dato de la tabla
    public boolean delete(int id){
        try {
            boolean result=database.delete(table,"id='"+id+"'",null)!=0;
            dataBaseManager.closeDB(database);
            return result;
        }catch (Exception ex){
            Log.e("Time/Delete",ex.toString());
            return false;
        }
    }
    //MODIFICAR un dato de la Tabla
    public boolean update(Time time){
        try{
            ContentValues values=new ContentValues();
            values.put("time",time.getTime());
            values.put("date",time.getDate());
            values.put("distance",time.getDistance());
            values.put("description",time.getDescription());
            boolean result=database.update(table,values,"id='"+time.getId()+"'",null)!=0;
            dataBaseManager.closeDB(database);
            return  result;

        }catch (Exception ex){
            Log.e("Time/update",ex.toString());
            return false;
        }
    }

}
