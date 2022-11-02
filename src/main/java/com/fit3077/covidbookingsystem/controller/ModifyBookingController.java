package com.fit3077.covidbookingsystem.controller;

import com.fit3077.covidbookingsystem.booking.Booking;
import com.fit3077.covidbookingsystem.service.BookingService;
import com.fit3077.covidbookingsystem.service.LoginService;
import com.fit3077.covidbookingsystem.service.ModifyBookingService;
import com.fit3077.covidbookingsystem.service.TestingSiteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ModifyBookingController {

  private final BookingService bookingService;
  private final ModifyBookingService modifyBookingService;

  private final LoginService loginService;

  private final TestingSiteService testingSiteService;


  public ModifyBookingController(
      BookingService bookingService, ModifyBookingService modifyBookingService,
      LoginService loginService,
      TestingSiteService testingSiteService) {
    this.bookingService = bookingService;
    this.modifyBookingService = modifyBookingService;
    this.loginService = loginService;
    this.testingSiteService = testingSiteService;
  }


  @PostMapping("/book/cancel")
  public String cancelBooking(@ModelAttribute Booking booking,
      Model model,
      @RequestParam(value = "action", required = false) String action,
      @RequestParam(value = "cancelOption", required = true) String cancelOption) {

    model.addAttribute("cancelBooking", "true");
    if (cancelOption.equals("Y")) {
      Boolean hasCancelledSuccessfully = modifyBookingService.cancelBooking(booking);
      model.addAttribute("hasCancelledSuccessfully", hasCancelledSuccessfully ? "true" : "false");
      return "Booking/customerOnSiteForm";
    }

    return "Booking/customerOnSiteForm";
  }

  @PostMapping("/book/revert")
  public String revertBooking(@ModelAttribute Booking booking,
      Model model,
      @RequestParam(value = "revertOption") String cancelOption) {

    model.addAttribute("revertBooking", "true");
    if (cancelOption.equals("Y")) {
      Boolean hasRevertedSuccessfully = modifyBookingService.revertBooking(booking);
      model.addAttribute("hasReversedSuccessfully", hasRevertedSuccessfully ? "true" : "false");
      return "Booking/customerOnSiteForm";
    }

    return "Booking/customerOnSiteForm";
  }

  @PostMapping("/book/delete")
  public String deleteBooking(@ModelAttribute Booking booking,
      Model model,
      @RequestParam(value = "deleteOption") String cancelOption) {

    model.addAttribute("deleteBooking", "true");
    if (cancelOption.equals("Y")) {
      Boolean hasDeletedSuccessfully = modifyBookingService.deleteBooking(booking);
      model.addAttribute("hasDeletedSuccessfully", hasDeletedSuccessfully ? "true" : "false");
      return "Booking/customerOnSiteForm";
    }

    return "Booking/customerOnSiteForm";
  }

  @GetMapping("/form/onsite/modify/{id}")
  public String customerModifiesFormBooking(@ModelAttribute Booking booking,
      @PathVariable(value = "id", required = false) String bookingId, Model model) {
//        bookingService.setTestSite(testSiteID);
//        booking.setTestSite(TestSite.builder().build());
    booking.setBookingId(bookingId);
    booking.setCustomerId(loginService.getUser().getId());
    //model.addAttribute("onSiteLocation", bookingService.getTestSite());
    model.addAttribute("pastBookings", bookingService.displayPastBookingsHtml(booking));
    model.addAttribute("bookingModify", "true");
    model.addAttribute("testSite", testingSiteService.displayHtml());

    return "Booking/customerOnSiteForm";
  }

  @GetMapping("/form/onsite/cancel/{id}")
  public String customerCancelFormBooking(@ModelAttribute Booking booking,
      @PathVariable(value = "id", required = false) String bookingId, Model model) {

    booking.setBookingId(bookingId);
    booking.setCustomerId(loginService.getUser().getId());
    model.addAttribute("cancelBooking", "true");

    return "Booking/customerOnSiteForm";
  }

  @GetMapping("/form/onsite/delete/{id}")
  public String receptionistDeleteFormBooking(@ModelAttribute Booking booking,
      @PathVariable(value = "id", required = false) String bookingId, Model model) {

    booking.setBookingId(bookingId);
    booking.setCustomerId(loginService.getUser().getId());
    model.addAttribute("deleteBooking", "true");

    return "Booking/customerOnSiteForm";
  }

  @PostMapping("/form/onsite/revert/{id}")
  public String customerRevertFormBooking(@ModelAttribute Booking booking,
      @PathVariable(value = "id", required = false) String bookingId, Model model) {

    booking.setBookingId(bookingId);
    booking.setCustomerId(loginService.getUser().getId());
    model.addAttribute("revertBooking", "true");

    return "Booking/customerOnSiteForm";
  }

}
