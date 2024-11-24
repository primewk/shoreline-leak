package net.shoreline.client.api.config;

import com.google.gson.JsonObject;

public interface Serializable<T> {
   JsonObject toJson();

   T fromJson(JsonObject var1);
}
