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
import android.widget.Button;
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


public class Aniadir extends Activity{

    private EditText edNom,edIns;
    private Button ok;
    private Spinner sp;
    private GestorReceta gr;
    private GestorIngrediente gi;
    private GestorRecetaIngrediente gri;
    private RecetaIngrediente ri;
    private GestorCategoria gc;
    private Ayudante a;
    private LinearLayout LLIngre,LLCant;
    private List<EditText> arrayIng=new ArrayList<>(),arrayCant=new ArrayList<>();
    private String rutaFoto;
    private ImageView ivFoto;

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aniadir_receta);
        init();
    }

    public void init(){
        LLIngre=(LinearLayout)findViewById(R.id.LLing);
        LLCant=(LinearLayout)findViewById(R.id.LLcan);
        edNom=(EditText) findViewById(R.id.etNom);
        edIns=(EditText)findViewById(R.id.etIns);
        ivFoto=(ImageView)findViewById(R.id.ivFoto);
        rutaFoto=""+R.mipmap.ic_launcher;
        ok=(Button)findViewById(R.id.btOk);
        sp=(Spinner)findViewById(R.id.spCategoria);
        a=new Ayudante(this);
        gr=new GestorReceta(a);
        gri=new GestorRecetaIngrediente(a);
        gi=new GestorIngrediente(a);
        gc=new GestorCategoria(a);
        ri=new RecetaIngrediente();
    }

    /********************************Accion de añadir receta***************************************/

    public void aceptar(View v){
        Receta r = new Receta();
        int idReceta=-1;
        boolean añadir=true;
        String nombre, inst, nombreIng;
        long idIngrediente;
        int cantidad;
        nombre=edNom.getText().toString();
        inst=edIns.getText().toString();

        //Comprobamos que no deja el nombre o las instrucciones vacias
        if(nombre.isEmpty() || inst.isEmpty()) {
            Toast.makeText(this, R.string.vacio, Toast.LENGTH_LONG).show();
        }else if(arrayIng.size()==0 || arrayCant.size()==0) {
            //Comprobamos que el usuario ha añadido minimo 1 ingrediente
            Toast.makeText(this, R.string.ingredienteVacio, Toast.LENGTH_LONG).show();
        }else{
            //Primero creamos la receta
            r.setNombre(nombre);
            r.setInstrucciones(inst);
            r.setFoto(rutaFoto);
            String nomCat = sp.getSelectedItem().toString();
            r.setIdCat(gc.select("NOMBRE = '" + nomCat + "'", null).get(0).getId());

            //Ahora, por cada ingrediente, comprobamos que no lo deje vacio. Si existe ya ese
            //ingrediente no es añadido a la tabla de ingredientes. Si es uno nuevo es añadido
            for (int i = 0; i < arrayIng.size(); i++) {
                nombreIng=arrayIng.get(i).getText().toString();
                String cantIng=arrayCant.get(i).getText().toString();
                if(nombreIng.isEmpty() || cantIng.isEmpty()) {
                    Toast.makeText(this, R.string.vacio, Toast.LENGTH_LONG).show();
                }else{
                    List<Ingrediente> arrayIngrediente = gi.select("NOMBRE = '" + nombreIng + "'", null);
                    //Si ya existe en la base de datos, lo cojemos de la tabla.
                    if (arrayIngrediente.size() > 0) {
                        idIngrediente = arrayIngrediente.get(0).getId();
                        String cant=arrayCant.get(i).getText().toString();
                        //Comprobamos que no deja el campo de cantidad vacio
                        if(cant.isEmpty()) {
                            Toast.makeText(this, R.string.vacio, Toast.LENGTH_LONG).show();
                        }else{
                            cantidad = Integer.parseInt(cant);
                            if(añadir) {
                                añadir=false;
                                idReceta = (int) gr.insert(r);
                            }
                            ri = new RecetaIngrediente(idReceta, idIngrediente, cantidad);
                            gri.insert(ri);
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    //Si el ingrediente no existe en la base de datos lo añadimos
                    } else {
                        Ingrediente aux = new Ingrediente();
                        nombreIng=arrayIng.get(i).getText().toString();
                        if(nombreIng.isEmpty() || cantIng.isEmpty()){
                            Toast.makeText(this, R.string.vacio, Toast.LENGTH_LONG).show();
                        }else{
                            aux.setNombre(nombreIng);
                            idIngrediente = (int) gi.insert(aux);
                            cantIng=arrayCant.get(i).getText().toString();
                            if(cantIng.isEmpty() || nombreIng.isEmpty()){
                                Toast.makeText(this, R.string.vacio, Toast.LENGTH_LONG).show();
                            }else{
                                cantidad = Integer.parseInt(cantIng);
                                if(añadir) {
                                    añadir=false;
                                    idReceta = (int) gr.insert(r);
                                }
                                ri = new RecetaIngrediente(idReceta, idIngrediente, cantidad);
                                gri.insert(ri);
                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                        }
                    }
                }
            }
        }
    }

    /**********************Añadir nuevos campos de ingredientes y cantidad*************************/

    public void nuevoIngre(View v){
        EditText et=new EditText(this);
        EditText et2=new EditText(this);
        et2.setInputType(2);
        et.setHint(R.string.hIng);
        et2.setHint(R.string.hCant);

        arrayIng.add(et);
        arrayCant.add(et2);
        ViewGroup.LayoutParams parametros = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        LLIngre.addView(et,parametros);
        LLCant.addView(et2, parametros);
    }

    /***********************Metodos para obtener la ruta de las fotos******************************/

    public void foto(View v){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

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

                    rutaFoto=filePath;

                    File imgFile = new  File(filePath);
                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        ivFoto.setImageBitmap(myBitmap);
                    }
                }
        }
    }
}

