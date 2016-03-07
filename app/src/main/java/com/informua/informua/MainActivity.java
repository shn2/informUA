package com.informua.informua;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    JSONArray resultado;
    ListView listaPosts;
    Activity actividad;
    AsyncHttpClient client;
    EditText texto;
    Button enviarpost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listaPosts=(ListView)findViewById(R.id.listaPosts);
        actividad=this;
        texto=(EditText) findViewById(R.id.TextoNuevoPost);
        enviarpost=(Button) findViewById(R.id.EnviarPost);
        enviarpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearPost();
            }
        });
        obtenerPosts();
    }


    private void obtenerPosts(){
        client.get("http://vps222360.ovh.net/posts/VerPosts.php", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray result) {
                adaptadorPosts adapter = new adaptadorPosts(actividad, result);
                listaPosts.setAdapter(adapter);
            }
        });
    }


    private void crearPost(){
        RequestParams params = new RequestParams();
        params.put("texto", texto.getText());
        params.put("categoria", "1");
        client.post("http://vps222360.ovh.net/posts/CrearPost.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                texto.setText("");
                obtenerPosts();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }
}
