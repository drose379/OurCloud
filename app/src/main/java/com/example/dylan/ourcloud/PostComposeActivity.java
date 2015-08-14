package com.example.dylan.ourcloud;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;


/**
 * Created by dylan on 8/12/15.
 */
public class PostComposeActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView selectImageIcon;
    ImageView uploadedImageContainer;

    private MaterialDialog photoSourceSelect;
    private File postImage = null;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
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
        selectImageIcon = (ImageView) findViewById(R.id.photoAddButton);
        uploadedImageContainer = (ImageView) findViewById(R.id.selectedImageContainer);

        userName.setText(UserInfo.getInstance().getDisplayName());
        Picasso.with(this).load(UserInfo.getInstance().getProfileImageSized(100)).into(userImage);
        selectImageIcon.setOnClickListener(this);

    }

    public void initDialogs() {
        photoSourceSelect = new MaterialDialog.Builder(this)
                .title("Choose Location")
                .items(new String[]{"Camera", "Gallery"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        switch (i) {
                            case 0:
                                //camera
                                break;
                            case 1:
                                Intent imagePicker = new Intent();
                                imagePicker.setType("image/*");
                                imagePicker.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(imagePicker, 2);
                                break;
                        }
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
            Log.i("imageOri", "Not normal");
            Log.i("imageOri",imageExif.getAttribute(ExifInterface.TAG_ORIENTATION));
            uploadedImageContainer.setRotation(90);
        }

        uploadedImageContainer.setImageBitmap(BitmapFactory.decodeFile(postImage.getAbsolutePath()));
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        if (resultCode == -1) {
            switch (requestCode) {
                case 1 :
                    //from camera
                    break;

                case 2:
                    try {
                        Uri imageUri = data.getData();
                        postImage  = ImageUtil.getImageFile(this,imageUri);
                        updateUserSelectedImage();
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                    break;
            }
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
                 */
                break;
        }
    }

}
