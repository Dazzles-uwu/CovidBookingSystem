package com.fit3077.covidbookingsystem.controller;

import com.fit3077.covidbookingsystem.booking.Booking;
import com.fit3077.covidbookingsystem.service.BookingService;
import com.fit3077.covidbookingsystem.service.LoginService;
import com.fit3077.covidbookingsystem.service.TestingSiteService;
import com.fit3077.covidbookingsystem.testsite.TestSiteFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VerifyBookingController {

  private final BookingService bookingService;
  private final LoginService loginService;

  private final TestingSiteService testingSiteService;

  public VerifyBookingController(BookingService bookingService, LoginService loginService,
      TestingSiteService testingSiteService) {
    this.bookingService = bookingService;
    this.loginService = loginService;
    this.testingSiteService = testingSiteService;
  }


  @PostMapping("/verify-pin")
  public String staffVerifyPin(@ModelAttribute Booking booking, Model model,
      @RequestParam(value = "action", required = false) String action) {

    Booking originalBooking = bookingService.verifyPinNumber(booking);

    boolean isInitiatedStatus = originalBooking.getStatus().contains("INITIATED");

    model.addAttribute("booking", originalBooking);
    model.addAttribute("checkBookingStatus", true);
    model.addAttribute("patientBookingTime", originalBooking.getStartTime());
    model.addAttribute("bookingStatus", originalBooking.getStatus());
    model.addAttribute("isInitiatedStatus", isInitiatedStatus);
    model.addAttribute("user", loginService.getUser());
    model.addAttribute("onSiteLocation", bookingService.getTestSite());
    model.addAttribute("testSiteFilter", new TestSiteFilter());
    model.addAttribute("testSite", testingSiteService.displayHtml());
    model.addAttribute("bookingModify", isInitiatedStatus ? "true" : "false");
    model.addAttribute("pastBookings", bookingService.displayPastBookingsHtml(originalBooking));
    return "userLoggedIn";
  }

  @PostMapping("/verify-qr")
  public String staffVerifyQrCode(@ModelAttribute Booking booking, Model model,
      @RequestParam String qrCode) {

    String qrStatus = bookingService.verifyQRCode(booking, qrCode);

    model.addAttribute("checkQRStatus",
        qrStatus.equals("QR Code is valid and kit can be collected"));
    model.addAttribute("bookingIdSaved", booking.getCustomerId());
    model.addAttribute("qrStatus", qrStatus);
    model.addAttribute("user", loginService.getUser());
    model.addAttribute("onSiteLocation", bookingService.getTestSite());

    return "Users/userLoggedIn";
  }

  @PostMapping("/verify-bookingId")
  public String staffVerifyBookingId(@ModelAttribute Booking booking, Model model,
      @RequestParam String bookingId) {

    Booking originalBooking = bookingService.getBookingByIdAndValidate(
        Booking.builder().bookingId(bookingId).build());
    Boolean findBooking = originalBooking.getStatus() != null;
      if (!findBooking) {
          originalBooking = Booking.builder().status("").startTime("").build();
      } else {
          model.addAttribute("booking", originalBooking);
      }

    Boolean isInitiatedStatus = originalBooking.getStatus().contains("INITIATED");

    model.addAttribute("checkBookingStatus", findBooking);
    model.addAttribute("bookingStatus", "Booking STATUS: " + originalBooking.getStatus());
    model.addAttribute("patientBookingTime", originalBooking.getStartTime());
    model.addAttribute("isInitiatedStatus", isInitiatedStatus);
    model.addAttribute("user", loginService.getUser());
    model.addAttribute("onSiteLocation", bookingService.getTestSite());
    model.addAttribute("bookingModify", isInitiatedStatus ? "true" : "false");
    model.addAttribute("testSite", testingSiteService.displayHtml());
    model.addAttribute("pastBookings", bookingService.displayPastBookingsHtml(originalBooking));

    return "Users/userLoggedIn";
  }

  @PostMapping("/confirm-qr")
  public String staffConfirmQrCode(@ModelAttribute Booking booking, Model model,
      @RequestParam String bookingID) {
    String qrStatus = bookingService.confirmQRCode(bookingID);

    model.addAttribute("ifConfirmQrStatus", true);

    model.addAttribute("confirmQrStatus", qrStatus);
    model.addAttribute("user", loginService.getUser());
    model.addAttribute("onSiteLocation", bookingService.getTestSite());

    return "Users/userLoggedIn";
  }
}
