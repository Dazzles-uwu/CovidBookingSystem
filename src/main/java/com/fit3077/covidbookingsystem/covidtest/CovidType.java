package com.fit3077.covidbookingsystem.covidtest;

public enum CovidType {
  PCR("PCR"),
  RAT("RAT");

  private final String covidType;

  CovidType(String covidType) {
    this.covidType = covidType;
  }

  public String getCovidType() {
    return covidType;
  }
}
