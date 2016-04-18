package com.informua.informua;

/**
 * Created by jorge on 05/03/2016.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
        import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class adaptadorPosts extends BaseAdapter{

    private final Activity context;
    private final JSONArray resultado;

    public adaptadorPosts(Activity context, JSONArray resultado) {
        this.context=context;
        this.resultado=resultado;
    }

    @Override
    public int getCount() {
        return resultado.length();
    }

    @Override
    public Object getItem(int position) {
        if(position>0){


        try {
            return resultado.get(position);
            } catch (JSONException e) {
            }
        }else {
            try {
                return resultado.get(0);
            } catch (JSONException e) {
            }
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if(position>0){  try {
            return  resultado.get(position).hashCode();
        } catch (JSONException e) {
            e.printStackTrace();
        }}
        return 0;
    }

    public View getView(int position,View view,ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.posts, null, true);

            TextView fecha = (TextView) rowView.findViewById(R.id.fecha);
            TextView texto = (TextView) rowView.findViewById(R.id.texto);
            TextView megusta = (TextView) rowView.findViewById(R.id.megustas);
            ImageView categoria_img=(ImageView) rowView.findViewById(R.id.icono_categoria);
            ImageButton botonLike=(ImageButton)rowView.findViewById(R.id.botonLike);

           // TextView categoria = (TextView) rowView.findViewById(R.id.categoria);
            try {
                JSONObject json_data = resultado.getJSONObject(position);
                fecha.setText(json_data.getString("fecha").split(" ")[0]);
                texto.setText(json_data.getString("texto"));
                megusta.setText(json_data.getString("like"));
                String categoria=json_data.getString("id_categoria");
                botonLike.setTag(json_data.getString("id"));
                System.out.println(categoria);
                if(categoria.equals("1")){
                    categoria_img.setImageDrawable(context.getResources().getDrawable(R.drawable.heart));
                    System.out.println("holaaaaaaaaaaa");
                }
                if(categoria.equals("2"))
                    categoria_img.setImageDrawable(context.getResources().getDrawable(R.drawable.music));
                if(categoria.equals("3"))
                    categoria_img.setImageDrawable(context.getResources().getDrawable(R.drawable.com_facebook_tooltip_black_bottomnub));
                if(categoria.equals("4"))
                    categoria_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ball));
                if(categoria.equals("5"))
                    categoria_img.setImageDrawable(context.getResources().getDrawable(R.drawable.game));
                if(categoria.equals("6"))
                    categoria_img.setImageDrawable(context.getResources().getDrawable(R.drawable.party));
                if(categoria.equals("7"))
                    categoria_img.setImageDrawable(context.getResources().getDrawable(R.drawable.book));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return rowView;

    };


}