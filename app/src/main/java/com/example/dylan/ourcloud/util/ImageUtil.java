package com.example.dylan.ourcloud.util;

import android.content.Context;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by dylan on 8/10/15.
 */
public class ImageUtil {

    public interface ImageCallback {
        public void imageUploaded(int status,String url);
    }

    private static ImageUtil instance = null;
    private Context context;

    private OkHttpClient httpClient;

    public static ImageUtil getInstance( Context context ) {
        if (instance == null) {
            instance = new ImageUtil(context);
        }
        return instance;
    }

    public ImageUtil( Context context ) {
        this.context = context;
        httpClient = new OkHttpClient();
    }

    public void uploadImage(Context context,final int status,File postImage) {
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
                callback.imageUploaded(status,response.body().string().trim());
            }
        });

    }

    public File newImageFile() throws IOException {
        Random r = new Random();
        r.nextInt(1000000);

        String fileName = "JPEG" + r.nextInt();
        //File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        storageDirectory.mkdirs();

        File image = new File(storageDirectory, fileName + ".jpg");

        image.createNewFile();

        return image;
    }



}
