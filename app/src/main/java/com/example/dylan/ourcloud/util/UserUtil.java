package com.example.dylan.ourcloud.util;

import com.example.dylan.ourcloud.SignInController;
import com.google.android.gms.plus.model.people.Person;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by dylan on 8/16/15.
 */
public class UserUtil {
    /**
     * This method makes a request to the server to a script that checks if this user is saved in the `users` table, if not, saves them
     */


    public static void userSignIn(final SignInController.UICallback callback,final Person person) {
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), JSONUtil.generateJSONArray(person.getId(), person.getDisplayName(), person.getImage().getUrl()));
        Request request = new Request.Builder()
                .post(body)
                .url("http://104.236.15.47/OurCloudAPI/index.php/userSignin")
                .build();
        Call newCall = httpClient.newCall(request);
        newCall.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                callback.signInSuccess(person);
            }
        });
    }



}
