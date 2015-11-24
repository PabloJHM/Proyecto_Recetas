package com.example.pablo.proyecto_recetas.funciones;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pablo.proyecto_recetas.BD.Ayudante;
import com.example.pablo.proyecto_recetas.R;
import com.example.pablo.proyecto_recetas.categoria.Categoria;
import com.example.pablo.proyecto_recetas.categoria.GestorCategoria;
import com.example.pablo.proyecto_recetas.ingrediente.GestorIngrediente;
import com.example.pablo.proyecto_recetas.ingrediente.Ingrediente;
import com.example.pablo.proyecto_recetas.receta.GestorReceta;
import com.example.pablo.proyecto_recetas.receta.Receta;
import com.example.pablo.proyecto_recetas.receta_ingrediente.GestorRecetaIngrediente;
import com.example.pablo.proyecto_recetas.receta_ingrediente.RecetaIngrediente;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ver extends Activity {
    private Receta re;
    private ImageView ivImg;
    private TextView tvNom,tvInstru,tvCat;
    private List<Ingrediente> li;
    private List<RecetaIngrediente> lri;
    private GestorRecetaIngrediente gri;
    private GestorReceta gr;
    private GestorIngrediente gi;
    private GestorCategoria gc;
    private Ayudante a;
    private long id;
    private String ingredientes;
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_receta);
        Intent i=getIntent();
        Bundle b=i.getExtras();
        //Recojo la id de la receta seleccionada
        id=b.getLong("id");
        init();
    }

    public void init(){
        a=new Ayudante(this);
        ivImg= (ImageView)findViewById(R.id.ivFotoRec);
        tvNom=(TextView)findViewById(R.id.tvNombreReceta);
        tvInstru=(TextView)findViewById(R.id.tvInstru);
        tvCat=(TextView)findViewById(R.id.tvCategoria);
        gri=new GestorRecetaIngrediente(a);
        gr=new GestorReceta(a);
        gi=new GestorIngrediente(a);
        gc=new GestorCategoria(a);

        //Obtengo los datos de la receta asi como sus ingredientes
        re= gr.select("_ID = "+id, null).get(0);
        ingredientes=gri.selectIngredientes(new String[]{"" + id});

        li=gi.select();
        lri=gri.select();

        ViewHolder vh = new ViewHolder();

        TextView tv = (TextView)findViewById(R.id.tvNombreReceta);
        vh.tv1=tv;
        TextView tv2 = (TextView)findViewById(R.id.tvInstru);
        vh.tv2=tv2;
        TextView tv3 = (TextView)findViewById(R.id.tvIngredientes);
        vh.tv3=tv3;
        TextView tv4 =(TextView)findViewById(R.id.tvCategoria);
        vh.tv4=tv4;
        ImageView iv = (ImageView)findViewById(R.id.ivFotoRec);
        vh.iv=iv;

        //Muestro los datos
        vh.tv1.setText(re.getNombre());
        vh.tv2.setText(re.getInstrucciones());
        vh.tv3.setText(ingredientes);

        //Obtengo el nombre de la categoria obteniendo la categoria cuya id es igual al idcategoria
        //de la receta.
        Categoria c=gc.select("_ID = "+re.getIdCat(),null).get(0);
        String nomCat=c.getNombre();

        vh.tv4.setText(nomCat);

        File imgf= new File(re.getFoto());
        if(imgf.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgf.getAbsolutePath());
            vh.iv.setImageBitmap(myBitmap);
        }
    }
    private class ViewHolder {
        public TextView tv1, tv2, tv3, tv4;
        public ImageView iv;
    }
}
