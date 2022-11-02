package com.fit3077.covidbookingsystem.testsite;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;

@JsonInclude(Include.NON_NULL)
public class Address {

  @JsonProperty("latitude")
  public String latitude;
  @JsonProperty("longitude")
  public String longitude;
  @JsonProperty("unitNumber")
  public String unitNumber;
  @JsonProperty("street")
  public String street;
  @JsonProperty("street2")
  public String street2;
  @JsonProperty("suburb")
  public String suburb;
  @JsonProperty("state")
  public String state;
  @JsonProperty("postcode")
  public String postcode;
  @JsonProperty("additionalInfo")
  public ObjectNode additionalInfo;

  public String getLatitude() {
    return latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public String getUnitNumber() {
    return unitNumber;
  }

  public String getStreet() {
    return street;
  }

  public String getStreet2() {
    return street2;
  }

  public String getSuburb() {
    return suburb;
  }

  public String getState() {
    return state;
  }

  public String getPostcode() {
    return postcode;
  }

  public ObjectNode getAdditionalInfo() {
    return additionalInfo;
  }


}


