package com.example.pablo.proyecto_recetas.ingrediente;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pablo.proyecto_recetas.BD.Ayudante;
import com.example.pablo.proyecto_recetas.BD.Tablas;

import java.util.ArrayList;
import java.util.List;

public class GestorIngrediente {
    private Ayudante abd;
    private SQLiteDatabase bd;
    public GestorIngrediente(Ayudante a) {
        this.abd=a;
        bd=abd.getWritableDatabase();
    }
    public void open() {
        bd = abd.getWritableDatabase();
    }
    public void openRead() {
        bd = abd.getReadableDatabase();
    }
    public void close() {
        abd.close();
    }

    public long insert(Ingrediente ig) {
        ContentValues valores = new ContentValues();
        valores.put(Tablas.TablaIngrediente.NOMBRE,
                ig.getNombre());
        long id = bd.insert(Tablas.TablaIngrediente.TABLA,
                null, valores);
        return id;
    }

    public int delete(Ingrediente ig) {
        String condicion = Tablas.TablaIngrediente._ID + " = ?";
        String[] argumentos = { ig.getId() + "" };
        int cuenta = bd.delete(
                Tablas.TablaIngrediente.TABLA, condicion, argumentos);
        return cuenta;
    }

    public int update(Ingrediente ig) {
        ContentValues valores = new ContentValues();
        valores.put(Tablas.TablaIngrediente.NOMBRE, ig.getNombre());
        String condicion = Tablas.TablaIngrediente._ID + " = ?";
        String[] argumentos = { ig.getId() + "" };
        int cuenta = bd.update(Tablas.TablaIngrediente.TABLA, valores,
                condicion, argumentos);
        return cuenta;
    }

    //Select cuando queremos unas condiciones o parametros especificos
    public List<Ingrediente> select(String condicion,String[] parametros) {
        List<Ingrediente> la = new ArrayList<Ingrediente>();
        Cursor cursor = bd.query(Tablas.TablaIngrediente.TABLA, null,
                condicion, parametros, null, null, null);
        Ingrediente ig;
        while (cursor.moveToNext()) {
            ig = getRow(cursor);
            la.add(ig);
        }
        cursor.close();
        return la;
    }

    public List<Ingrediente> select() {
        return select(null,null);
    }

    public Ingrediente getRow(Cursor c) {
        Ingrediente i = new Ingrediente();
        i.setId(c.getLong(c.getColumnIndex(Tablas.TablaIngrediente._ID)));
        i.setNombre(c.getString(1));
        return i;
    }

    public Cursor getCursor() {
        Cursor cursor = bd.query(
                Tablas.TablaIngrediente.TABLA, null, null, null, null, null,
                null);
        return cursor;
    }
}
