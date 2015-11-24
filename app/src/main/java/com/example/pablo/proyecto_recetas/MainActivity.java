package com.example.pablo.proyecto_recetas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.pablo.proyecto_recetas.BD.Ayudante;
import com.example.pablo.proyecto_recetas.categoria.Categoria;
import com.example.pablo.proyecto_recetas.categoria.GestorCategoria;
import com.example.pablo.proyecto_recetas.funciones.Aniadir;
import com.example.pablo.proyecto_recetas.funciones.Editar;
import com.example.pablo.proyecto_recetas.funciones.Ver;
import com.example.pablo.proyecto_recetas.ingrediente.GestorIngrediente;
import com.example.pablo.proyecto_recetas.receta.GestorReceta;
import com.example.pablo.proyecto_recetas.receta.Receta;
import com.example.pablo.proyecto_recetas.receta_ingrediente.GestorRecetaIngrediente;
import com.example.pablo.proyecto_recetas.receta_ingrediente.RecetaIngrediente;

import java.util.List;

public class MainActivity extends Activity {
    private static final int ANIADIR=0,EDITAR=1;
    private ListView lv;
    private ImageView iv;
    private Spinner sp;
    private GestorReceta gr;
    private GestorRecetaIngrediente gri;
    private GestorIngrediente gi;
    private GestorCategoria gc;
    private Adaptador ad;
    private List<Receta> lista;
    private int SPposicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bd();
        init();
        addListenerOnSpinnerItemSelection();
    }

    @Override
    protected void onResume(){
        super.onResume();
        generaAdaptador();
        List<Categoria> lc=gc.select();
        //Inicializo solo una vez las categorias.
        if(lc.size()==0){
            gc.generacategorias();
        }
    }
    @Override
    public void onPostResume(){
        super.onPostResume();
        generaAdaptador();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


    public void init(){
        lv=(ListView) findViewById(R.id.lvLista);
        iv=(ImageView) findViewById(R.id.ivAdd);
        sp=(Spinner)findViewById(R.id.spinner);
        generaAdaptador();

    }

    public void bd(){
        Ayudante ay=new Ayudante(this);
        gr=new GestorReceta(ay);
        gri=new GestorRecetaIngrediente(ay);
        gi=new GestorIngrediente(ay);
        gc=new GestorCategoria(ay);
    }

    /*******************************************OnClicks*******************************************/
    public void add(View v){
        Intent i=new Intent (this, Aniadir.class);
        startActivity(i);
    }

    public void lanzarVer(int position){
        long idRec=lista.get(position).getId();
        Intent i = new Intent(this, Ver.class);
        Bundle b=new Bundle();
        b.putLong("id",idRec);
        i.putExtras(b);
        startActivity(i);
    }
    /*******************************************Spinner********************************************/
    public void addListenerOnSpinnerItemSelection() {
        sp = (Spinner) findViewById(R.id.spinner);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtengo la posición del item seleccionado
                SPposicion = parent.getPositionForView(view);
                generaAdaptador();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        ad.notifyDataSetChanged();
    }

    //Genero la lista en condición del elemento seleccionado en el spinner. Si es la posicion 0 (todos)
    //aparecerán todas las recetas. En cualquier otro caso, aparecerán solo las recetas cuya categoria
    //sea la seleccionada en el spinner
    public void generaAdaptador(){
        if(SPposicion==0){
            lista=gr.select();
        } else {
            lista = gr.select("IDCATEGORIA = " + (SPposicion+1) ,null);
        }

        ad=new Adaptador(this,R.layout.item_list,lista);
        lv.setAdapter(ad);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lanzarVer(position);
            }
        });
        registerForContextMenu(lv);
    }

    /***************************************Menú contextual****************************************/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextual, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo vistainfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int posicion = vistainfo.position;
        switch(item.getItemId()){

            case R.id.mnBorrar:
                long id=lista.get(posicion).getId();
                Receta borrar=gr.select("_ID = "+id,null).get(0);
                gr.delete(borrar);
                List<RecetaIngrediente> borrarRI = gri.select("IDRECETA = " + id, null);
                for (RecetaIngrediente aux: borrarRI) {
                    gri.delete(aux);
                }
                generaAdaptador();
                ad.notifyDataSetChanged();
                return true;

            case R.id.mnEditar:
                Intent i = new Intent( this,Editar.class);
                long idRec=lista.get(posicion).getId();
                Bundle b=new Bundle();
                b.putLong("id",idRec);
                i.putExtras(b);
                startActivityForResult(i,EDITAR);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
