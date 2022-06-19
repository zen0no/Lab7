package net;

import com.google.gson.*;
import dataClasses.HumanBeing;
import other.InfoBlock;
import other.typeAdapters.DateAdapter;
import other.typeAdapters.ZonedDateTimeAdapter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Response implements Serializable {
    public StatusCode code;

    public Method method;

    public Object body;




    public enum StatusCode{
        OK,
        ERROR,
    }

    public Response(StatusCode code, Method method, Object object){
        this.code = code;
        this.method = method;
        this.body = object;
    }

    public static String toJson(Response response){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new DateAdapter());
        builder.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter());
        return builder.create().toJson(response);
    }

    public static Response fromJson(String string){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new DateAdapter());
        builder.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter());
        Gson gson = builder.create();
        JsonObject o = JsonParser.parseString(string).getAsJsonObject();
        StatusCode code = gson.fromJson(o.get("code"), StatusCode.class);
        Method method = gson.fromJson(o.get("method"), Method.class);
        switch (code){
            case ERROR -> {
                String body = o.get("body").getAsString();
                return new Response(code, method, body);

            }

            case OK -> {
                switch (method){
                    case LOGIN, REGISTER -> {
                        return new Response(code, method, null);
                    }
                    case POST, DELETE, GET, UPDATE -> {
                        JsonArray json = (JsonArray) o.get("body");
                        List<HumanBeing> body = new ArrayList<HumanBeing>();
                        for (JsonElement e: json){
                            HumanBeing h = gson.fromJson(e, HumanBeing.class);
                            body.add(h);
                        }
                        return new Response(code, method, body);
                    }

                    case INFO ->{
                        InfoBlock body = gson.fromJson(o.get("body"), InfoBlock.class);
                        return new Response(code, method, body);
                    }
                    default -> {
                        return null;
                    }
                }
            }

            default -> {
                return null;
            }
        }
    }
}
