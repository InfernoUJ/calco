package com.example.calco;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.calco.logic.files.JsonFilesCreator;
import com.example.calco.logic.files.JsonZipCreator;
import com.example.calco.logic.files.JsonZipReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.zip.ZipFile;

public class DataActivity extends AppCompatActivity {

    private final static int OPEN_FILE_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setHandlers();
    }

    private void setHandlers() {
        setExportButtonHandler();
        setImportButtonHandler();
    }

    private void setExportButtonHandler() {
        Button exportButton = findViewById(R.id.exportDataBtn);
        exportButton.setOnClickListener(v -> {
            boolean result = storeHistory();
            if (result) {
                Toast.makeText(getApplicationContext(), "Data saved to downloads with success", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Data saving failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setImportButtonHandler() {
        Button importButton = findViewById(R.id.importDataBtn);
        importButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("application/zip");
            startActivityForResult(intent, OPEN_FILE_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_FILE_CODE && resultCode == RESULT_OK && data != null) {
            Uri zipUri = data.getData();
            String zipPath = zipUri.getPath();
            System.out.println("Data file uri: "+zipUri+" "+zipPath);

            boolean result = importHistory(zipUri);
            if (result) {
                Toast.makeText(getApplicationContext(), "Data imported with success", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Data import failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean storeHistory() {
        try {
            JsonZipCreator.createZip(getApplicationContext(), JsonFilesCreator.createJsonFiles());
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean importHistory(Uri zipUri) {
        try {
            System.out.println("Importing history from: "+zipUri);
            JsonZipReader.addHistory(getApplicationContext(), zipUri);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private ZipFile getZipFile(Uri uri) throws IOException {
        File outputDir = getApplicationContext().getCacheDir();
        File outputFile = File.createTempFile("temp_history"+LocalDateTime.now(), ".zip", outputDir);

        InputStream is = getApplicationContext().getContentResolver().openInputStream(uri);
        Files.copy(is, outputFile.toPath(), REPLACE_EXISTING);

        return new ZipFile(outputFile);
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = null;
        String filePath = null;

        try {
            cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
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