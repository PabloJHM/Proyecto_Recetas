package com.example.pablo.proyecto_recetas.categoria;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pablo.proyecto_recetas.BD.Ayudante;
import com.example.pablo.proyecto_recetas.BD.Tablas;

import java.util.ArrayList;
import java.util.List;


public class GestorCategoria {

    private Ayudante abd;
    private SQLiteDatabase bd;
    public GestorCategoria(Ayudante a) {
        this.abd=a;
        bd=abd.getWritableDatabase();    }
    public void open() {
        bd = abd.getWritableDatabase();
    }
    public void openRead() {
        bd = abd.getReadableDatabase();
    }
    public void close() {
        abd.close();
    }

    public long insert(Categoria ca) {
        ContentValues valores = new ContentValues();
        valores.put(Tablas.TablaCategoria.NOMBRE,
                ca.getNombre());
        long id = bd.insert(Tablas.TablaCategoria.TABLA,
                null, valores);
        return id;
    }

    public int delete(Categoria ca) {
        String condicion = Tablas.TablaCategoria._ID + " = ?";
        String[] argumentos = { ca.getId() + "" };
        int cuenta = bd.delete(
                Tablas.TablaCategoria.TABLA, condicion, argumentos);
        return cuenta;
    }

    public int update(Categoria ca) {
        ContentValues valores = new ContentValues();
        valores.put(Tablas.TablaCategoria.NOMBRE, ca.getNombre());
        String condicion = Tablas.TablaCategoria._ID + " = ?";
        String[] argumentos = { ca.getId() + "" };
        int cuenta = bd.update(Tablas.TablaCategoria.TABLA, valores,
                condicion, argumentos);
        return cuenta;
    }

    //Select cuando queremos unas condiciones o parametros especificos
    public List<Categoria> select(String condicion,String[] parametros) {
        List<Categoria> lc;
        lc = new ArrayList<>();
        Cursor cursor = bd.query(Tablas.TablaCategoria.TABLA, null,
                condicion, parametros, null, null, null);
        cursor.moveToFirst();
        Categoria ca;
        while (!cursor.isAfterLast()) {
            ca = getRow(cursor);
            lc.add(ca);
            cursor.moveToNext();
        }
        cursor.close();
        return lc;
    }

    //SELECT * FROM
    public List<Categoria> select() {
        return select(null,null);
    }

    public Categoria getRow(Cursor c) {
        Categoria i = new Categoria();
        i.setId(c.getLong(c.getColumnIndex(Tablas.TablaCategoria._ID)));
        i.setNombre(c.getString(1));
        return i;
    }

    public Cursor getCursor() {
        Cursor cursor = bd.query(
                Tablas.TablaCategoria.TABLA, null, null, null, null, null,
                null);
        return cursor;
    }

    //Asignacion de categorias por defecto
    public void generacategorias(){
        Categoria c = new Categoria("Todos");
        insert(c);
        Categoria c1 = new Categoria("Carnes");
        insert(c1);
        Categoria c2 = new Categoria("Pescados");
        insert(c2);
        Categoria c3 = new Categoria("Pasta");
        insert(c3);
        Categoria c4 = new Categoria("Verduras");
        insert(c4);
        Categoria c5 = new Categoria("Sopas");
        insert(c5);
        Categoria c6 = new Categoria("Postres");
        insert(c6);
    }
}

