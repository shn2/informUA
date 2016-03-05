package com.informua.informua;

/**
 * Created by jorge on 05/03/2016.
 */

import android.app.Activity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
        try {
            return resultado.get(position);
        } catch (JSONException e) {
            System.out.println("EEeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeerrrorrrr!");
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        try {
            return  resultado.get(position).hashCode();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.posts, null, true);

        TextView fecha = (TextView) rowView.findViewById(R.id.fecha);
        TextView texto = (TextView) rowView.findViewById(R.id.texto);
        TextView megusta = (TextView) rowView.findViewById(R.id.megustas);
        TextView categoria = (TextView) rowView.findViewById(R.id.categoria);
        try {
            JSONObject json_data = resultado.getJSONObject(position);
            fecha.setText(  json_data.getString("fecha"));
            texto.setText(  json_data.getString("texto"));
            megusta.setText(  json_data.getString("like"));
            categoria.setText(  json_data.getString("id_categoria"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rowView;

    };
}