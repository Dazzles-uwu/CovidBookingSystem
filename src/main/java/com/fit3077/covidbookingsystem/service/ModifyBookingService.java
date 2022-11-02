package com.fit3077.covidbookingsystem.service;

import com.fit3077.covidbookingsystem.api.ConsumeRestApiFacade;
import com.fit3077.covidbookingsystem.booking.Booking;
import com.fit3077.covidbookingsystem.booking.BookingAdditionalInfo;
import com.fit3077.covidbookingsystem.testsite.TestSite;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ModifyBookingService {

  private final String BOOKING_CANCEL = "CANCELLED";

  public Boolean cancelBooking(Booking booking) {
    return updateBookingStatus(booking.getBookingId(), BOOKING_CANCEL);
  }


  public Boolean revertBooking(Booking booking) {
    // Using notes to store the testsite id then need to place it back
    booking.setTestSite(TestSite.builder().id(booking.getStatus()).build());

    if (booking.getAdditionalInfo() == null) {
      booking.setAdditionalInfo(
          BookingAdditionalInfo.builder().previousBooking(new ArrayList<>()).build());
    }

    Booking originalBooking = ConsumeRestApiFacade.findByBookingId(booking.getBookingId());

      if (originalBooking.getAdditionalInfo().getPreviousBooking() != null) {
          booking.getAdditionalInfo().getPreviousBooking()
              .addAll(originalBooking.getAdditionalInfo().getPreviousBooking());
      }

    Boolean updateSuccessful = updateModifyBooking(booking);

    if (updateSuccessful) {

      // Remove the reverted changes
      booking.getAdditionalInfo()
          .setPreviousBooking(booking.getAdditionalInfo().getPreviousBooking().stream()
              .filter(booking1 -> !booking1.getStartTime().equals(booking.getStartTime())
                  && !booking1.getBookingId().equals(booking.getStatus()))
              .collect(Collectors.toUnmodifiableList()));

      updateSuccessful = updateModifyBooking(booking);
    }

    return updateSuccessful;

  }

  public Boolean updateBookingStatus(String bookingId, String status) {
    Integer responseCode = ConsumeRestApiFacade.updateBookingStatus(bookingId, status);
    if (responseCode == 200) {
      return true;
    }
    return false;

  }

  public Boolean updateModifyBooking(Booking booking) {
    Integer responseCode = ConsumeRestApiFacade.updateModifyBooking(booking);
    if (responseCode == 200) {
      return true;
    }
    return false;

  }

  public Boolean deleteBooking(Booking booking) {
    Integer responseCode = ConsumeRestApiFacade.deleteBooking(booking);
    if (responseCode == 204) {
      return true;
    }
    return false;
  }

}
