package com.fit3077.covidbookingsystem.covidtest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CovidTest {

  @JsonProperty("type")
  private String type;
  @JsonProperty("patientId")
  private String patientId;
  @JsonProperty("administererId")
  private String administererId;
  @JsonProperty("result")
  private String result;
  @JsonProperty("bookingId")
  private String bookingId;
  @JsonProperty("status")
  private String status;
  @JsonProperty("notes")
  private String notes;
  @JsonProperty("additionalInfo")
  private String additionalInfo;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public String getAdministererId() {
    return administererId;
  }

  public void setAdministererId(String administererId) {
    this.administererId = administererId;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  public String getBookingId() {
    return bookingId;
  }

  public void setBookingId(String bookingId) {
    this.bookingId = bookingId;
  }
}
