package com.example.pablo.proyecto_recetas.BD;

import android.provider.BaseColumns;

public class Tablas {
    private Tablas(){
    }

    public static abstract class TablaIngrediente implements BaseColumns {
        public static final String TABLA = "ingrediente";
        public static final String NOMBRE = "nombre";
    }

    public static abstract class TablaReceta implements BaseColumns {
        public static final String TABLA = "receta";
        public static final String NOMBRE = "nombre";
        public static final String FOTO = "foto";
        public static final String INSTRUCCIONES = "instrucciones";
        public static final String IDCATEGORIA = "idcategoria";
    }

    public static abstract class TablaRecetaIngrediente implements BaseColumns {
        public static final String TABLA = "recetaingrediente";
        public static final String IDRECETA = "idreceta";
        public static final String IDINGREDIENTE = "idingrediente";
        public static final String CANTIDAD = "cantidad";
    }

    public static abstract class TablaCategoria implements BaseColumns {
        public static final String TABLA = "categoria";
        public static final String NOMBRE = "nombre";
    }
}
