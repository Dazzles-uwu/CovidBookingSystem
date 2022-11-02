package com.fit3077.covidbookingsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit3077.covidbookingsystem.api.ConsumeRestApiFacade;
import com.fit3077.covidbookingsystem.booking.Booking;

import com.fit3077.covidbookingsystem.booking.BookingAdditionalInfo;
import com.fit3077.covidbookingsystem.booking.BookingObserver;
import com.fit3077.covidbookingsystem.booking.bookingcodegenerator.VerifyGeneratedCodes;
import com.fit3077.covidbookingsystem.testsite.TestSite;

import com.fit3077.covidbookingsystem.testsite.TestSiteAdditionalInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class BookingService {

  private final ObjectMapper mapper;

  private final TestingSiteService testingSiteService;

  private final BookingVerifyGenerateCodeService bookingVerifyGenerateCodeService;

  private final ValidateBookingService validateBookingService;

  private final ModifyBookingService modifyBookingService;

  private TestSite testSite;

  private final BookingObserver bookingObserver = new BookingObserver();

  public BookingService(ObjectMapper mapper, TestingSiteService testingSiteService,
      BookingVerifyGenerateCodeService bookingVerifyGenerateCodeService,
      ValidateBookingService validateBookingService, ModifyBookingService modifyBookingService) {
    this.mapper = mapper;
    this.testingSiteService = testingSiteService;
    this.bookingVerifyGenerateCodeService = bookingVerifyGenerateCodeService;
    this.validateBookingService = validateBookingService;
    this.modifyBookingService = modifyBookingService;
  }

  public boolean bookOnSite(Booking booking) {
    booking.setTestSite(TestSite.builder().id(testSite.getId()).build());
    booking.setAdditionalInfo(BookingAdditionalInfo.builder().build());

    return createBooking(booking);
  }

  public boolean bookOffSite(Booking booking, String collectKitOptions) {
    testSite = null;
    Map<String, String> bookingCodesList = bookingVerifyGenerateCodeService.processOffsiteBookingCustomerCode(
        booking.getCustomerId());

    try {
      booking.setAdditionalInfo(mapper.readValue(mapper.writeValueAsString(bookingCodesList),
          BookingAdditionalInfo.class));

    } catch (Exception exception) {
      System.out.println("Exception raised generating QR codes" + exception.getMessage());
    }
    if (!(collectKitOptions == null || collectKitOptions.length() == 1)) {
      booking.getAdditionalInfo().setHasKitCollected(false);
    } else {
      booking.getAdditionalInfo().setHasKitCollected(true);
    }

    return createBooking(booking);

  }

  private boolean createBooking(Booking booking) {

    if (validateBookingService.validateBookingTime(booking, testSite)) {
      return false;
    }

    booking.setStartTime(booking.getStartTime() + ":00.000Z");
    Integer responseCode = ConsumeRestApiFacade.createNewBookings(booking);

    if (responseCode == 201) {
      if (booking.getTestSite() != null && booking.getTestSite().getId() != null) {
        testingSiteService.refreshTestSiteDetails();
      }
      return true;
    }
    return false;
  }

  public Boolean modifyBooking(Booking booking) {
    if (booking.getAdditionalInfo() == null) {
      booking.setAdditionalInfo(
          BookingAdditionalInfo.builder().previousBooking(new ArrayList<>()).build());
    }

    Booking originalBooking = getBookingById(booking);

    if (originalBooking.getAdditionalInfo().getPreviousBooking() != null) {
      booking.getAdditionalInfo().getPreviousBooking()
          .addAll(originalBooking.getAdditionalInfo().getPreviousBooking());
    }

    // validate date
    if (validateBookingService.validateBookingTime(booking, originalBooking.getTestSite())) {
      return false;
    }

    Boolean updateSuccessful = modifyBookingService.updateModifyBooking(booking);

    if (updateSuccessful) {

      if (booking.getAdditionalInfo().getPreviousBooking().size() == 3) {
        booking.getAdditionalInfo().getPreviousBooking().remove(0);
      }
      // Add the original booking time into the past since its now considered an older
      // version
      booking.getAdditionalInfo().getPreviousBooking()
          .add(Booking.builder().bookingId(originalBooking.getTestSite().getId()).startTime(
              originalBooking.getStartTime()).build());

      updateSuccessful = modifyBookingService.updateModifyBooking(booking);
    }

    return updateSuccessful;
  }

  public List<Booking> displayPastBookingsHtml(Booking booking) {
    Booking originalBooking = getBookingById(booking);
    if (originalBooking == null) {
      return new ArrayList<>();
    }

    List<Booking> pastBookings = originalBooking.getAdditionalInfo().getPreviousBooking();
    if (pastBookings == null) {
      return new ArrayList<>();
    }
    for (Booking pastBookElement : pastBookings) {
      pastBookElement.setTestSite(
          testingSiteService.findBySTestSiteId(pastBookElement.getBookingId()));
    }

    return pastBookings;
  }

  private Booking getBookingById(Booking booking) {
    return ConsumeRestApiFacade.findByBookingId(booking.getBookingId());
  }

  public Booking getBookingByIdAndValidate(Booking newBooking) {
    Booking originalBooking = getBookingById(newBooking);

    if (originalBooking == null || originalBooking.getStatus() == null) {
      return newBooking;
    }

    Boolean isNotValid = validateBookingService.validateBookingTime(originalBooking,
        originalBooking.getTestSite());

    if (isNotValid && !originalBooking.getStatus().contains("EXPIRED")) {
      ConsumeRestApiFacade.updateBookingStatus(originalBooking.getBookingId(), "EXPIRED");
      originalBooking.setStatus("EXPIRED");
    }

    return originalBooking;
  }

  public Booking verifyPinNumber(Booking booking) {
    return ConsumeRestApiFacade.getPinNumberAndVerify(booking, booking.getSmsPin());
  }

  public String verifyQRCode(Booking booking, String qrCode) {

    return ConsumeRestApiFacade.getBookingsAndFilterByQRCode(booking, qrCode);

  }

  public String confirmQRCode(String bookingID) {
    return ConsumeRestApiFacade.getQRCodeAndConfirm(bookingID);
  }

  public List<Booking> getAllCustomersActiveBookings(String customerId) {

    Booking[] bookings = ConsumeRestApiFacade.getAllCustomersBookings(customerId);

    List<Booking> activeBookings = Arrays.stream(bookings)
        .filter(booking -> booking.getTestSite() != null)
        .collect(Collectors.toList());

    return activeBookings;
  }

//  public List<Booking> getAllBookingsWithATestSite(String testSiteId) {
//
//    Booking[] bookings = ConsumeRestApiFacade.getAllBookinsWithATestSite(testSiteId);
//
//    List<Booking> activeBookings = Arrays.stream(bookings)
//        .filter(booking -> booking.getTestSite() != null)
//        .collect(Collectors.toList());
//
//    return activeBookings;
//  }

  public List<Booking> getAllBookings() {

    List<Booking> activeBookings = ConsumeRestApiFacade.getAllBookings();
    activeBookings = activeBookings.stream().filter(booking -> booking.getTestSite() != null)
        .collect(Collectors.toList());
    return activeBookings;

  }

  public boolean isBookingUpdated() {
    return bookingObserver.getChangesNotified();
  }

  public void setTestSite(TestSite testSite) {
    this.testSite = testSite;
  }

  public void setTestSite(String testSite) {
    this.testSite = testingSiteService.findBySTestSiteId(testSite);
  }

  public TestSite getTestSite() {
    return testSite;
  }


}
