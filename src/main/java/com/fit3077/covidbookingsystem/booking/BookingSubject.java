package com.fit3077.covidbookingsystem.booking;

import static java.util.stream.Collectors.toSet;

import com.fit3077.covidbookingsystem.api.ConsumeRestApiFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookingSubject {

  private List<Booking> previousBooking;

  private boolean ifPreviousBookingsEmpty() {
    return previousBooking == null;
  }

  public boolean updateChanges() {
    if (ifPreviousBookingsEmpty() || previousBooking.size() == 0) {
      previousBooking = ConsumeRestApiFacade.getAllBookings().stream()
          .filter(booking -> booking.getTestSite() != null)
          .collect(Collectors.toList());
      return false;
    } else {
      List<Booking> retrievedBookings = ConsumeRestApiFacade.getAllBookings().stream()
          .filter(booking -> booking.getTestSite() != null)
          .collect(Collectors.toList());


      boolean equalLists = true;

      Integer counter = 0;
      for(Booking previousBookingElem: previousBooking){
        for (Booking retrievedBookingsElem: retrievedBookings){

          if (previousBookingElem.getBookingId().equals(retrievedBookingsElem.getBookingId())) {
            counter += 1;
            if (!previousBookingElem.getStatus().equals(retrievedBookingsElem.getStatus()) ||
                !previousBookingElem.getStartTime().equals(retrievedBookingsElem.getStartTime()) ||
                !previousBookingElem.getTestSite().getId()
                    .equals(retrievedBookingsElem.getTestSite().getId())) {
              equalLists = false;
              System.out.println("Booking MODIFICATION HAS OCCURRED");
              break;
            }
          }

        }
      }
      if (previousBooking.size() != counter){
        equalLists = false;
      }


      if (!equalLists) {
        previousBooking = retrievedBookings;
        return true;
      }
      return false;
    }
  }


}
