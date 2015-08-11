package com.example.dylan.ourcloud;

import android.content.Context;
import android.database.Cursor;

import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by dylan on 8/10/15.
 */
public class ImageUtil {

    public static File getImageFile(Context context, Uri imageUri) {
        String[] memory = {MediaStore.Images.Media.DATA};
        Cursor memoryCursor = context.getContentResolver().query(imageUri, memory, null, null, null);
        memoryCursor.moveToFirst();
        int colIndex = memoryCursor.getColumnIndex(MediaStore.Images.Media.DATA);
        String path = memoryCursor.getString(colIndex);

        return new File(path);
    }

}
