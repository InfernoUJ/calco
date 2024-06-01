package com.example.calco.logic.files;

import com.google.gson.JsonElement;

public class JsonFile {
    private JsonFileName name;
    private JsonElement jsonElement;

    public JsonFile(JsonFileName name, JsonElement jsonElement) {
        this.name = name;
        this.jsonElement = jsonElement;
    }

    public JsonFileName getName() {
        return name;
    }

    public JsonElement getJsonElement() {
        return jsonElement;
    }
}
