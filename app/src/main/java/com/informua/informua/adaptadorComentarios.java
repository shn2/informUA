package com.informua.informua;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jorge on 20/04/2016.
 */
public class adaptadorComentarios extends BaseAdapter {

    private final Activity context;
    private final JSONArray resultado;
    //final Context context = getApplicationContext();
    public adaptadorComentarios(Activity context, JSONArray resultado) {
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
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        JSONObject json_data = null;
        try {
            json_data = resultado.getJSONObject(position);
            return json_data.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.comentario, null, true);
        try {
            JSONObject json_data = resultado.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rowView;
    }
}
