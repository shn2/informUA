package com.informua.informua;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

public class MenuLateral extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    JSONArray resultado;
    ListView listaPosts;
    Activity actividad;
    EditText texto;
    Button enviarpost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_lateral);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaPosts=(ListView)findViewById(R.id.listaPosts);
        actividad=this;
        texto=(EditText) findViewById(R.id.TextoNuevoPost);
        texto.clearFocus();
        enviarpost=(Button) findViewById(R.id.EnviarPost);
        enviarpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearPost();
            }
        });
        obtenerPosts("all");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void obtenerPosts(String algo){
        AsyncHttpClient client =new AsyncHttpClient();
        client.get("http://vps222360.ovh.net/posts/VerPosts.php?category="+algo, null, new JsonHttpResponseHandler() {
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
        AsyncHttpClient client =new AsyncHttpClient();
        client.post("http://vps222360.ovh.net/posts/CrearPost.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                texto.setText("");
                obtenerPosts("all");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lateral, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_all) {
            obtenerPosts("all");
        } else if (id == R.id.nav_sports) {
            obtenerPosts("sports");
        } else if (id == R.id.nav_love) {
            obtenerPosts("love");
        } else if (id == R.id.nav_party) {
            obtenerPosts("party");
        } else if (id == R.id.nav_study) {
            obtenerPosts("study");
        } else if (id == R.id.nav_music) {
            obtenerPosts("music");
        } else if (id == R.id.nav_games) {
            obtenerPosts("games");
        } else if (id == R.id.nav_others) {
            obtenerPosts("others");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
