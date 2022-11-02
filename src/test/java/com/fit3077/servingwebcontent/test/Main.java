package com.fit3077.servingwebcontent.test;/*
  Norman Chen, 2021 - 2022.

  Access the COVID Testing Registration system web service and query data in Java.

  There are several ways to make HTTP requests in Java:
  https://www.twilio.com/blog/5-ways-to-make-http-requests-in-java

  In this sample code, we will be using the built-in HttpClient library, available in Java 11 onwards.

  Parsing JSON in Java (and converting them to Java objects) might require third-party packages.
  Any imported third-party packages will then become dependencies of your project.

  To download and import third-party packages, you can use the Maven tool in your project. For IntelliJ:
  https://www.jetbrains.com/help/idea/convert-a-regular-project-into-a-maven-project.html

  When using Maven, third-party package dependencies can be defined in the pom.xml file.
  We will use the following package as a third-party package to help us parse JSON: com.fasterxml.jackson.core

  You are free to use any third-party packages to make HTTP requests and parse JSON responses as you wish.
  You do not need to strictly follow the tools/packages/approaches described in this sample code, as it serves as a guide only.
 */

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.fit3077.covidbookingsystem.APIKEY;
import com.fit3077.covidbookingsystem.booking.Booking;
import com.fit3077.covidbookingsystem.booking.BookingAdditionalInfo;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {
  /*
    NOTE: In order to access the web service, you will need to include your api key in the Authorization header of all requests you make.
    Your personal api key can be obtained here: https://fit3077.com
   */
  private static final String myApiKey = APIKEY.API_KEY;

  // Provide the root URL for the web service. All web service request URLs start with this root URL.
  private static final String rootUrl = "https://fit3077.com/api/v2";

  public static void main(String[] args) throws Exception {
    // To get a specific resource from the web service, extend the root URL by appending the resource type you are looking for.
    // For example: [root_url]/user will return a JSON array object containing all users.
    ObjectMapper mapper = new ObjectMapper();

//    mapper.createObjectNode();
//    String json = "{\"name\":\"mkyong\", \"age\":\"37\"}";
//    String field = "name";
//    String value = "Kenny";
//    String test = "{\"" + field + "\" : \" " + value +"";
//
//    List<String> testArray = new ArrayList<>();
//    testArray.add("312");
//
//    Map hm = new HashMap();
//    hm.put("field","123");
//    String json1 = mapper.writeValueAsString(hm);
//    ObjectNode teston = mapper.createObjectNode();
//    mapper.valueToTree(json1);
//    mapper.readValue(json1,ObjectNode.class);
//    mapper.readValue((JsonParser) hm,ObjectNode.class);
//
//
//
//
//
//
//    String usersUrl = rootUrl + "/user";
//
//    /*
//      Part 1 - Unauthenticated requests
//     */
//
//    // This request is unauthenticated (api key not attached), and will return an error.
//    HttpClient client = HttpClient.newHttpClient();
//    HttpRequest request = HttpRequest
//      .newBuilder(URI.create(usersUrl))
//      .GET()
//      .build();
//
//    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//    System.out.println("Part 1\n----");
//    System.out.println(request.uri());
//    System.out.println("Response code: " + response.statusCode()); // Status code of 4xx or 5xx indicates an error with the request or with the server, respectively.
//    System.out.println("Full JSON response: " + response.body());
//    System.out.println("----\n\n");


    /*
      Part 2 - Performing GET requests that return an array of objects.
     */
    String usersUrl = rootUrl + "/booking/a64dd69c-40b1-4d67-bad3-d4eac30c07e5";

    // This request is authenticated (api key attached in the Authorization header), and will return the JSON array object containing all users.
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest
      .newBuilder(URI.create(usersUrl))
      .setHeader("Authorization", myApiKey)
      .GET()
      .build();

    HttpResponse<String>  response = client.send(request, HttpResponse.BodyHandlers.ofString());

    mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    ObjectNode[] userDetails = mapper.readValue(response.body(), ObjectNode[].class);
     mapper.convertValue(userDetails[0].get("additionalInfo"), BookingAdditionalInfo[].class);

    System.out.println("Part 2\n----");
    System.out.println(request.uri());
    System.out.println("Response code: " + response.statusCode());
    System.out.println("Full JSON response: " + response.body());

    System.out.println();

    // Error checking for this sample code. You can check the status code of your request, as part of performing error handling in your assignment.
    if (response.statusCode() != 200) {
      throw new Exception("Please specify your api key in line 38 to continue using this sample code.");
    }

    // The GET /user endpoint returns a JSON array, so we can loop through the response as we could with a normal array/list.
    ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);

    for (ObjectNode node: jsonNodes) {
      System.out.println(node.toString());
    }

    System.out.println("----\n\n");

    // Get the first user object in the array and keep for future use in later parts of this sample code.
    ObjectNode userObjectForPart3 = jsonNodes[0];

    /*
      Part 3a - Performing an invalid GET request to fetch a particular resource by ID.
     */

    // To get a particular User resource, you will need to specify the user ID you are interested in. Simply extend the users_url by appending the user's id.
    String usersIdUrl = usersUrl + "/this-is-my-make-believe-id";

    // This request will fail as the user's ID is invalid/does not exist.
    client = HttpClient.newHttpClient();
    request = HttpRequest
        .newBuilder(URI.create(usersIdUrl))
        .setHeader("Authorization", myApiKey)
        .GET()
        .build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println("Part 3a\n----");
    System.out.println(request.uri());
    System.out.println("Response code: " + response.statusCode());
    System.out.println("Full JSON response: " + response.body());
    System.out.println("----\n\n");


    /*
      Part 3b - Performing a valid GET request to fetch a particular resource by ID
     */

    // This request will succeed (assuming there was at least one user object returned in the array from Part 2.
    usersIdUrl = usersUrl + "/" + userObjectForPart3.get("id").textValue();
    client = HttpClient.newHttpClient();
    request = HttpRequest
        .newBuilder(URI.create(usersIdUrl))
        .setHeader("Authorization", myApiKey)
        .GET()
        .build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println("Part 3b\n----");
    System.out.println(request.uri());
    System.out.println("Response code: " + response.statusCode());
    System.out.println("Full JSON response: " + response.body());
    System.out.println("----\n\n");


    /*
      Part 3c - Performing a valid GET request to fetch a particular resource by ID, with query parameters.
     */

    // To get more data for users (e.g. their bookings and COVID tests taken), you can add query params to the request.
    // You can directly append the query string to the end of the request URL - e.g. usersIdUrl + "?fields=bookings"
    // To get multiple fields, you can use: usersIdUrl + "?fields=bookings&fields=testsTaken"
    client = HttpClient.newHttpClient();
    request = HttpRequest
        .newBuilder(URI.create(usersIdUrl + "?fields=bookings"))
        .setHeader("Authorization", myApiKey)
        .GET()
        .build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println("Part 3c\n----");
    System.out.println(request.uri());
    System.out.println("Response code: " + response.statusCode());
    System.out.println("Full JSON response: " + response.body()); // Note that the object now has the bookings property returned.
    System.out.println("----\n\n");

    /*
      Part 4 - Authenticating a user's credentials using the POST /user/login endpoint
     */

    // Create the request body. The password for each of the sample user objects that have been created for you are the same as their respective usernames.
    String jsonString = "{" +
      "\"userName\":\"" + userObjectForPart3.get("userName").textValue() + "\"," +
      "\"password\":\"" + userObjectForPart3.get("userName").textValue() + "\"" +
      "}";

    // Note the POST() method being used here, and the request body is supplied to it.
    // A request body needs to be supplied to this endpoint, otherwise a 400 Bad Request error will be returned.
    String usersLoginUrl = usersUrl + "/login";
    client = HttpClient.newHttpClient();
    request = HttpRequest.newBuilder(URI.create(usersLoginUrl + "?jwt=true")) // Return a JWT so we can use it in Part 5 later.
      .setHeader("Authorization", myApiKey)
      .header("Content-Type","application/json") // This header needs to be set when sending a JSON request body.
      .POST(HttpRequest.BodyPublishers.ofString(jsonString))
      .build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println("Part 4\n----");
    System.out.println(request.uri());
    System.out.println("Response code: " + response.statusCode());
    System.out.println("Full JSON response: " + response.body()); // The JWT token that has just been issued will be returned since we set ?jwt=true.
    System.out.println("----\n\n");


    /*
      Part 5a - Verifying a JWT using the POST /user/verify-token endpoint
     */
    ObjectNode jsonNode = new ObjectMapper().readValue(response.body(), ObjectNode.class);

    jsonString = "{\"jwt\":\"" + jsonNode.get("jwt").textValue() + "\"}";

    // Note the POST() method being used here, and the request body is supplied to it.
    // A request body needs to be supplied to this endpoint, otherwise a 400 Bad Request error will be returned.
    String usersVerifyTokenUrl = usersUrl + "/verify-token";
    client = HttpClient.newHttpClient();
    request = HttpRequest.newBuilder(URI.create(usersVerifyTokenUrl)) // Return a JWT so we can use it in Part 5 later.
      .setHeader("Authorization", myApiKey)
      .header("Content-Type","application/json") // This header needs to be set when sending a JSON request body.
      .POST(HttpRequest.BodyPublishers.ofString(jsonString))
      .build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println("Part 5a\n----");
    System.out.println(request.uri());
    System.out.println("Response code: " + response.statusCode());
    System.out.println("Full JSON response: " + response.body()); // This endpoint does not return a JSON response upon success.
    System.out.println("----\n\n");


    /*
      Part 5b - Verifying a forged/tampered JWT using the POST /user/verify-token endpoint
     */
    jsonString = "{\"jwt\":\"" + jsonNode.get("jwt").textValue() + "tampered" + "\"}"; // Tamper with the JWT.

    // Note the POST() method being used here, and the request body is supplied to it.
    // A request body needs to be supplied to this endpoint, otherwise a 400 Bad Request error will be returned.
    // A JSON response will only be returned by the POST /user/verify-token endpoint if there were problems verifying the JWT.
    client = HttpClient.newHttpClient();
    request = HttpRequest.newBuilder(URI.create(usersVerifyTokenUrl))
      .setHeader("Authorization", myApiKey)
      .header("Content-Type","application/json") // This header needs to be set when sending a JSON request body.
      .POST(HttpRequest.BodyPublishers.ofString(jsonString))
      .build();

    response = client.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println("Part 5b\n----");
    System.out.println(request.uri());
    System.out.println("Response code: " + response.statusCode());
    System.out.println("Full JSON response: " + response.body()); // Note that all 4xx and 5xx errors are guaranteed to return a JSON response.
    System.out.println("----\n\n");
  }
}
