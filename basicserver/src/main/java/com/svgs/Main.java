package com.svgs;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.gson.Gson;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class Main {
    private static Gson gson = new Gson();
    private static List<Message> messages = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        port(4567);
        staticFiles.location("/public");
        disableCORS();
        get("/", (req, res) -> {
            res.redirect("/testProgram.html");
            return null;
        });
        get("/test", (req, res) -> "<b><i>This is a test!</i></b>");

        post("/send", (req, res) -> {
            System.out.println(req.body());
            Message message = gson.fromJson(req.body(), Message.class);
            messages.add(message);
            res.status(201);
            return "";
        });
        get("/messages", (req, res) -> {
            res.type("application/json");
            return gson.toJson(messages);
        });
    }

    public static void disableCORS() {
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        });

        options("/*", (req, res) -> {
            String accessControlRequestHeaders = req.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                res.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = req.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                res.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });
    }
}