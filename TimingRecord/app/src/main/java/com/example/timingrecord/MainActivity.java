package com.example.timingrecord;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private EditText txtTime;
    private EditText txtDate;
    private EditText txtDescription;
    private EditText txtDistance;
    private ImageView btnStart;
    private ImageView btnPause;
    private Button btnFinalize;
    private Chronometer chronometer;
    private boolean pauseCliked;
    private boolean startCliked;
    private SimpleDateFormat date=new SimpleDateFormat("dd-MMM-yyyy");
    private long timePause;

    //variables para llamar el arraylist
    private ArrayList<Time> listTime;
    private TimeAdapter adapter;
    private RecyclerView recyclerView;
    //dialog
    private Dialog dialog;
    //tiempo en segundos
    private long timeSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart=(ImageView)findViewById(R.id.btnStart);
        btnFinalize=(Button)findViewById(R.id.btnFinalize);
        btnPause=(ImageView)findViewById(R.id.btnPause);

        //inicializar el recyclerView View en esta linea
        recyclerView=(RecyclerView)findViewById(R.id.rvTime);
        chronometer=(Chronometer)findViewById(R.id.chronometer);
        //asiganer al recyclerView
        dialog=new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertdialog_new);
        showListTime();
        //metodos de los botones
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime()+timePause);
                chronometer.start();
                pauseCliked=false;
                startCliked=true;
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(!pauseCliked){
                timePause=chronometer.getBase()-SystemClock.elapsedRealtime();
                chronometer.stop();
                pauseCliked=true;
            }
            }
        });

        btnFinalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startCliked){
                    if(pauseCliked){
                        new AlertDialog.Builder(MainActivity.this).setTitle(getString(R.string.title))
                                .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        newTime(null);
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton(getString(R.string.restart),new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                chronometer.setBase(SystemClock.elapsedRealtime());
                                timePause=0;
                                startCliked=false;
                                dialog.dismiss();
                            }
                        }).show();
                    }else{
                        Toast.makeText(getApplicationContext(),getString(R.string.pause_clicked),Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),getString(R.string.pause_clicked),Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void showListTime(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        listTime=new Time(getApplicationContext(),false).getAllTime();
        if (listTime!=null){
            adapter=new TimeAdapter(listTime, new TimeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Time time) {
                    newTime(time);
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }
    //para insetar los datos a la de DB
    public void newTime(final Time time){
        //llamar a los componentes
        txtTime=(EditText)dialog.findViewById(R.id.txtTime);
        txtDescription=(EditText)dialog.findViewById(R.id.txtDescription);
        txtDistance=(EditText)dialog.findViewById(R.id.txtDistance);
        txtDate=(EditText)dialog.findViewById(R.id.txtDates);
        //traemos el valor del tiempo
        timeSecond=(long)Math.abs(timePause/1000);
        //porque el tiempo se mide en milisegundos.para formaterarlo base 60
        if(time!=null){
            txtDate.setText(time.getDate());
            txtDistance.setText(time.getDistance());
            txtDescription.setText(time.getDescription());
            txtTime.setText(time.getTime());
        }else{
            //long horas=timeSecond/3600;
            long minutos=(timeSecond%3600)/60;
            long segundos=(timeSecond%60);
            long milliSec=(timeSecond/1000);
            //*******************   txtTime.setText(String.format("%02d:%02d:%02d",horas,minutos,segundos));
            //txtTime.setText(String.format("%02d:%02d:%02d:02d",horas,minutos,segundos,milliSec));

            //
            txtTime.setText(String.format("%02d",minutos)+":"
            +String.format("%02d",segundos)+":"+String.format("%02d",milliSec));

            chronometer.setText(String.format("%02d",minutos)+":"
                    +String.format("%02d",segundos)+":"+String.format("%02d",milliSec));
            txtDate.setText(date.format(Calendar.getInstance().getTime()));
        }
        dialog.show();
        // Cuando demos un click en btnSave, entonces pasará esto
        dialog.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (time!=null)
                    new Time(getApplicationContext(), true).update(getData(time.getId()));
                else
                    new Time(getApplicationContext(),true).save(getData(0));
                clear();
                chronometer.setBase(SystemClock.elapsedRealtime());
                timePause=0;
                dialog.dismiss();
                showListTime();
            }
        });

        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // Vamos a dar funcionalidad al botón CANCELAR
        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí dentro, simplemente hacemos dismiss
                dialog.dismiss();
            }
        });


    }

    // Elaboramos el método para la línea 222
    // Así construimos otro objeto Time que es llamado desde el , update , de la línea 222
    // Cogemos el id, construimos el objeto time y luego en esa línea 222 llamamos a
    // este objeto
    public Time getData(int id){
        return new Time(id, txtTime.getText().toString(), txtDate.getText().toString(), txtDistance.getText().toString(), txtDescription.getText().toString());
    }

    // CONTINUAMOS
    // Con este método limpiamos las cajas de texto
    public void clear(){
        txtDate.setText("");
        txtTime.setText("");
        txtDistance.setText("");
        txtDescription.setText("");

    }


}

