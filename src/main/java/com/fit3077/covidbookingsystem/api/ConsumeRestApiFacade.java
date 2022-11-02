package com.fit3077.covidbookingsystem.api;

import com.fit3077.covidbookingsystem.booking.Booking;
import com.fit3077.covidbookingsystem.covidtest.CovidTest;
import com.fit3077.covidbookingsystem.loginsystem.User;
import com.fit3077.covidbookingsystem.testsite.TestSite;

import java.net.http.HttpResponse;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class ConsumeRestApiFacade {


  private static BookingApi bookingApi = new BookingApi();
  private static TestSiteApi testSiteApi = new TestSiteApi();

  private static CovidTestApi covidTestApi = new CovidTestApi();

  private static UserApi userApi = new UserApi();


  @Bean
  @Scope("singleton")
  public ConsumeRestApiFacade consumeRestApiFacadeSingleton() {
    return new ConsumeRestApiFacade();
  }

  public static String getBookingsAndFilterByQRCode(Booking booking, String qrCode) {
    return bookingApi.getBookingsAndFilterByQRCode(booking, qrCode);
  }

  public static Integer createNewBookings(Booking booking) {
    return bookingApi.postCreateNewBookings(booking);
  }

  public static Booking getPinNumberAndVerify(Booking booking, String smsPin) {
    return bookingApi.getPinNumberAndVerify(booking, smsPin);
  }

  public static String getQRCodeAndConfirm(String bookingId) {
    return bookingApi.getQRCodeAndConfirm(bookingId);
  }

  public static List<TestSite> getAllTestSites() {
    return testSiteApi.getAllTestSitesWithBookings();
  }

  public static List<Booking> getAllBookings() {
    return bookingApi.getAllBookings();
  }

  public static String getOneTestSite(String testSiteID, Integer waitTime) {
    return testSiteApi.getOneTestSite(testSiteID, waitTime);
  }
//  public static Booking[] getAllBookinsWithATestSite(String testSiteID){
//    return testSiteApi.getAllBookingsWithATestSite(testSiteID);
//  }

  public static Integer patchWaitingTimeForTestSite(String testSiteID, String jsonBody) {
    return testSiteApi.patchWaitingTimeForTestSite(testSiteID, jsonBody);
  }

  public static Integer createNewCovidTest(CovidTest covidTest, User user) {
    return covidTestApi.createNewCovidTest(covidTest, user);

  }

  public static HttpResponse<String> userLogin(User user) {
    return userApi.login(user);
  }

  public static Integer updateBookingStatus(String bookingId, String status) {
    return bookingApi.updateBookingStatus(bookingId, status);
  }

  public static Integer updateModifyBooking(Booking booking) {
    return bookingApi.updateModifiedBookingStatus(booking);
  }

  public static Integer deleteBooking(Booking booking) {
    return bookingApi.deleteBooking(booking.getBookingId());
  }

  public static Booking[] getAllCustomersBookings(String customerId) {
    return userApi.getCustomerActiveBooking(customerId);
  }

  public static Booking findByBookingId(String bookingId) {
    return bookingApi.findByBookingId(bookingId);
  }

}
