package com.fit3077.covidbookingsystem.api;

import com.fit3077.covidbookingsystem.covidtest.CovidTest;
import com.fit3077.covidbookingsystem.loginsystem.User;
import java.net.http.HttpResponse;

public class CovidTestApi extends ConsumeRestApiAbstract {

  private final String API_PATH = "/covid-test";
  private String jsonString;

  public Integer createNewCovidTest(CovidTest covidTest, User user) {

    if (!covidTest.getBookingId().equals("")) {
      jsonString = "{" +
          "\"type\":\"" + covidTest.getType() + "\"," +
          "\"patientId\":\"" + covidTest.getPatientId() + "\"," +
          "\"bookingId\":\"" + covidTest.getBookingId() + "\"," +
          "\"administererId\":\"" + user.getId() + "\"" +
          "}";
    } else {
      jsonString = "{" +
          "\"type\":\"" + covidTest.getType() + "\"," +
          "\"patientId\":\"" + covidTest.getPatientId() + "\"," +
          "\"administererId\":\"" + user.getId() + "\"" +
          "}";
    }

    HttpResponse<String> postResponse = this.consumeRestPostApi(API_PATH, jsonString);

    System.out.println("You have created a NEW Covid TEST Object: " + postResponse.body());

    if (postResponse.statusCode() != 201) {
      System.out.println("Did not process for correctly creating covid-test"
          + " with response body of " + postResponse.body());
    }

    return postResponse.statusCode();
  }
}
