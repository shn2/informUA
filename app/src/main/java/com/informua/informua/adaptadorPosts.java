package com.informua.informua;



import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class adaptadorPosts extends BaseAdapter{

    private final Activity context;
    private final JSONArray resultado;
    private  final Context c;
    //final Context context = getApplicationContext();
    public adaptadorPosts(Activity context, JSONArray resultado,  Context c) {
        this.context=context;
        this.resultado=resultado;
        this.c=c;
    }

    @Override
    public int getCount() {
        return resultado.length()+1;
    }

    @Override
    public Object getItem(int position) {
        if(position>0){


            try {
                return resultado.get(position+1);
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
        return 1;
    }

    public View getView(int position,View view,ViewGroup parent) {
        if (position == 0) {
            TextView t = new TextView(context);
            t.setText("Todos los posts");
            t.setTextSize(15);
            t.setPadding(10,20,10,15);
            return t;
        } else {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.posts, null, true);
            TextView fecha = (TextView) rowView.findViewById(R.id.fecha);
            TextView texto = (TextView) rowView.findViewById(R.id.texto);
            TextView megusta = (TextView) rowView.findViewById(R.id.megustas);
            ImageView categoria_img = (ImageView) rowView.findViewById(R.id.icono_categoria);
            RelativeLayout relativo = (RelativeLayout) rowView.findViewById(R.id.relativo);
            try {
                JSONObject json_data = resultado.getJSONObject(position-1);
                fecha.setText(json_data.getString("fecha").split(" ")[0]);
                texto.setText(json_data.getString("texto"));
                megusta.setText(json_data.getString("like"));
                String categoria = json_data.getString("id_categoria");
                //     botonLike.setTag(json_data.getString("id"));
                relativo.setTag(json_data.getInt("id"));
                System.out.println(categoria);
                if (categoria.equals("1")) {
                    categoria_img.setImageDrawable(context.getResources().getDrawable(R.drawable.heart));
                    System.out.println("holaaaaaaaaaaa");
                }
                if (categoria.equals("2"))
                    categoria_img.setImageDrawable(context.getResources().getDrawable(R.drawable.music));
                if (categoria.equals("3"))
                    categoria_img.setImageDrawable(context.getResources().getDrawable(R.drawable.com_facebook_tooltip_black_bottomnub));
                if (categoria.equals("4"))
                    categoria_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ball));
                if (categoria.equals("5"))
                    categoria_img.setImageDrawable(context.getResources().getDrawable(R.drawable.game));
                if (categoria.equals("6"))
                    categoria_img.setImageDrawable(context.getResources().getDrawable(R.drawable.party));
                if (categoria.equals("7"))
                    categoria_img.setImageDrawable(context.getResources().getDrawable(R.drawable.book));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return rowView;

        }

    }



}