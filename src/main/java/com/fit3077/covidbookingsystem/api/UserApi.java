package com.fit3077.covidbookingsystem.api;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fit3077.covidbookingsystem.booking.Booking;
import com.fit3077.covidbookingsystem.loginsystem.User;
import java.net.http.HttpResponse;

public class UserApi extends ConsumeRestApiAbstract {

  private final String API_PATH = "/user/";

  public HttpResponse<String> login(User user) {
    String apiPath = API_PATH + "login?jwt=true";
    String jsonString = "{" +
        "\"userName\":\"" + user.getId() + "\"," +
        "\"password\":\"" + user.getPassword() + "\"" +
        "}";
    return this.consumeRestPostApi(apiPath, jsonString);

  }

  public Booking[] getCustomerActiveBooking(String customerID) {
    mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    String apiPath = API_PATH + customerID + "?fields=bookings";
    HttpResponse<String> response = consumeRestGetApi(apiPath);
    try {
      ObjectNode[] userDetails = this.mapper.readValue(response.body(), ObjectNode[].class);
      return this.mapper.convertValue(userDetails[0].get("bookings"), Booking[].class);
    } catch (Exception exception) {
      System.out.println("Fail to deserialize json for active bookings " + exception.getMessage());
    }

    System.out.println("Fail to deserialize json for active bookings or could be empty");
    return new Booking[0];

  }
}
