package com.example.calco.logic.files;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class JsonZipReader {
    public static <T> List<T> deserialize(Class<T> clazz, String zipPath) {
        try {
            Type listClass = TypeToken.getParameterized(List.class, clazz).getType();
            ZipFile zipFile = new ZipFile(zipPath);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String nameWithoutExtension = entry.getName().replace(".json", "");
                if (nameWithoutExtension.equals(JsonFileName.getName(clazz))) {
                    return new Gson().fromJson(new String(zipFile.getInputStream(entry).readAllBytes(), StandardCharsets.UTF_8), listClass);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}
