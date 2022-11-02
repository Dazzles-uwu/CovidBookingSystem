package com.fit3077.covidbookingsystem.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fit3077.covidbookingsystem.booking.Booking;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class BookingApi extends ConsumeRestApiAbstract {

  private final String API_PATH = "/booking";

  public String getBookingsAndFilterByQRCode(Booking booking, String qrCode) {
    HttpResponse<String> response = this.consumeRestGetApi(API_PATH);

    ObjectNode[] bookingList = null;
    try {
      bookingList = this.mapper.readValue(response.body(), ObjectNode[].class);
    } catch (Exception exception) {
      System.out.println("Fail to deserialize json for booking");
    }
    for (ObjectNode node : bookingList) {
      if (node.get("additionalInfo").size() != 0 && node.get("additionalInfo").get("qrCode")
          .asText().equals(qrCode)) {
        Boolean hasCollectedKit = node.get("additionalInfo").get("hasKitCollected").asBoolean();
        booking.setCustomerId(node.get("id").asText());
        if (hasCollectedKit) {
          return "QR Code is valid and kit has already been collected";
        }
        return "QR Code is valid and kit can be collected";
      }
    }
    return "Could not find QR Code in the system";
  }

  public Integer postCreateNewBookings(Booking booking) {
    String jsonString = "";
    try {
      jsonString = this.mapper.writeValueAsString(booking);
      // add test site id if it exists
      if (booking.getTestSite() != null && booking.getTestSite().getId() != null) {
        jsonString = "{\"testingSiteId\" : \"" + booking.getTestSite().getId() + "\","
            + jsonString.substring(1);
      }
    } catch (JsonProcessingException e) {
      System.out.println("Exception serializing booking");
    }
    HttpResponse<String> postResponse = this.consumeRestPostApi(API_PATH, jsonString);
    if (postResponse.statusCode() != 201) {
      System.out.println("Did not process for creating new bookings"
          + " correctly with response body of " + postResponse.body());
    }
    return postResponse.statusCode();
  }

  public Booking getPinNumberAndVerify(Booking booking, String smsPin) {
    HttpResponse<String> response = this.consumeRestGetApi(API_PATH);
    ObjectNode[] bookingList = null;
    try {
      bookingList = mapper.readValue(response.body(), ObjectNode[].class);

      for (ObjectNode node : bookingList) {
        if (node.get("smsPin").asText().equals(smsPin)) {

          Booking originalBooking = this.mapper.treeToValue(node, Booking.class);
          originalBooking.setCustomerId(node.get("customer").get("id").asText());
          return originalBooking;
        }
      }
      booking.setStatus("PIN NOT VALID");
    } catch (Exception exception) {
      System.out.println("Fail to deserialize json for booking");
    }
    return booking;
  }

  public String getQRCodeAndConfirm(String bookingID) {
    String apiPath = API_PATH + "/" + bookingID;
    HttpResponse<String> getResponse = this.consumeRestGetApi(apiPath);

    if (getResponse.statusCode() != 200) {
      return "Unable to retrieve existing Booking";
    }

    Booking booking;
    String jsonString = "";
    try {
      booking = mapper.readValue(getResponse.body(), Booking.class);

      booking.getAdditionalInfo().setHasKitCollected(true);

      mapper.writeValueAsString(booking.getAdditionalInfo());
      jsonString = "{ \"additionalInfo\" : " +
          mapper.writeValueAsString(booking.getAdditionalInfo()) + "}";

    } catch (Exception exception) {
      System.out.println("Coult not get booking details to verify QR CODE");
    }
    HttpResponse<String> patchResponse = this.consumeRestPatchApi(apiPath, jsonString);

    if (patchResponse.statusCode() == 200) {
      return "System Acknowledged that patient has picked up KIT,";
    }

    return "Could not find QR Code in the system";
  }

  public Integer updateBookingStatus(String bookingID, String status) {
    String apiPath = API_PATH + "/" + bookingID;
    String jsonString = "{ \"status\" : \"" +
        status + "\" }";

    HttpResponse<String> patchResponse = this.consumeRestPatchApi(apiPath, jsonString);

    if (patchResponse.statusCode() != 200) {
      System.out.println("Fail to update status of existing booking " + patchResponse.body());
    }

    return patchResponse.statusCode();

  }

  public Integer updateModifiedBookingStatus(Booking booking) {
    String apiPath = API_PATH + "/" + booking.getBookingId();
    String jsonString = " {\"startTime\": \"" + booking.getStartTime() + "\",\n"
        + "    \"testingSiteId\": \"" + booking.getTestSite().getId() + "\",\n"
        + " \"notes\": \"" + (booking.getNotes() == null ? "MODIFIED BOOKING" : booking.getNotes())
        + "\" ";

    if (booking.getAdditionalInfo().getPreviousBooking() != null
        && booking.getAdditionalInfo().getPreviousBooking().size() != 0) {
      String jsonAdditionalInfoString = ", \n \"additionalInfo\": { \"previousBooking\" : [";
      for (Booking bookingElement : booking.getAdditionalInfo().getPreviousBooking()) {
        jsonAdditionalInfoString =
            jsonAdditionalInfoString + "{" + "\"id\" : \"" + bookingElement.getBookingId()
                + "\", \n"
                + " \"startTime\": \"" + bookingElement.getStartTime() + "\"},\n";
      }
      jsonString +=
          jsonAdditionalInfoString.substring(0, jsonAdditionalInfoString.length() - 2) + "] }";
    } else if (booking.getAdditionalInfo().getPreviousBooking().size() == 0) {
      String jsonAdditionalInfoString = ", \n \"additionalInfo\": { \"previousBooking\" : [ ] }";
      jsonString += jsonAdditionalInfoString;
    }

    jsonString += "}";

    HttpResponse<String> patchResponse = this.consumeRestPatchApi(apiPath, jsonString);

    if (patchResponse.statusCode() != 200) {
      System.out.println("Fail to update status of existing booking " + patchResponse.body());
    }

    return patchResponse.statusCode();

  }

  public Booking findByBookingId(String bookingId) {
    String apiPath = API_PATH + "/" + bookingId;
    HttpResponse<String> getResponse = this.consumeRestGetApi(apiPath);

    if (getResponse.statusCode() != 200) {
      System.out.println(
          "Fail to get booking with booking id:" + bookingId + " Response: " + getResponse.body());
    }
    try {
      Booking booking = this.mapper.readValue(getResponse.body(), Booking.class);
      booking.setCustomerId(
          this.mapper.readValue(getResponse.body(), JsonNode.class).get("customer").get("id")
              .asText());
      return booking;
    } catch (Exception exception) {
      System.out.println("Fail to deserialise booking");
    }

    return null;
  }

  public Integer deleteBooking(String bookingId) {
    String apiPath = API_PATH + "/" + bookingId;
    HttpResponse<String> deleteResponse = this.consumeRestDeleteApi(apiPath);

    if (deleteResponse.statusCode() != 204) {
      System.out.println("Fail to get booking with booking id:" + bookingId + " Response: "
          + deleteResponse.body());
    }

    return deleteResponse.statusCode();

  }

  public List<Booking> getAllBookings() {
    mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    String apiPath = API_PATH;
    HttpResponse<String> getResponse = this.consumeRestGetApi(apiPath);
    try {
      ObjectNode[] bookingDetails = this.mapper.readValue(getResponse.body(), ObjectNode[].class);

      List<Booking> activeBookings = new ArrayList<>();
      for (ObjectNode booking : bookingDetails) {
        Booking bookingElement = this.mapper.treeToValue(booking, Booking.class);
        bookingElement.setCustomerId(booking.get("customer").get("id").asText());
        activeBookings.add(bookingElement);

      }
      return activeBookings;

    } catch (Exception exception) {
      System.out.println("Exceptions raised: getting List of test sites" + exception.getMessage());
    }
    return null;
  }

}
