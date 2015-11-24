package com.example.pablo.proyecto_recetas.receta;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pablo.proyecto_recetas.BD.Ayudante;
import com.example.pablo.proyecto_recetas.BD.Tablas;

import java.util.ArrayList;
import java.util.List;

public class GestorReceta {
    private Ayudante abd;
    private SQLiteDatabase bd;
    public GestorReceta(Ayudante a) {
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

    //Si el insert falla devuelve -1
    public long insert(Receta rc) {
        ContentValues valores = new ContentValues();
        valores.put(Tablas.TablaReceta.NOMBRE,
                rc.getNombre());
        valores.put(Tablas.TablaReceta.FOTO,
                rc.getFoto());
        valores.put(Tablas.TablaReceta.INSTRUCCIONES,
                rc.getInstrucciones());
        valores.put(Tablas.TablaReceta.IDCATEGORIA,
                rc.getIdCat());
        long id = bd.insert(Tablas.TablaReceta.TABLA,
                null, valores);
        return id;
    }

    public int delete(Receta rc) {
        String condicion = Tablas.TablaReceta._ID + " = ?";
        String[] argumentos = { rc.getId() + "" };
        int cuenta = bd.delete(
                Tablas.TablaReceta.TABLA, condicion, argumentos);
        return cuenta;
    }

    public int update(Receta rc) {
        ContentValues valores = new ContentValues();
        valores.put(Tablas.TablaReceta.NOMBRE, rc.getNombre());
        valores.put(Tablas.TablaReceta.FOTO, rc.getFoto());
        valores.put(Tablas.TablaReceta.INSTRUCCIONES, rc.getInstrucciones());
        valores.put(Tablas.TablaReceta.IDCATEGORIA,rc.getIdCat());
        String condicion = Tablas.TablaReceta._ID + " = ?";
        String[] argumentos = { rc.getId() + "" };
        int cuenta = bd.update(Tablas.TablaReceta.TABLA, valores,
                condicion, argumentos);
        return cuenta;
    }

    //Select cuando queremos unas condiciones o parametros especificos
    public List<Receta> select(String condicion,String[] parametros) {
        List<Receta> la= new ArrayList<>();
        Cursor cursor = bd.query( Tablas.TablaReceta.TABLA,
                null,
                condicion,
                parametros,
                null,
                null,
                Tablas.TablaReceta._ID+", "+Tablas.TablaReceta.NOMBRE);
        Receta rc;
        while (cursor.moveToNext()) {
            rc = getRow(cursor);
            la.add(rc);
        }
        cursor.close();
        return la;
    }

    public List<Receta> select() {
        return select(null,null);
    }

    public Receta getRow(Cursor c) {
        Receta r = new Receta();
        r.setId(c.getLong(c.getColumnIndex(Tablas.TablaReceta._ID)));
        r.setNombre(c.getString(c.getColumnIndex(Tablas.TablaReceta.NOMBRE)));
        r.setFoto(c.getString(c.getColumnIndex(Tablas.TablaReceta.FOTO)));
        r.setInstrucciones(c.getString(c.getColumnIndex(Tablas.TablaReceta.INSTRUCCIONES)));
        r.setIdCat(c.getLong(c.getColumnIndex(Tablas.TablaReceta.IDCATEGORIA)));
        return r;
    }

    public Cursor getCursor() {
        Cursor cursor = bd.query(
                Tablas.TablaReceta.TABLA, null, null, null, null, null,
                null);
        return cursor;
    }

}
