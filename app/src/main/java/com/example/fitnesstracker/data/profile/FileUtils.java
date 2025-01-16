package com.example.fitnesstracker.data.profile;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.Objects;

public class FileUtils {
    @Nullable
    public static String getFileName(@NonNull Context context, @NonNull Uri uri) {
        if (Objects.equals(uri.getScheme(), "file")) {
            return new File(uri.getPath()).getName();
        }

        String fileName = null;
        try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int displayNameColumnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (displayNameColumnIndex != -1) {
                    fileName = cursor.getString(displayNameColumnIndex);
                }
            }
        }
        return fileName;
    }
}
