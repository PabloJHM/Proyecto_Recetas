package com.example.pablo.proyecto_recetas.BD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Ayudante extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="recetario.sqlite";
    public static final int DATABASE_VERSION = 1;

    public Ayudante(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;

        //Tabla de ingredientes
        sql="create table "+ Tablas.TablaIngrediente.TABLA+
                " ("+ Tablas.TablaIngrediente._ID+
                " integer primary key autoincrement, "+
                Tablas.TablaIngrediente.NOMBRE+" text) ";
        db.execSQL(sql);

        //Tabla de recetas
        sql="create table "+Tablas.TablaReceta.TABLA+
                " ("+ Tablas.TablaReceta._ID+
                " integer primary key autoincrement, "+
                Tablas.TablaReceta.NOMBRE+" text, "+
                Tablas.TablaReceta.FOTO+" text, "+
                Tablas.TablaReceta.IDCATEGORIA+" long, "+
                Tablas.TablaReceta.INSTRUCCIONES+" text," +
                "FOREIGN KEY (" + Tablas.TablaReceta.IDCATEGORIA+")" +
                "REFERENCES "+Tablas.TablaCategoria.TABLA+"("+Tablas.TablaCategoria._ID+")"+
                ") ";
        db.execSQL(sql);

        //Tabla de RecetaIngredentes
        sql="create table "+Tablas.TablaRecetaIngrediente.TABLA+
                " ("+ Tablas.TablaRecetaIngrediente._ID+
                " integer primary key autoincrement, "+
                Tablas.TablaRecetaIngrediente.IDINGREDIENTE+" long, "+
                Tablas.TablaRecetaIngrediente.IDRECETA+" long, "+
                Tablas.TablaRecetaIngrediente.CANTIDAD+" text," +
                "FOREIGN KEY (" + Tablas.TablaRecetaIngrediente.IDINGREDIENTE+")" +
                "REFERENCES "+Tablas.TablaIngrediente.TABLA+"("+Tablas.TablaIngrediente._ID+"),"+
                "FOREIGN KEY (" + Tablas.TablaRecetaIngrediente.IDRECETA+")" +
                "REFERENCES "+Tablas.TablaReceta.TABLA+"("+Tablas.TablaReceta._ID+")"+
                ") ";
        db.execSQL(sql);

        //Tabla de Categorias
        sql="create table "+ Tablas.TablaCategoria.TABLA+
                " ("+ Tablas.TablaCategoria._ID+
                " integer primary key autoincrement, "+
                Tablas.TablaCategoria.NOMBRE+" text) ";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="drop table if exists "
                + Tablas.TablaIngrediente.TABLA+","+
                Tablas.TablaReceta.TABLA+","+
                Tablas.TablaRecetaIngrediente.TABLA;
        db.execSQL(sql);
        onCreate(db);
    }
}
