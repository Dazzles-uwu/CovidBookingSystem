package com.fit3077.covidbookingsystem.booking;

import com.fasterxml.jackson.annotation.*;
import com.fit3077.covidbookingsystem.testsite.TestSite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {


  private String customerId;
  private TestSite testSite;
  private String startTime;
  private String notes;
  private String status;
  @JsonProperty("additionalInfo")
  private BookingAdditionalInfo additionalInfo;

  @JsonIgnore
  private String smsPin;

  @JsonAlias("id")
  private String bookingId;

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = String.valueOf(startTime);
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public BookingAdditionalInfo getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(BookingAdditionalInfo additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  public String getSmsPin() {
    return smsPin;
  }

  public void setSmsPin(String smsPin) {
    this.smsPin = smsPin;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @JsonIgnore
  public TestSite getTestSite() {
    return testSite;
  }

  @JsonProperty("testingSite")
  public void setTestSite(TestSite testSite) {
    this.testSite = testSite;
  }

  public String getBookingId() {
    return bookingId;
  }

  public void setBookingId(String bookingId) {
    this.bookingId = bookingId;
  }


}


