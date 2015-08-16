package com.example.dylan.ourcloud;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

/**
 * Created by drose379 on 7/3/15.
 */


/**
 * Lifecycle of this controller
 * Constructor will be called, GoogleApiClient will be built

 * Sign in attempt will be made

 * If no user is signed in, it will fail, since flag is set to false, it will switch the flag to true, and send callback to UI thread to show Log-in screen

 * Once Log-In screen is showing, controller waits for google sign in button to be clicked, once its clicked, another attempt at sign in is made
 * It will fail, but since the flag is not set to true, callback will be made to UI thread to show account picker
 * onActiivtyResult will be called when user picks an account, attemptSignIn() method called from there, onConnected called in controller and callback to UI

 * If the user is already signed in, onConnected will be called and a callback will be sent to the UI thread to skip the Log-in screen
 */


public class SignInController implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks {

    public interface UICallback {
        public void signInSuccess(Person currentPerson);
        public void showLoginScreen();
        public void inflateResolution(ConnectionResult result);
    }


    private Context context;
    private UICallback uiCallback;

    private GoogleApiClient gClient;

    private boolean shouldInflateAccountPicker = false;

    public SignInController(Context context) {
        this.context = context;
        uiCallback = (UICallback) context;

        gClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        attemptSignIn();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Person currentPerson = Plus.PeopleApi.getCurrentPerson(gClient);
        if (currentPerson != null) {
            uiCallback.signInSuccess(currentPerson);
            UserUtil.userSignIn(currentPerson);
        } else {
            throw new RuntimeException("Could not log you in.");
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (shouldInflateAccountPicker) {
            uiCallback.inflateResolution(result);
        } else {
            //set should inflate flag to true, for next time the user will be clicking the log in button and it should inflate the account picker
            Log.i("gConnect","SHOULD NOT INFLATE RES, WAIT FOR CLICK...");
            uiCallback.showLoginScreen();
            shouldInflateAccountPicker = true;
        }
    }

    @Override
    public void onConnectionSuspended(int error) {

    }

    public void attemptSignIn() {
        Log.i("gConnect","ATTEMPT SIGN IN");
        gClient.connect();
    }

    public void disconnect() {
        gClient.disconnect();
    }


}