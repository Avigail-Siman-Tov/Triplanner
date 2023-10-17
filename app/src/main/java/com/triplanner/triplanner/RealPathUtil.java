package com.triplanner.triplanner;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class RealPathUtil {
    public static String getRealPath(Context context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String realPath = cursor.getString(column_index);
        cursor.close();
        return realPath;
    }
}