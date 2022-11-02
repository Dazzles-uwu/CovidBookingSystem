package com.fit3077.covidbookingsystem.service;

import com.fit3077.covidbookingsystem.api.ConsumeRestApiFacade;
import com.fit3077.covidbookingsystem.covidtest.CovidTest;
import com.fit3077.covidbookingsystem.loginsystem.User;
import org.springframework.stereotype.Service;

@Service
public class CovidTestService {

  private final String PROCESSED_STATUS = "PROCESSED";

  private final ModifyBookingService modifyBookingService;

  public CovidTestService(ModifyBookingService modifyBookingService) {
    this.modifyBookingService = modifyBookingService;
  }

  public boolean conductTest(CovidTest covidTest, User user) {
    Integer responseCode = ConsumeRestApiFacade.createNewCovidTest(covidTest, user);

    if (responseCode != 201) {
      return false;
    }
    if (!covidTest.getBookingId().equals("")) {
      boolean isPatchedBooking = modifyBookingService.updateBookingStatus(covidTest.getBookingId(),
          PROCESSED_STATUS);
      if (isPatchedBooking) {
        System.out.println("Was able to patch an existing booking");
      }
    }

    return true;
  }


}
