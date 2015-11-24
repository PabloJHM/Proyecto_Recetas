package com.example.pablo.proyecto_recetas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pablo.proyecto_recetas.receta.Receta;

import java.io.File;
import java.util.List;


public class Adaptador extends ArrayAdapter<Receta> {
    List<Receta> aux;
    private Context ctx;
    private LayoutInflater i;
    int res;

    public Adaptador(Context context, int resource, List<Receta> aux) {
        super(context, resource, aux);
        this.aux = aux;
        this.res = resource;
        this.ctx=context;
        i = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    class ViewHolder {
        public TextView tv1, tv2;
        public ImageView iv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = new ViewHolder();

        if (convertView == null) {
            convertView = i.inflate(res,null);
            TextView tv = (TextView) convertView.findViewById(R.id.tvNombre);
            vh.tv1 = tv;
            tv = (TextView) convertView.findViewById(R.id.tvDesc);
            vh.tv2 = tv;
            ImageView iv =(ImageView) convertView.findViewById(R.id.ivFoto);
            vh.iv=iv;
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder)convertView.getTag();
        }
        vh.tv1.setText(aux.get(position).getNombre());
        vh.tv2.setText(aux.get(position).getInstrucciones());
        vh.iv.setId(position);
        String ruta;
        //Si no tuviese foto, le añadiria una por defecto
        try{
            ruta=aux.get(position).getFoto();
        }catch(Exception e){
            ruta=""+R.mipmap.ic_launcher;
        }

        File imgf= new File(ruta);
        if(imgf.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgf.getAbsolutePath());
            vh.iv.setImageBitmap(myBitmap);
        }
        return convertView;
    }
}