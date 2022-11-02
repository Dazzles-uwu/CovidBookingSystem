package com.fit3077.covidbookingsystem.service;

import com.fit3077.covidbookingsystem.booking.Booking;
import com.fit3077.covidbookingsystem.testsite.TestSite;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class ValidateBookingService {

  public boolean validateBookingTime(Booking booking, TestSite testSite) {

    Date currentDate = new Date();

    Date bookDate = null;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        Locale.ENGLISH);

    try {
      bookDate = formatter.parse(booking.getStartTime() + ":00.000Z");

      // just need to grab time and compare if between opening and closeing time of the test site
      int bookDateHour = bookDate.getHours();
      int bookDateMinutes = bookDate.getMinutes();
      String bookTimeString = String.format("%02d:%02d", bookDateHour, bookDateMinutes);

      Calendar calendarBookTime = Calendar.getInstance();
      calendarBookTime.setTime(new SimpleDateFormat("HH:mm").parse(bookTimeString));
      calendarBookTime.add(Calendar.DATE, 1);

      // if null implies its offsite
      if (testSite == null) {
        Date testSiteOpenTime = new SimpleDateFormat("HH:mm:ss").parse("08:00:00");
        Calendar calendarTestSiteOpenTime = Calendar.getInstance();
        calendarTestSiteOpenTime.setTime(testSiteOpenTime);
        calendarTestSiteOpenTime.add(Calendar.DATE, 1);

        Date testSiteCloseTime = new SimpleDateFormat("HH:mm:ss").parse("18:00:00");
        if (validateDuringOpeningHours(calendarBookTime, calendarTestSiteOpenTime,
            testSiteCloseTime)) {
          return true;
        }

      } else {
        Date testSiteOpenTime = new SimpleDateFormat("HH:mm:ss").parse(
            testSite.getAdditionalInfo().getOpenTime());
        Calendar calendarTestSiteOpenTime = Calendar.getInstance();
        calendarTestSiteOpenTime.setTime(testSiteOpenTime);
        calendarTestSiteOpenTime.add(Calendar.DATE, 1);

        Date testSiteCloseTime = new SimpleDateFormat("HH:mm:ss").parse(
            testSite.getAdditionalInfo().getClosingTime());
        if (validateDuringOpeningHours(calendarBookTime, calendarTestSiteOpenTime,
            testSiteCloseTime)) {
          return true;
        }
      }

    } catch (Exception exception) {
      System.out.println("Could not parse date" + exception.getMessage());
    }

    if (bookDate == null) {
      return true;
    }

    return !(bookDate.after(currentDate));


  }

  private boolean validateDuringOpeningHours(Calendar calendarBookTime,
      Calendar calendarTestSiteOpenTime, Date testSiteCloseTime) {
    Calendar calendarTestSiteCloseTime = Calendar.getInstance();
    calendarTestSiteCloseTime.setTime(testSiteCloseTime);
    calendarTestSiteCloseTime.add(Calendar.DATE, 1);

    if (!(calendarBookTime.getTime().after(calendarTestSiteOpenTime.getTime())
        && calendarBookTime.getTime().before(calendarTestSiteCloseTime.getTime()))) {
      System.out.println("Time is not between the opening time and closing time");
      return true;
    }
    return false;
  }
}
