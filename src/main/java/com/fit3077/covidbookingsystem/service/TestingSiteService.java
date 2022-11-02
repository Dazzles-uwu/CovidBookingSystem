package com.fit3077.covidbookingsystem.service;

import com.fit3077.covidbookingsystem.api.ConsumeRestApiFacade;
import com.fit3077.covidbookingsystem.booking.Booking;
import com.fit3077.covidbookingsystem.loginsystem.User;
import com.fit3077.covidbookingsystem.testsite.TestSite;
import com.fit3077.covidbookingsystem.testsite.TestSiteFilter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TestingSiteService {

  private final int WAITING_TIME = 30;

  private List<TestSite> allTestingSites;

  private boolean ifTestingSitesEmpty() {
    return allTestingSites == null;
  }

  private List<TestSite> getAllTestingSites() {
    return allTestingSites;
  }

  private void setAllTestingSites(List<TestSite> allTestingSites) {
    this.allTestingSites = allTestingSites;
  }

  public List<TestSite> displayHtml() {
    if (this.ifTestingSitesEmpty()) {
      return this.findByAllTestingSiteLocations();
    } else {
      return this.getAllTestingSites();
    }
  }

  public void refreshTestSiteDetails() {
    findByAllTestingSiteLocations();
  }

  public TestSite findBySTestSiteId(String testSiteId) {
    for (TestSite testSite : findByAllTestingSiteLocations()) {
      if (testSite.getId().equals(testSiteId)) {
        return testSite;
      }
    }
    return null;
  }

  public void filterLocations(TestSiteFilter testSiteFilter) {

    List<TestSite> list = allTestingSites;

    if (testSiteFilter.getSuburbName() != null) {
      list = list.stream()
          .filter(testSite -> testSite.getAddress().getSuburb().toLowerCase().contains(
              testSiteFilter.getSuburbName().toLowerCase())).collect(
              Collectors.toList());
    }

    Iterator<TestSite> testSiteIterator = list.iterator();
    if (testSiteFilter.getFacilityType().size() != 0) {
      testSiteIterator = list.iterator();
      while (testSiteIterator.hasNext()) {
        TestSite testSite = testSiteIterator.next();
        if (Collections.disjoint(testSite.getAdditionalInfo().getAvailableFacilityType(),
            testSiteFilter.getFacilityType())) {
          testSiteIterator.remove();
        }
      }
    }

    System.out.println("Filtered test sites");

    setAllTestingSites(list);

  }

  /**
   * Returns receptionist/adminstrators testsite location
   *
   * @param actualUser
   * @return
   */
  public TestSite findByUserTestingSite(User actualUser) {
    if (!(actualUser.isReceptionist() || actualUser.isHealthcareWorker())) {
      return null;
    }
    findByAllTestingSiteLocations();
    // Filter for the first time
    for (TestSite testSite : getAllTestingSites()) {
      for (User findUser : testSite.getAdditionalInfo().getTestSiteWorkers()) {
        if (findUser.getId() == null || actualUser.getId() == null) {
          System.out.println("Error either find user or actual user is null");
          continue;
        }
        if (findUser.getId().equals(actualUser.getId())) {
          return testSite;
        }
      }
    }

    return null;
  }

  public boolean updateTestSiteWaitingTime(String testSiteId, Integer waitTime) {

    String jsonBody = ConsumeRestApiFacade.getOneTestSite(testSiteId, waitTime);
    Integer postResponseCode = ConsumeRestApiFacade.patchWaitingTimeForTestSite(testSiteId,
        jsonBody);

    if (postResponseCode == 200) {
      return true;
    }

    return false;
  }

  private List<TestSite> findByAllTestingSiteLocations() {

    List<TestSite> responseList = ConsumeRestApiFacade.getAllTestSites();

    for (TestSite testSite : responseList) {
      int waitTime = 0;
      for (Booking booking : testSite.getBookings()) {
        if (validateStartTimeForCurrentDate(booking.getStartTime())) {
          System.out.println("Test Site ID " + testSite.getId() + " + 30 mins");
          waitTime += WAITING_TIME;
        }
      }
      if (!(testSite.getAdditionalInfo().getWaitingTime().replaceAll("[^0-9]", "")
          .equals(String.valueOf(waitTime)))) {
        testSite.getAdditionalInfo().setWaitingTime(Integer.valueOf(waitTime) + " minutes");
        updateTestSiteWaitingTime(testSite.getId(), waitTime);
      }

    }

    setAllTestingSites(responseList);
    return allTestingSites;
  }

  private boolean validateStartTimeForCurrentDate(String bookingDate) {

    try {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
          Locale.ENGLISH);

      Date currentbookingDate = formatter.parse(bookingDate);

      Date currentDateTime = new Date();

      currentbookingDate.after(currentDateTime);

      currentDateTime.getDate();

      Calendar calendar = Calendar.getInstance();
      calendar.setTime(currentDateTime);
      calendar.add(Calendar.DAY_OF_YEAR, 1);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      Date nextDate = calendar.getTime();

      return currentbookingDate.after(currentDateTime) && currentbookingDate.before(nextDate);
    } catch (Exception exception) {
      System.out.println("Error Processing dates");
    }

    return false;
  }
}



