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
import android.view.Window;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Main2Activity extends Activity {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Activity view;
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
            Intent intent = new Intent(view, MenuLateral.class);
            startActivity(intent);
        }



        ///////////////////////////////////////////////////////////////////////////////////////////
        /////     Cuando se le de al botón de login de facebook        ///////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeo");
                loginResult.getAccessToken();
                //Se deberia hacer una llamada a la api guardando el token que da face y desde el servidor hacer cosas con php

                //Abrir Main activity
                Intent intent = new Intent(view, MenuLateral.class);
                startActivity(intent);
                /////////////////////////////

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
