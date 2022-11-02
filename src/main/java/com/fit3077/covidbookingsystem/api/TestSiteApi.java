package com.fit3077.covidbookingsystem.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fit3077.covidbookingsystem.booking.Booking;
import com.fit3077.covidbookingsystem.testsite.TestSite;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestSiteApi extends ConsumeRestApiAbstract {

  private final String API_PATH = "/testing-site";

  public List<TestSite> getAllTestSitesWithBookings() {
    String apiPath = API_PATH + "?fields=bookings";
    HttpResponse<String> response = this.consumeRestGetApi(apiPath);
    List<TestSite> reponselist = new ArrayList<TestSite>();
    try {
      reponselist = mapper.readValue(response.body(), new TypeReference<List<TestSite>>() {
      });
    } catch (Exception exception) {
      System.out.println("Exceptions raised: getting List of test sites" + exception.getMessage());
    }
    return reponselist;
  }

//  public Booking[] getAllBookingsWithATestSite(String testSiteId) {
//    mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
//    String apiPath = API_PATH + "/" + testSiteId + "?fields=bookings";
//    HttpResponse<String> getResponse = this.consumeRestGetApi(apiPath);
//    try {
//      ObjectNode[] testSiteDetails = this.mapper.readValue(getResponse.body(), ObjectNode[].class);
//      Booking[] activeBookings = this.mapper.convertValue(testSiteDetails[0].get("bookings"), Booking[].class);
//
//      for(Booking booking: activeBookings){
//        booking.setTestSite(this.mapper.readValue(getResponse.body(), TestSite.class));
//      }
//      return activeBookings;
//
//    } catch (Exception exception) {
//      System.out.println("Exceptions raised: getting List of test sites" + exception.getMessage());
//    }
//    return null;
//  }

  public String getOneTestSite(String testSiteID, Integer waitTime) {
    String apiPath = API_PATH + "/" + testSiteID;
    HttpResponse<String> getResponse = this.consumeRestGetApi(apiPath);
    TestSite testSite = TestSite.builder().build();

    String jsonString = "";
    try {
      testSite = mapper.readValue(getResponse.body(), TestSite.class);
      testSite.getAdditionalInfo().setWaitingTime(waitTime + " minutes");
      jsonString = "{ \"additionalInfo\" : " +
          mapper.writeValueAsString(testSite.getAdditionalInfo()) + "}";

    } catch (Exception exception) {
      System.out.println("Exceptions raised: " + exception.getMessage());
    }
    return jsonString;
  }

  public Integer patchWaitingTimeForTestSite(String testSiteID, String jsonBody) {
    String apiPath = API_PATH + "/" + testSiteID;

    HttpResponse<String> postResponse = this.consumeRestPatchApi(apiPath, jsonBody);

    if (postResponse.statusCode() != 200) {
      System.out.println("Did not process for correctly patching Waiting Time"
          + " with response body of " + postResponse.body() + " Status code: "
          + postResponse.statusCode());
    }
    return postResponse.statusCode();
  }

}
