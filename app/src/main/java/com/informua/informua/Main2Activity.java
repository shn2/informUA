package com.informua.informua;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cz.msebera.android.httpclient.Header;


public class Main2Activity extends Activity {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Activity view;
    String usuarioId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        view=this;
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main2);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);


        ////////////////////////////////////////////////////////////////////////////////////////////
        /////      Sacar claves hash que hay que incluir en face         ///////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.informua.informua",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHKeyH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }



        ///////////////////////////////////////////////////////////////////////////////////////////
        //Si ya está loggeado en face va a a la actividad 2 directamente
        ///////////////////////////////////////////////////////////////////////////////////////////
        if(AccessToken.getCurrentAccessToken()!=null){
             AccessToken accessToken= AccessToken.getCurrentAccessToken();
            System.out.println("EEEEEEEEO "+accessToken.getUserId());
            System.out.println("EEEEEEEEO "+accessToken.getToken());
            AsyncHttpClient client =new AsyncHttpClient();
            RequestParams params= new RequestParams();;
            params.put("id",accessToken.getUserId());
            params.put("token", accessToken.getToken());
            usuarioId=accessToken.getUserId();
            client.post("http://vps222360.ovh.net/usuarios/CrearUsuario.php", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    System.out.println("EEEEEEO Crearusuario.php bien eh");


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    System.out.println("EEEEEEEEO " + responseBody);
                }
            });
            Intent intent = new Intent(view, MenuLateral.class);
            intent.putExtra("id", usuarioId);
            System.out.println("EEEEEEEEO "+usuarioId);
            startActivity(intent);
        }



        ///////////////////////////////////////////////////////////////////////////////////////////
        /////     Cuando se le de al botón de login de facebook        ///////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginResult.getAccessToken();
                AccessToken accessToken=loginResult.getAccessToken();
                AsyncHttpClient client =new AsyncHttpClient();
                RequestParams params= new RequestParams();;
                params.put("id",accessToken.getUserId());
                usuarioId=accessToken.getUserId();
                params.put("token", accessToken.getToken());
                client.post("http://vps222360.ovh.net/usuarios/CrearUsuario.php", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                        System.out.println("EEEEEEO Crearusuario.php bien eh");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        System.out.println("EEEEEEEEO "+responseBody);
                    }
                });
                Intent intent = new Intent(view, MenuLateral.class);
                intent.putExtra("id", usuarioId);
                startActivity(intent);
            }

            @Override
            public void onCancel() {

            }
            @Override
            public void onError(FacebookException e) {
                System.out.println("maaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaal");
            }
        });
    }



    //Se supone que esto se ejecuta cuando volvamos a esta actividad
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }




}
