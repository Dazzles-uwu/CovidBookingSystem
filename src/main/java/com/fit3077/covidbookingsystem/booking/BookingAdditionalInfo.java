package com.fit3077.covidbookingsystem.booking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fit3077.covidbookingsystem.testsite.FacilityType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingAdditionalInfo {

  @JsonProperty("hasKitCollected")
  private Boolean hasKitCollected;

  @JsonProperty("qrCode")
  private String qrCode;
  @JsonProperty("videoUrlCode")
  private String videoUrlCode;

  @JsonProperty("previousBooking")
  private List<Booking> previousBooking;


  public Boolean getHasKitCollected() {
    return hasKitCollected;
  }

  public void setHasKitCollected(Boolean hasKitCollected) {
    this.hasKitCollected = hasKitCollected;
  }

  public String getQrCode() {
    return qrCode;
  }

  public void setQrCode(String qrCode) {
    this.qrCode = qrCode;
  }

  public String getVideoUrlCode() {
    return videoUrlCode;
  }

  public void setVideoUrlCode(String videoUrlCode) {
    this.videoUrlCode = videoUrlCode;
  }

  public List<Booking> getPreviousBooking() {
    return previousBooking;
  }

  public void setPreviousBooking(
      List<Booking> previousBooking) {
    this.previousBooking = previousBooking;
  }

}
