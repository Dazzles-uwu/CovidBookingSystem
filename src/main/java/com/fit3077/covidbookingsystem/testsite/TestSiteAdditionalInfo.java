package com.fit3077.covidbookingsystem.testsite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fit3077.covidbookingsystem.loginsystem.User;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestSiteAdditionalInfo {

  @JsonProperty("openTime")
  private String openTime;
  @JsonProperty("closingTime")
  private String closingTime;

  @JsonProperty("waitingTime")
  private String waitingTime;

  @JsonProperty("availableFacilityType")
  private List<FacilityType> availableFacilityType;

  @JsonProperty("testSiteWorkers")
  private List<User> testSiteWorkers;

  public String getOpenTime() {
    return openTime;
  }

  public void setOpenTime(String openTime) {
    this.openTime = openTime;
  }

  public String getClosingTime() {
    return closingTime;
  }

  public void setClosingTime(String closingTime) {
    this.closingTime = closingTime;
  }

  public List<FacilityType> getAvailableFacilityType() {
    return availableFacilityType;
  }

  public void setAvailableFacilityType(
      List<FacilityType> availableFacilityType) {
    this.availableFacilityType = availableFacilityType;
  }

  public String getWaitingTime() {
    return waitingTime;
  }

  public void setWaitingTime(String waitingTime) {
    this.waitingTime = waitingTime;
  }

  public List<User> getTestSiteWorkers() {
    return testSiteWorkers;
  }
}
