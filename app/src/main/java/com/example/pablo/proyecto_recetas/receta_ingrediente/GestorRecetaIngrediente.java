package com.example.pablo.proyecto_recetas.receta_ingrediente;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pablo.proyecto_recetas.BD.Ayudante;
import com.example.pablo.proyecto_recetas.BD.Tablas;
import com.example.pablo.proyecto_recetas.ingrediente.Ingrediente;

import java.util.ArrayList;
import java.util.List;

public class GestorRecetaIngrediente {
    private Ayudante abd;
    private SQLiteDatabase bd;
    private String query="SELECT * FROM RECETAINGREDIENTE INNER JOIN "+
            "INGREDIENTE ON RECETAINGREDIENTE.IDINGREDIENTE = "+
            "INGREDIENTE._ID WHERE " + Tablas.TablaRecetaIngrediente.IDRECETA+" = ";

    public GestorRecetaIngrediente(Ayudante a) {
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

    public long insert(RecetaIngrediente ri) {
        ContentValues valores = new ContentValues();
        valores.put(Tablas.TablaRecetaIngrediente.IDRECETA,
                ri.getIdReceta());
        valores.put(Tablas.TablaRecetaIngrediente.IDINGREDIENTE,
                ri.getIdIngrediente());
        valores.put(Tablas.TablaRecetaIngrediente.CANTIDAD,
                ri.getCantidad());
        long id = bd.insert(Tablas.TablaRecetaIngrediente.TABLA,
                null, valores);
        return id;
    }

    public int delete(RecetaIngrediente ri) {
        String condicion = Tablas.TablaRecetaIngrediente._ID + " = ?";
        String[] argumentos = { ri.getId() + "" };
        int cuenta = bd.delete(
                Tablas.TablaRecetaIngrediente.TABLA, condicion, argumentos);
        return cuenta;
    }

    public int update(RecetaIngrediente ri) {
        ContentValues valores = new ContentValues();
        valores.put(Tablas.TablaRecetaIngrediente.IDRECETA, ri.getIdReceta());
        valores.put(Tablas.TablaRecetaIngrediente.IDINGREDIENTE, ri.getIdIngrediente());
        valores.put(Tablas.TablaRecetaIngrediente.CANTIDAD, ri.getCantidad());
        String condicion = Tablas.TablaRecetaIngrediente._ID + " = ?";
        String[] argumentos = { ri.getId() + "" };
        int cuenta = bd.update(Tablas.TablaRecetaIngrediente.TABLA, valores,
                condicion, argumentos);
        return cuenta;
    }

    public List<RecetaIngrediente> select(String condicion,String[] parametros) {
        List<RecetaIngrediente> la= new ArrayList<>();
        Cursor cursor =
                bd.query(
                        Tablas.TablaRecetaIngrediente.TABLA,
                        null,
                        condicion,
                        parametros,
                        null,
                        null,
                        null);
        cursor.moveToFirst();
        RecetaIngrediente rc;
        while (!cursor.isAfterLast()) {
            rc = getRow(cursor);
            la.add(rc);
            cursor.moveToNext();
        }
        cursor.close();
        return la;
    }

    public List<RecetaIngrediente> select() {
        return select(null,null);
    }

    public RecetaIngrediente getRow(Cursor c) {
        RecetaIngrediente ri = new RecetaIngrediente();
        ri.setId(c.getLong(c.getColumnIndex(Tablas.TablaRecetaIngrediente._ID)));
        ri.setIdReceta(c.getLong(1));
        ri.setIdIngrediente(c.getLong(2));
        ri.setCantidad(c.getInt(3));
        return ri;
    }

    public Cursor getCursor() {
        Cursor cursor = bd.query(
                Tablas.TablaRecetaIngrediente.TABLA, null, null, null, null, null,
                null);
        return cursor;
    }

    //Inner join para obtener un string con la cantidad y nombre de los ingredientes para mostrar
    //en archivo "Ver.java"
    public String selectIngredientes(String[] parametros){
        String ingredientes="";
        Cursor cursor = getCursorInner(parametros);
        while(cursor.moveToNext()){
            ingredientes=ingredientes+""+cursor.getString(cursor.getColumnIndex(Tablas.TablaRecetaIngrediente.CANTIDAD))+
                    " "+cursor.getString(cursor.getColumnIndex(Tablas.TablaIngrediente.NOMBRE))+"\n";
        }
        return ingredientes;
    }

    //Inner join para obtener una lista de ingredientes para mostrar y poder editar los ingredientes
    //y la cantidad en el archivo "Editar.java"
    public List<Ingrediente> innerIngrediente(String[] parametros){
        List<Ingrediente> LI=new ArrayList<>();
        Ingrediente i;
        Cursor cursor = getCursorInner(parametros);
        while(cursor.moveToNext()){
            i= new Ingrediente();
            i.setId(cursor.getLong(cursor.getColumnIndex(Tablas.TablaIngrediente._ID)));
            i.setNombre(cursor.getString(cursor.getColumnIndex(Tablas.TablaIngrediente.NOMBRE)));
            LI.add(i);
        }
        return LI;
    }

    public Cursor getCursorInner(String[] parametros){
        return bd.rawQuery(query+parametros[0]+";",null);
    }
}
