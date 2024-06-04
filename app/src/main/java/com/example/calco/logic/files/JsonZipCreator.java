package com.example.calco.logic.files;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.google.gson.JsonElement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class JsonZipCreator {

    private static final String NAME = "calco_history_";
    private static final String JSON_EXTENSION = ".json";
    private static final String ZIP_EXTENSION = ".zip";
    public static String createZip(Context context, JsonFile... jsonFiles) {
        Uri zipUri = createZipFileUri(context);
        System.out.println("ZipUri: "+zipUri+" "+zipUri.getPath());

        fillZip(zipUri, context, jsonFiles);

        return getRealPathFromURI(context, zipUri);
    }

    private static String getZipName() {
        return NAME + DateTimeFormatter.ofPattern("MM/dd").format(LocalDateTime.now()) + ZIP_EXTENSION;
    }

    private static Uri createZipFileUri(Context context) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, getZipName());
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/zip");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

        return resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
    }

    private static void fillZip(Uri zipUri, Context context, JsonFile... jsonFiles) {
        try {
            OutputStream os = context.getContentResolver().openOutputStream(zipUri);
            if (os != null) {
                ZipOutputStream zos = new ZipOutputStream(os);

                for (JsonFile jsonFile : jsonFiles) {
                    zos.putNextEntry(new ZipEntry(jsonFile.getName() + JSON_EXTENSION));
                    zos.write(jsonFile.getJsonElement().toString().getBytes(StandardCharsets.UTF_8));
                    zos.closeEntry();
                }

                zos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getRealPathFromURI(Context context, Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = null;
        String filePath = null;

        try {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return filePath;
    }
}