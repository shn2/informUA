package com.informua.informua;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

public class comentarios extends AppCompatActivity {
    ListView listacomentarios;
    TextView textoPost;
    Activity actividad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        textoPost=(TextView) findViewById(R.id.post_Text);
        setSupportActionBar(toolbar);
        actividad = this;
       Long id= getIntent().getExtras().getLong("id");
        String texto=getIntent().getExtras().getString("texto");
        listacomentarios=(ListView)findViewById(R.id.listacomentarios);
        obtenerComentarios(id.toString());
        System.out.println("Cuentaaaa " + listacomentarios.getChildCount());
        textoPost.setText(texto);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void obtenerComentarios(String algo){

        final Context c = getApplicationContext();

        AsyncHttpClient client =new AsyncHttpClient();
        client.get("http://vps222360.ovh.net/comentarios/VerComentariosPosts.php?post=" + algo, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray result) {

                adaptadorComentarios adapter = new adaptadorComentarios(actividad, result);
                listacomentarios.setAdapter(adapter);
                System.out.println("EEEEEEEEEEEE");
                System.out.println("Cuentaaaa "+ listacomentarios.getChildCount());
            }


        });
    }


}
