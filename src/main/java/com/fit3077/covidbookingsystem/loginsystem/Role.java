package com.fit3077.covidbookingsystem.loginsystem;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Role {

  @JsonProperty("isCustomer")
  boolean isCustomer = false;
  @JsonProperty("isHealthcareWorker")
  boolean isHealthcareWorker = false;
  @JsonProperty("isReceptionist")
  boolean isReceptionist = false;

  public boolean isHealthcareWorker() {
    return isHealthcareWorker;
  }

  public void setHealthcareWorker(boolean healthcareWorker) {
    isHealthcareWorker = healthcareWorker;
  }

  public boolean isCustomer() {
    return isCustomer;
  }

  public void setCustomer(boolean customer) {
    isCustomer = customer;
  }

  public boolean isReceptionist() {
    return isReceptionist;
  }

  public void setReceptionist(boolean receptionist) {
    isReceptionist = receptionist;
  }

}
