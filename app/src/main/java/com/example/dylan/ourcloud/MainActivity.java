package com.example.dylan.ourcloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dylan.ourcloud.home_zone.ThisZone;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.plus.model.people.Person;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,SignInController.UICallback {

    private SignInController signInController;
    private WifiController wifiController;

    LinearLayout loadContainer;
    RelativeLayout signinContainer;
    SignInButton signinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById( R.id.toolbarTitle );
        toolbarTitle.setText("OurCloud");

        loadContainer = (LinearLayout) findViewById(R.id.loadingContainer);
        signinContainer = (RelativeLayout) findViewById(R.id.loginContainer);
        signinButton = (SignInButton) findViewById(R.id.googleSignIn);

        signinButton.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();

        wifiController = new WifiController(this);

        signInController = new SignInController(this);
        signInController.attemptSignIn();

    }

    @Override
    public void signInSuccess(Person currentUser) {
        LocalUser.getInstance(this).userSignIn(currentUser);

        startService(new Intent(this, GcmTokenGrab.class));
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                LocalUser.getInstance(MainActivity.this).setGcmId(intent.getStringExtra("gcmId"));

                new UserSignIn().execute();
                

                LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(this);
            }
        },new IntentFilter(GcmTokenGrab.RECEIVE_GCM_TOKEN));

        /**
         * Need to create class for wifi manager, which has a callback interface once the wifi accesspoint is established
         *
         * Call wifimanager method here
         * Once callback comes with wifi accesspoint ID, add it to currentUser with setWifiId() and call intent for home tab activity
         * Home tabs: "This Zone","Marked Zones","My Posts"
         */
    }

    @Override
    public void inflateResolution(ConnectionResult conRes) {
        try {
            conRes.startResolutionForResult(this,1);
        } catch (IntentSender.SendIntentException e)  {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void showLoginScreen() {
        loadContainer.setVisibility(View.GONE);
        signinContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int request,int result,Intent data) {
        if (request == 1) {
            signInController.attemptSignIn();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.googleSignIn :
                signInController.attemptSignIn();
                break;
        }
    }

    public class UserSignIn extends AsyncTask<Void,Void,Void> {

        @Override
        public Void doInBackground( Void... params ) {
            LocalUser local = LocalUser.getInstance( MainActivity.this );

            JSONArray items = new JSONArray();
            items.put( local.getItem( LocalUserDBHelper.user_id_col ) );
            items.put( local.getItem( LocalUserDBHelper.gcm_id_col ) );
            items.put( local.getItem( LocalUserDBHelper.nameCol ) );
            items.put( local.getItem( LocalUserDBHelper.profile_image_col ) );


            OkHttpClient http = new OkHttpClient();
            RequestBody body = RequestBody.create( MediaType.parse("text/plain"), items.toString() );
            Request r = new Request.Builder()
                    .post( body )
                    .url("http://104.236.15.47/OurCloudAPI/index.php/userSignin")
                    .build();

            try {

                http.newCall( r ).execute();

                Intent i = new Intent(MainActivity.this, ThisZone.class);
                startActivity(i);
                MainActivity.this.finish();

            } catch( IOException e ) {
                e.getMessage();
            }

            return null;
        }

    }


}
