package com.informua.informua;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.Header;
import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

public class MenuLateral extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    JSONArray resultado;
    ListView listaPosts;
    Activity actividad;
    EditText texto;
    Button enviarpost;
    RelativeLayout modal;
    int intPost=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_lateral);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Context context = getApplicationContext();
        listaPosts=(ListView)findViewById(R.id.listaPosts);
        modal = (RelativeLayout) findViewById(R.id.Modal);
        actividad=this;
        texto=(EditText) findViewById(R.id.TextoNuevoPost);
        texto.clearFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        enviarpost=(Button) findViewById(R.id.EnviarPost);
        enviarpost
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                        modal.setVisibility(View.VISIBLE);
                        //crearPost();
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


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(texto.getWindowToken(), 0);
    }

    private void obtenerPosts(String algo){
        final SmallBang mSmallBang = SmallBang.attach2Window(this);
        final Context c = getApplicationContext();

        AsyncHttpClient client =new AsyncHttpClient();
        client.get("http://vps222360.ovh.net/posts/VerPosts.php?category=" + algo, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray result) {
                adaptadorPosts adapter = new adaptadorPosts(actividad, result, c);
                listaPosts.setAdapter(adapter);
                listaPosts.setLongClickable(true);
                listaPosts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                   int pos, long id) {
                        // TODO Auto-generated method stub
                        System.out.println("eeeeeoooooooooooooooooo");
                        //  ((ViewGroup) arg1).getChildAt(1).setVisibility(View.VISIBLE);
                        int childcount = ((ViewGroup) arg1).getChildCount();
                        // for (int i=0; i < childcount; i++){
                        final View v = ((ViewGroup) ((ViewGroup) arg1).getParent()).getChildAt(1);

                        final String idPost = v.getTag().toString();
                        v.setVisibility(View.VISIBLE);


                        mSmallBang.bang(v, 180, new SmallBangListener() {
                            @Override
                            public void onAnimationStart() {
                                v.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd() {
                                v.setVisibility(View.INVISIBLE);
                                AsyncHttpClient client = new AsyncHttpClient();
                                client.get("http://vps222360.ovh.net/posts/Megusta.php?id=" + idPost, null, new AsyncHttpResponseHandler() {

                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                                texto.setText("");
                                                obtenerPosts("all");
                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers,
                                                                  byte[] responseBody, Throwable error) {
                                            }
                                        }

                                );
                            }
                        });
                        System.out.println("dlkflasdkfhlakjsfdi!" + v.toString());
                        //}

                        return true;
                    }
                });


            }
        });
    }


    public void crearPost(View v){
        RequestParams params = new RequestParams();
        params.put("texto", texto.getText());
        params.put("categoria", v.getTag());
        AsyncHttpClient client =new AsyncHttpClient();
        modal.setVisibility(View.GONE);
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

    public void meGusta(View v){

        AsyncHttpClient client =new AsyncHttpClient();
        client.post("http://vps222360.ovh.net/posts/Megusta.php", new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                texto.setText("");
                //obtenerPosts("all");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    public void quitarModal(View v){
        modal.setVisibility(View.GONE);
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

    //==============================================================================================
    //==============================================================================================



    public void obtenerId(View miView) {
        String idPost=miView.getTag().toString();
        AsyncHttpClient client =new AsyncHttpClient();
        client.get("http://vps222360.ovh.net/posts/Megusta.php?id=" + idPost, null, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess ( int statusCode, Header[] headers,byte[] response){
                        texto.setText("");
                        obtenerPosts("all");
                    }

                    @Override
                    public void onFailure ( int statusCode, Header[] headers,
                                            byte[] responseBody, Throwable error){
                    }
                }

        );

    }





}
