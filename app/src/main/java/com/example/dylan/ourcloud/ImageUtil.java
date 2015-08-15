package com.example.dylan.ourcloud;

import android.content.Context;
import android.database.Cursor;

import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

/**
 * Created by dylan on 8/10/15.
 */
public class ImageUtil {

    public interface ImageCallback {
        public void imageUploaded(int status,String url);
    }

    public static ImageUtil instance = null;

    private OkHttpClient httpClient;

    public static ImageUtil getInstance() {
        if (instance == null) {
            instance = new ImageUtil();
        }
        return instance;
    }

    public ImageUtil() {
        httpClient = new OkHttpClient();
    }

    public void uploadImage(Fragment context,final int status,File postImage) {

        final ImageCallback callback = (ImageCallback) context;

        MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpeg");
        RequestBody rBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("photo",postImage.getName(),RequestBody.create(MEDIA_TYPE_JPG,postImage))
                .build();
        Request request = new Request.Builder()
                .post(rBody)
                .url("http://104.236.15.47/OurCloudAPI/index.php/postImageUpload")
                .build();
        Call newCall = httpClient.newCall(request);
        newCall.enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                callback.imageUploaded(status,response.body().string());
            }
        });

    }



}
