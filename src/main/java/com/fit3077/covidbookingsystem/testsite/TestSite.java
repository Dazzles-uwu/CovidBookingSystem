package com.fit3077.covidbookingsystem.testsite;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fit3077.covidbookingsystem.booking.Booking;
import java.util.List;
import jdk.dynalink.linker.LinkerServices;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class TestSite {

  @JsonProperty("id")
  private String id;
  @JsonProperty("name")
  private String name;
  @JsonProperty("description")
  private String description;
  @JsonProperty("websiteUrl")
  private String websiteUrl;
  @JsonProperty("phoneNumber")
  private String phoneNumber;
  @JsonProperty("address")
  private Address address;
  @JsonProperty("createdAt")
  private String createdAt;
  @JsonProperty("updatedAt")
  private String updatedAt;
  @JsonProperty("additionalInfo")
  private TestSiteAdditionalInfo additionalInfo;

  @JsonProperty("bookings")
  private List<Booking> bookings;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getWebsiteUrl() {
    return websiteUrl;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public Address getAddress() {
    return address;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public TestSiteAdditionalInfo getAdditionalInfo() {
    return additionalInfo;
  }

  public List<Booking> getBookings() {
    return bookings;
  }


}
