package com.example.pablo.proyecto_recetas.funciones;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pablo.proyecto_recetas.BD.Ayudante;
import com.example.pablo.proyecto_recetas.R;
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

public class Editar extends Activity {
    private Spinner sp;
    private List<EditText> arrayIng=new ArrayList<>(),arrayCant=new ArrayList<>();
    private Receta re;
    private RecetaIngrediente rei;
    private List<Ingrediente> listaIngredientes;
    private Ayudante a;
    private GestorRecetaIngrediente gri;
    private GestorReceta gr;
    private GestorIngrediente gi;
    private GestorCategoria gc;
    private long idReceta;
    private String ruta;

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_receta);
        Intent i=getIntent();
        Bundle b=i.getExtras();
        idReceta=b.getLong("id");
        init();
    }

    public void init(){
        a=new Ayudante(this);
        re=new Receta();
        rei=new RecetaIngrediente();
        gri=new GestorRecetaIngrediente(a);
        gr=new GestorReceta(a);
        gi=new GestorIngrediente(a);
        gc=new GestorCategoria(a);
        obtenerDatos();
    }

    private class ViewHolder {
        public EditText etNom, etInstruc;
        public ImageView iv;
        public LinearLayout LLI,LLC;
    }

    /**************************Sacar datos de la receta seleccionada*******************************/
    public void obtenerDatos(){
        ViewHolder vh = new ViewHolder();
        sp=(Spinner)findViewById(R.id.spinner2);
        EditText tv = (EditText)findViewById(R.id.etCambiaNombre);
        vh.etNom=tv;
        EditText tv2 = (EditText)findViewById(R.id.etCambiaInst);
        vh.etInstruc=tv2;
        ImageView iv = (ImageView)findViewById(R.id.ivCambiaImagen);
        vh.iv=iv;
        LinearLayout ll1 = (LinearLayout)findViewById(R.id.LLCambiaIng);
        vh.LLI=ll1;
        LinearLayout ll2 = (LinearLayout)findViewById(R.id.LLCambiaCant);
        vh.LLC=ll2;

        //Obtengo la receta con la que estoy trabajando
        re= gr.select("_ID = "+idReceta, null).get(0);

        //Obtengo todos los ingredientes de la receta
        listaIngredientes=gri.innerIngrediente(new String[]{idReceta + ""});
//        listaCantidad= new ArrayList<>();

        //Muestro los datos
        vh.etNom.setText(re.getNombre());
        vh.etInstruc.setText(re.getInstrucciones());
        File imgf= new File(re.getFoto());
        if(imgf.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgf.getAbsolutePath());
            vh.iv.setImageBitmap(myBitmap);
        }

        ViewGroup.LayoutParams parametros = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        //Recorro la lista de ingredientes asignados a la receta, añadiendo un nuevo edittext por cada
        //ingrediente y su cantidad
        for(Ingrediente aux:listaIngredientes){
            RecetaIngrediente sacaCantidad=gri.select("IDINGREDIENTE = "+aux.getId(),null).get(0);
            EditText et=new EditText(this);
            EditText et2=new EditText(this);
            et.setText(aux.getNombre());
            et2.setText(sacaCantidad.getCantidad() + "");
            arrayIng.add(et);
            arrayCant.add(et2);
            vh.LLI.addView(et, parametros);
            vh.LLC.addView(et2,parametros);
        }
    }

    /****************************************Guardar los cambios***********************************/
    public void guardarModificaciones(View v){
        re= gr.select("_ID = "+idReceta, null).get(0);
        ViewHolder vh = new ViewHolder();

        EditText tv = (EditText)findViewById(R.id.etCambiaNombre);
        vh.etNom=tv;
        EditText tv2 = (EditText)findViewById(R.id.etCambiaInst);
        vh.etInstruc=tv2;
        ImageView iv = (ImageView)findViewById(R.id.ivCambiaImagen);
        vh.iv=iv;
        LinearLayout ll1 = (LinearLayout)findViewById(R.id.LLCambiaIng);
        vh.LLI=ll1;
        LinearLayout ll2 = (LinearLayout)findViewById(R.id.LLCambiaCant);
        vh.LLC=ll2;

        String nombre=vh.etNom.getText().toString();
        String inst=vh.etInstruc.getText().toString();

        //Comprobacion de que no se quedan campos vacios
        if(nombre.isEmpty() || inst.isEmpty()) {
            Toast.makeText(this, R.string.vacio, Toast.LENGTH_LONG).show();
        }else{
            //Recojo los datos
            String nombreIng;
            re.setNombre(nombre);
            re.setInstrucciones(inst);

            if(ruta!=null){
                re.setFoto(ruta);
            } else {
                re.setFoto(re.getFoto());
            }

            String nomCat = sp.getSelectedItem().toString();
            re.setIdCat(gc.select("NOMBRE = '" + nomCat + "'", null).get(0).getId());

            gr.update(re);

            //Elimino todas las recetasingredientes que estaban ligadas a la receta, posteriormente
            //genero unas nuevas
            borrarRecetasIngredientes();

            //Recojo la lista de ingredientes y renuevo las RecetaIngredientes y actualizo ingredientes
            List<Ingrediente> arrayIngrediente;
            Ingrediente aux;
            long idIngrediente;
            int cantidad;
            for (int i = 0; i < arrayIng.size(); i++) {
                nombreIng=arrayIng.get(i).getText().toString();
                if(nombreIng.isEmpty()) {
                    Toast.makeText(this, R.string.vacio, Toast.LENGTH_LONG).show();
                }else{
                    arrayIngrediente = gi.select("nombre = '" + nombreIng + "'", null);
                    if (arrayIngrediente.size() > 0) {
                        idIngrediente = arrayIngrediente.get(0).getId();
                        String cant=arrayCant.get(i).getText().toString();
                        if(cant.isEmpty())
                            Toast.makeText(this, R.string.vacio, Toast.LENGTH_LONG).show();
                        else{
                            cantidad = Integer.parseInt(cant);

                            rei = new RecetaIngrediente(idReceta, idIngrediente, cantidad);
                            gri.insert(rei);
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    } else if (arrayIngrediente.size() == 0) {
                        aux = new Ingrediente();
                        nombreIng=arrayIng.get(i).getText().toString();
                        if(nombreIng.isEmpty()){
                            Toast.makeText(this, R.string.vacio, Toast.LENGTH_LONG).show();
                        }else{
                            aux.setNombre(nombreIng);
                            idIngrediente = (int) gi.insert(aux);
                            String cadCant=arrayCant.get(i).getText().toString();
                            if(cadCant.isEmpty()){
                                Toast.makeText(this, R.string.vacio, Toast.LENGTH_LONG).show();

                            }else{
                                cantidad = Integer.parseInt(cadCant);
                                rei = new RecetaIngrediente(idReceta, idIngrediente, cantidad);
                                gri.insert(rei);
                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                        }
                    }
                }
            }
        }
    }

    //Recorro la lista de todas las recetasingredientes ligadas a la receta y las borro
    public void borrarRecetasIngredientes(){
        List<RecetaIngrediente> borrarRI = gri.select("IDRECETA = " + idReceta, null);
        for (RecetaIngrediente aux: borrarRI) {
            gri.delete(aux);
        }
    }

    /*****************************Acciones de los botones (OnClick)********************************/
    //Para añadir nuevos campos de ingredientes y cantidad
    public void nuevoIngre(View v){
        EditText et=new EditText(this);
        EditText et2=new EditText(this);
        ViewHolder vh = new ViewHolder();
        et2.setInputType(2);
        et.setHint(R.string.hIng);
        et2.setHint(R.string.hCant);
        LinearLayout ll1 = (LinearLayout)findViewById(R.id.LLCambiaIng);
        vh.LLI=ll1;
        LinearLayout ll2 = (LinearLayout)findViewById(R.id.LLCambiaCant);
        vh.LLC=ll2;
        arrayIng.add(et);
        arrayCant.add(et2);

        ViewGroup.LayoutParams parametros = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        vh.LLI.addView(et, parametros);

        vh.LLC.addView(et2, parametros);
    }

    //Para cambiar la foto
    public void foto(View v){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        ViewHolder vh=new ViewHolder();
        ImageView iv=(ImageView)findViewById(R.id.ivCambiaImagen);
        vh.iv=iv;
        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(
                            selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    ruta=filePath;

                    File imgFile = new  File(filePath);
                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        vh.iv.setImageBitmap(myBitmap);
                    }
                }
        }
    }


}
