package com.example.dylan.ourcloud;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import com.example.dylan.ourcloud.util.ImageUtil;

import com.github.clans.fab.FloatingActionButton;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by dylan on 8/12/15.
 */
public class PostComposeActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PHOTO_FROM_CAMERA = 1;
    public static final int PHOTO_FROM_GALLERY = 2;

    public static final int POST_TEXT_ONLY = 1;
    public static final int POST_BOTH = 2;
    public static final int POST_PHOTO_ONLY = 3;

    EditText postTextArea;

    ImageView selectImageIcon;
    ImageView uploadedImageContainer;

    private MaterialDialog photoSourceSelect;
    private MaterialDialog expDateDialog;
    private Uri finalImageUri;

    private File postImage;

    private int photoSource;

    @Override
    public void onCreate(Bundle savedInstance) {

        Log.i("onCreate","Activity restarted");

        super.onCreate(savedInstance);
        postImage = savedInstance != null ? (File)savedInstance.getSerializable("postImage") : null;

        setContentView(R.layout.post_compose);
        initDialogs();



        /**
         * Show current users photo and their name in header at top of layout (darker background then rest of layout)
         * Set listeners for image select button and done button
         * Have image select container be 300 dp high and match_parent width, with padding of 20 dp
         * When user selects, show loading dialog, when post is submitted successfully, close this activiy with this.finish()
          */

        TextView userName = (TextView) findViewById(R.id.userName);
        ImageView userImage = (ImageView) findViewById(R.id.userImage);
        FloatingActionButton postSubmitButton = (FloatingActionButton) findViewById(R.id.postSubmitButton);
        postTextArea = (EditText) findViewById(R.id.postTextArea);
        selectImageIcon = (ImageView) findViewById(R.id.photoAddButton);
        uploadedImageContainer = (ImageView) findViewById(R.id.selectedImageContainer);


        userName.setText(UserInfo.getInstance().getDisplayName());
        Picasso.with(this).load(UserInfo.getInstance().getProfileImageSized(90)).into(userImage);

        selectImageIcon.setOnClickListener(this);
        postSubmitButton.setOnClickListener(this);


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstance) {
        savedInstance.putSerializable("postImage", postImage);
        super.onSaveInstanceState(savedInstance);
    }

    @Override
    public void onStart() {
        super.onStart();
        finalImageUri = Uri.fromFile(new File(getFilesDir(), "cropped"));

    }

    public void initDialogs() {
        photoSourceSelect = new MaterialDialog.Builder(this)
                .title("Choose Location")
                .items(new String[]{"Camera","Gallery"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        switch (i) {
                            case 0:
                                try {
                                    postImage = ImageUtil.newImageFile();
                                    Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    takePhoto.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(postImage));
                                    startActivityForResult(takePhoto,1);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                break;
                            case 1:
                                Intent imagePicker = new Intent();
                                imagePicker.setType("image/*");
                                imagePicker.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(imagePicker, PHOTO_FROM_GALLERY);
                                break;
                        }
                    }
                })
                .build();

        final DatePicker datePicker = new DatePicker(this);
        datePicker.setSpinnersShown(false);
        expDateDialog = new MaterialDialog.Builder(this)
                .title("Expiration Date?")
                .customView(datePicker, true)
                .positiveText("Done")
                .negativeText("None")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        /**
                         * Grab year,month,day, convert to millis, submit with rest of new post.
                         * Create method that calls the result intent to pass all data back to the activity (basically same as whats found in onClick here for submit button)
                         */

                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

                        submitPost(selectedDate);

                        //call mehtod that compiles all info, have one method that acceps expdate and one that doesnt, if user wants post to never expire
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        submitPost(null);
                    }
                })
                .build();

    }

    public void updateUserSelectedImage() throws IOException {
       selectImageIcon.setVisibility(View.GONE);
       uploadedImageContainer.setVisibility(View.VISIBLE);


        ExifInterface imageExif = new ExifInterface(postImage.getAbsolutePath());
        int rotation = imageExif.getAttributeInt(ExifInterface.TAG_ORIENTATION,6);

        if (rotation != ExifInterface.ORIENTATION_NORMAL && rotation != ExifInterface.ORIENTATION_UNDEFINED) {

            Matrix matrix = new Matrix();

            switch(rotation) {
                case ExifInterface.ORIENTATION_ROTATE_90 :
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270 :
                    matrix.setRotate(270);
                    break;
            }



            Bitmap original = BitmapFactory.decodeFile(postImage.getAbsolutePath());
            Bitmap rotated = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), matrix, true);

            FileOutputStream os = new FileOutputStream(postImage);
            rotated.compress(Bitmap.CompressFormat.JPEG, 100, os);

        }


        uploadedImageContainer.setImageBitmap(BitmapFactory.decodeFile(postImage.getAbsolutePath()));
    }

    public int generateResultCode() {
        int status = 0;

        //Need to account for a video post also, start with posts that just have videos (to test uploading to server)

        if (postTextArea.length() > 0 && postImage != null) {status = 2;}
        if (postTextArea.length() > 0 && postImage == null) {status = 1;}
        if (postTextArea.length() == 0 && postImage != null) {status = 3;}
        if (postTextArea.length() == 0 && postImage == null) {status = 0;}

        return status;
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
            switch (requestCode) {
                case PHOTO_FROM_CAMERA :
                    if (postImage.length() > 0) {
                        Crop.of(Uri.fromFile(postImage),finalImageUri).asSquare().start(this);
                        photoSource = PHOTO_FROM_CAMERA;
                    }
                    break;
                case PHOTO_FROM_GALLERY:
                    if (data != null) {
                        Crop.of(data.getData(),finalImageUri).asSquare().start(this);
                        photoSource = PHOTO_FROM_GALLERY;
                    }
                    break;
                case Crop.REQUEST_CROP :
                    try {
                        postImage = new File(Crop.getOutput(data).getPath());
                        updateUserSelectedImage();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case RESULT_CANCELED :
                    postImage = null;
            }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photoAddButton :
                photoSourceSelect.show();
                break;
            case R.id.postSubmitButton :
                /**
                 * Check to make sure at least post text is filled in, photo is not required
                 * Call different thisZoneController.newPost depending on if there is a photo to be uploaded or not
                 * NEED TO GENERATE DIFFERENT RESULT CODES FOR EACH SCENARIO, 1 FOR JUST TEXT, 2 FOR BOTH, 3 FOR JUST IMAGE
                 */
                expDateDialog.show();
        }
    }

    public void submitPost(Calendar selectedDate) {

        Intent results = new Intent();
        int resultCode = 0;

        long expMillis = selectedDate != null ? selectedDate.getTimeInMillis() : 0;

        Log.i("expMillis",String.valueOf(expMillis));

        switch (generateResultCode()) {
            case 0 :
                results = null;
                break;
            case 1:
                results.putExtra("postText",postTextArea.getText().toString());
                results.putExtra("expDate",expMillis);
                resultCode = POST_TEXT_ONLY;
                break;
            case 2:
                results.putExtra("postText",postTextArea.getText().toString());
                results.putExtra("postImage", postImage);
                results.putExtra("expDate", expMillis);
                resultCode = POST_BOTH;
                break;
            case 3:
                results.putExtra("postImage",postImage);
                results.putExtra("expDate", expMillis);
                resultCode = POST_PHOTO_ONLY;
                break;
        }

        if (results != null) {
            setResult(resultCode,results);
            finish();
        }

    }


}
