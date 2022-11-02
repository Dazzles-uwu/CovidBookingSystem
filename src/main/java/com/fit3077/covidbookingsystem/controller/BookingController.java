package com.fit3077.covidbookingsystem.controller;

import com.fit3077.covidbookingsystem.booking.Booking;
import com.fit3077.covidbookingsystem.service.BookingService;
import com.fit3077.covidbookingsystem.service.LoginService;
import com.fit3077.covidbookingsystem.service.TestingSiteService;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookingController {
    private final BookingService bookingService;
    private final LoginService loginService;
    private final TestingSiteService testingSiteService;

    public BookingController(BookingService bookingService, LoginService loginService, TestingSiteService testingSiteService) {
        this.bookingService = bookingService;
        this.loginService = loginService;
        this.testingSiteService = testingSiteService;
    }

    @GetMapping("/form/onsite/{id}")
    public String customerSelectsTestLocation(@ModelAttribute Booking booking,
                                              @PathVariable(value = "id", required = false) String testSiteID, Model model) {
        bookingService.setTestSite(testSiteID);
        booking.setCustomerId(loginService.getUser().getId());
        model.addAttribute("onSiteLocation", bookingService.getTestSite());

        return "Booking/customerOnSiteForm";
    }


    @GetMapping("/form/offsite")
    public String customerSelectsTestLocation(@ModelAttribute Booking booking, Model model) {
        booking.setCustomerId(loginService.getUser().getId());
        model.addAttribute("testSiteOptions", testingSiteService.displayHtml());

        return "Booking/customerOffSiteForm";
    }

    @GetMapping("/booking-active")
    public String viewCustomersAllActiveBookings(@ModelAttribute Booking booking, Model model,
        @RequestParam(value = "receptionistTestSite", required = false) String testSiteId) {

        List<Booking> activeBookings = new ArrayList<>();
        if (loginService.getUser().isReceptionist()) {
            activeBookings = bookingService.getAllBookings();
        } else if (loginService.getUser().isCustomer()) {
            activeBookings = bookingService.getAllCustomersActiveBookings(
                loginService.getUser().getId());
        }

        model.addAttribute("user", loginService.getUser());
        model.addAttribute("activeBookings", activeBookings);
        boolean bookingUpdated = false;

        bookingUpdated = bookingService.isBookingUpdated();
        model.addAttribute("update", bookingUpdated);

        return "Booking/customersActiveBooking";
    }

    @PostMapping("/book/offsite")
    public String offSiteBooking(@ModelAttribute Booking booking, Model model,
                                 @RequestParam(value = "action", required = false) String action,
                                 @RequestParam String collectKitOptions) {

        Boolean hasBookedSuccessfully = bookingService.bookOffSite(booking, collectKitOptions);

        if (hasBookedSuccessfully) {
            model.addAttribute("hasBookedSuccessfully", "true");
            System.out.println("booked succesfully");
        } else {
            model.addAttribute("hasBookedSuccessfully", "false");
        }

        model.addAttribute("user", loginService.getUser());
        model.addAttribute("onSiteLocation", bookingService.getTestSite());
        model.addAttribute("testSiteOptions", testingSiteService.displayHtml());

        if (action.equals("customer")) {
            return "Booking/customerOffSiteForm";
        }

        return "Users/userLoggedIn";
    }

    @PostMapping("/book/onsite")
    public String onSiteBooking(@ModelAttribute Booking booking,
                                Model model,
                                @RequestParam(value = "action", required = false) String action,
                                @RequestParam(value = "chosenTestSite", required = false) String testSiteId) {
        Boolean hasBookedSuccesfully = false;
        if (action.equals("receptionist") || action.equals("customer")) {
            hasBookedSuccesfully = bookingService.bookOnSite(booking);
        }

        if (action.equals("modify")){
            model.addAttribute("bookingModify","true");
            model.addAttribute("testSite",testingSiteService.displayHtml());
            booking.setTestSite(testingSiteService.findBySTestSiteId(testSiteId));
            hasBookedSuccesfully = bookingService.modifyBooking(booking);
            model.addAttribute("pastBookings",bookingService.displayPastBookingsHtml(booking));
        }


        if (hasBookedSuccesfully) {
            model.addAttribute("hasBookedSuccessfully", "true");
            System.out.println("booked succesfully or modified booking succesfully");

        } else {
            model.addAttribute("hasBookedSuccessfully", "false");
        }

        model.addAttribute("user", loginService.getUser());
        model.addAttribute("onSiteLocation", bookingService.getTestSite());

        if (action.equals("customer")|| action.equals("modify")) {
            return "Booking/customerOnSiteForm";
        }

        return "Users/userLoggedIn";
    }

}
