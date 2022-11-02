package com.fit3077.covidbookingsystem.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit3077.covidbookingsystem.APIKEY;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public abstract class ConsumeRestApiAbstract {

  private static final String myApiKey = APIKEY.API_KEY;
  private static final String rootUrl = "https://fit3077.com/api/v2";


  protected ObjectMapper mapper = new ObjectMapper();


  protected HttpResponse<String> consumeRestGetApi(String path) {
    String usersUrl = rootUrl + path;

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest
        .newBuilder(URI.create(usersUrl))
        .setHeader("Authorization", myApiKey)
        .GET()
        .build();

    try {

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      return response;

    } catch (Exception ex) {

      System.out.println(ex.getMessage());

    }
    return null;

  }

  protected HttpResponse<String> consumeRestPostApi(String path, String jsonString) {
    String usersUrl = rootUrl + path;

    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest
        .newBuilder(URI.create(usersUrl))
        .setHeader("Authorization", myApiKey)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonString))
        .build();

    try {

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      return response;

    } catch (Exception ex) {

      System.out.println(ex.getMessage());

    }
    return null;

  }

  protected HttpResponse<String> consumeRestPatchApi(String path, String jsonString) {
    String usersUrl = rootUrl + path;

    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest
        .newBuilder(URI.create(usersUrl))
        .setHeader("Authorization", myApiKey)
        .header("Content-Type", "application/json")
        .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonString))
        .build();

    try {

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      return response;

    } catch (Exception ex) {

      System.out.println(ex.getMessage());

    }
    return null;

  }

  protected HttpResponse<String> consumeRestDeleteApi(String path) {
    String usersUrl = rootUrl + path;

    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest
        .newBuilder(URI.create(usersUrl))
        .setHeader("Authorization", myApiKey)
        .DELETE()
        .build();

    try {

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      return response;

    } catch (Exception ex) {

      System.out.println(ex.getMessage());

    }
    return null;

  }


}
