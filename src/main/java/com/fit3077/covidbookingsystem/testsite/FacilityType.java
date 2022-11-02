package com.fit3077.covidbookingsystem.testsite;

public enum FacilityType {
  DRIVE_THROUGH("Drive Though"),
  WALK_IN("Walk In"),
  CLINICS("Clinic"),
  GP("GP"),
  HOSPITAL("Hospital");

  private final String displayName;

  FacilityType(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
