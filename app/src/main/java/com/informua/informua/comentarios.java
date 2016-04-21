package com.informua.informua;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.github.florent37.viewanimator.ViewAnimator;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lusfold.spinnerloading.SpinnerLoading;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

public class comentarios extends AppCompatActivity {
    ListView listacomentarios;
    TextView textoPost;
    Activity actividad;
    String idUsuarioLogeado;
    ImageButton enviarComentario;
    EditText textoComentario;
    Long id;
    String texto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        textoPost=(TextView) findViewById(R.id.post_Text);
        setSupportActionBar(toolbar);
        actividad = this;
        id= getIntent().getExtras().getLong("id");
         texto=getIntent().getExtras().getString("texto");
        idUsuarioLogeado=getIntent().getExtras().getString("idUsuarioLogeado");
        listacomentarios=(ListView)findViewById(R.id.listacomentarios);
        obtenerComentarios(id.toString());
        System.out.println("Cuentaaaa " + listacomentarios.getChildCount());
        textoPost.setText(texto);
        enviarComentario = (ImageButton) findViewById(R.id.EnviarComentario);
        textoComentario=(EditText) findViewById(R.id.TextoEnviarComentario);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        enviarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("eoieoe:"+0);
                RequestParams params = new RequestParams();
                params.put("texto", textoComentario.getText());
                params.put("post",id.toString());
                params.put("id", idUsuarioLogeado);
                AsyncHttpClient client =new AsyncHttpClient();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textoComentario.getWindowToken(), 0);
                client.post("http://vps222360.ovh.net/comentarios/CrearComentario.php", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        textoComentario.setText("");
                        System.out.println("eoieoe:" + 2);
                        obtenerComentarios(id.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        System.out.println("eoieoe:"+1);
                    }
                });
            }
        });

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void obtenerComentarios(String algo){

        final Context c = getApplicationContext();
        final SpinnerLoading view = (SpinnerLoading) findViewById(R.id.spinner_loading);
        view.setVisibility(View.VISIBLE);


        AsyncHttpClient client =new AsyncHttpClient();
        client.get("http://vps222360.ovh.net/comentarios/VerComentariosPosts.php?post=" + algo, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray result) {
                view.setVisibility(View.GONE);
                adaptadorComentarios adapter = new adaptadorComentarios(actividad, result);
                listacomentarios.setAdapter(adapter);
                System.out.println("EEEEEEEEEEEE");
                System.out.println("Cuentaaaa "+ listacomentarios.getChildCount());
            }


        });
    }


}
