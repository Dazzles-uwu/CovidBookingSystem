package com.fit3077.covidbookingsystem.testsite;

import java.util.List;

public class TestSiteFilter {

  private String SuburbName;
  private List<FacilityType> facilityType;

  public void setSuburbName(String suburbName) {
    SuburbName = suburbName;
  }

  public void setFacilityType(List<FacilityType> facilityType) {
    this.facilityType = facilityType;
  }

  public String getSuburbName() {
    return SuburbName;
  }

  public List<FacilityType> getFacilityType() {
    return facilityType;
  }


}
