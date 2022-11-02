package com.fit3077.covidbookingsystem.booking;

import com.fit3077.covidbookingsystem.api.ConsumeRestApiFacade;

import java.util.List;

public class BookingObserver {
    BookingSubject bookingObject = new BookingSubject();

    public boolean getChangesNotified() {
        //Subscribes for any state changes

        return bookingObject.updateChanges();
    }
}
