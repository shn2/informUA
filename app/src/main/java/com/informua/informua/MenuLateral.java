package com.informua.informua;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.github.florent37.viewanimator.ViewAnimator;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lusfold.spinnerloading.SpinnerLoading;

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

public class MenuLateral extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    JSONArray resultado;
    ListView listaPosts;
    Activity actividad;
    EditText texto;
    Button enviarpost;
    RelativeLayout modal;
    int intPost=0;
    RelativeLayout postearrel;
    String idUsuarioLogeado ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_lateral);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Context context = getApplicationContext();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        actividad=this;
        listaPosts=(ListView)findViewById(R.id.listaPosts);
        modal = (RelativeLayout) findViewById(R.id.Modal);
        postearrel=(RelativeLayout) findViewById(R.id.postearrel);
        texto=(EditText) findViewById(R.id.TextoNuevoPost);
        enviarpost=(Button) findViewById(R.id.EnviarPost);
        idUsuarioLogeado=getIntent().getExtras().getString("id");
        final SpinnerLoading view = (SpinnerLoading) findViewById(R.id.spinner_loading1);
        view.setVisibility(View.GONE);
        enviarpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                ViewAnimator.animate(postearrel).duration(400).fadeOut().start();
               ViewAnimator.animate(fab).rotation(0).duration(200).start();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(texto.getWindowToken(), 0);
                postearrel.setVisibility(View.INVISIBLE);
                modal.setVisibility(View.VISIBLE);
            }
        });
       fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (postearrel.getVisibility() == View.VISIBLE) {

                    ViewAnimator.animate(postearrel).duration(400).fadeOut().start();
                    ViewAnimator.animate(fab).rotation(0).duration(200).start();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(texto.getWindowToken(), 0);
                    postearrel.setVisibility(View.INVISIBLE);
                } else {
                    postearrel.setVisibility(View.VISIBLE);
                    ViewAnimator.animate(postearrel).duration(400).fadeIn().start();
                    ViewAnimator.animate(fab).rotation(45).duration(200).start();
                }

            }
        });
        postearrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewAnimator.animate(postearrel).duration(400).fadeOut().start();
                ViewAnimator.animate(fab).rotation(0).duration(20).start();InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(texto.getWindowToken(), 0);

                postearrel.setVisibility(View.INVISIBLE);
            }
        });







        listaPosts.setLongClickable(true);
        listaPosts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                if(pos!=0) {

                    final TextView v2 = ((TextView) ((ViewGroup) ((ViewGroup) (((ViewGroup) ((ViewGroup) arg1).getChildAt(0)).getChildAt(0))).getChildAt(2)).getChildAt(0));

                    String c = v2.getText().toString();
                    int caca = Integer.parseInt(c) + 1;
                    String cacas = "" + caca;
                    SmallBang mSmallBang = SmallBang.attach2Window(actividad);
                    mSmallBang.bang(v2);
                    v2.setText(cacas);

                    final View v = ((ViewGroup) ((ViewGroup) arg1).getChildAt(0)).getChildAt(1);
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
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers,
                                                              byte[] responseBody, Throwable error) {
                                        }
                                    }

                            );
                        }
                    });
                }return true;
            }
        });




        

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(texto.getWindowToken(), 0);
        setSupportActionBar(toolbar);
        obtenerPosts("all");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        listaPosts.setClickable(true);

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               System.out.println("EEEEEEEEEE: " + position);
                if(position!=0) {
                    final TextView v2 = ((TextView) (((ViewGroup) (((ViewGroup) ((ViewGroup) view).getChildAt(0)).getChildAt(0))).getChildAt(1)));


                    System.out.println("holaaaaa");
                    Intent intent = new Intent(actividad, comentarios.class);
                    intent.putExtra("id", id);
                    intent.putExtra("texto", v2.getText());
                    intent.putExtra("idUsuarioLogeado", idUsuarioLogeado);
                    startActivity(intent);
                }
            }
        };
        listaPosts.setOnItemClickListener(listener);
    }

    private void obtenerPosts(final String algo){
       
        final Context c = getApplicationContext();
        final SpinnerLoading view = (SpinnerLoading) findViewById(R.id.spinner_loading1);
        view.setVisibility(View.VISIBLE);
        AsyncHttpClient client =new AsyncHttpClient();
        client.get("http://vps222360.ovh.net/posts/VerPosts.php?category=" + algo, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray result) {
                adaptadorPosts adapter = new adaptadorPosts(actividad, result, c, algo);
                view.setVisibility(View.GONE);
                listaPosts.setAdapter(adapter);
               // listaPosts.addHeaderView(new TextView(actividad));
              //  ((TextView) listaPosts.getItemAtPosition(0)).setText("Others");

            }
        });
        view.setVisibility(View.GONE);
    }


    public void crearPost(View v){
        RequestParams params = new RequestParams();
        params.put("texto", texto.getText());
        params.put("categoria", v.getTag());
        params.put("creador", idUsuarioLogeado);
        System.out.println("EEEEEEEEO "+idUsuarioLogeado);
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
          //  ((TextView) listaPosts.getItemAtPosition(0)).setText("Todos los posts");

        } else if (id == R.id.nav_sports) {
            obtenerPosts("sports");
          //  ((TextView) listaPosts.getItemAtPosition(0)).setText("Deportes");
        } else if (id == R.id.nav_love) {
            obtenerPosts("love");
          //  ((TextView) listaPosts.getItemAtPosition(0)).setText("Love");

        } else if (id == R.id.nav_party) {
            obtenerPosts("party");
            //((TextView) listaPosts.getItemAtPosition(0)).setText("Party");

        } else if (id == R.id.nav_study) {
            obtenerPosts("study");
            //((TextView) listaPosts.getItemAtPosition(0)).setText("Study");

        } else if (id == R.id.nav_music) {
            obtenerPosts("music");
          //  ((TextView) listaPosts.getItemAtPosition(0)).setText("Music");

        } else if (id == R.id.nav_games) {
            obtenerPosts("games");
            //((TextView) listaPosts.getItemAtPosition(0)).setText("Games");

        } else if (id == R.id.nav_others) {
            obtenerPosts("others");


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
/*<android.support.design.widget.FloatingActionButton
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_margin="@dimen/fab_margin"
        android:src="@drawable/add"
        android:layout_alignParentEnd="false"
        android:layout_alignParentStart="false"
        android:layout_alignParentBottom="true"
        android:layout_alignParentRight="true" />*/