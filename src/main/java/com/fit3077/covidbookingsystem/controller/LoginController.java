package com.fit3077.covidbookingsystem.controller;

import com.fit3077.covidbookingsystem.APIKEY;
import com.fit3077.covidbookingsystem.booking.Booking;
import com.fit3077.covidbookingsystem.covidtest.CovidTest;
import com.fit3077.covidbookingsystem.covidtest.InterviewFormAnswers;
import com.fit3077.covidbookingsystem.loginsystem.User;
import com.fit3077.covidbookingsystem.service.BookingService;
import com.fit3077.covidbookingsystem.service.LoginService;
import com.fit3077.covidbookingsystem.service.TestingSiteService;
import com.fit3077.covidbookingsystem.testsite.TestSiteFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class LoginController {

  private final LoginService loginService;

  private final TestingSiteService testingSiteService;

  private final BookingService bookingService;

  public LoginController(LoginService loginService, TestingSiteService testingSiteService,
      BookingService bookingService) {
    this.loginService = loginService;
    this.testingSiteService = testingSiteService;
    this.bookingService = bookingService;
  }

  @PostMapping("/login")
  public String loginForm(@ModelAttribute User user, Model model) {

    if (!loginService.login(user)) {
      return "redirect:/";
    }
    bookingService.setTestSite(testingSiteService.findByUserTestingSite(user));
    model.addAttribute("testSiteFilter", new TestSiteFilter());
    model.addAttribute("user", user);
    model.addAttribute("testSite", testingSiteService.displayHtml());
    model.addAttribute("booking", new Booking());
    model.addAttribute("onSiteLocation", bookingService.getTestSite());
    model.addAttribute("interviewFormAnswers", new InterviewFormAnswers());
    model.addAttribute("covidTest", new CovidTest());
    //model.addAttribute("bookings", bookingService.getAllBookings());

    return "Users/userLoggedIn";
  }

  @GetMapping("/validUser")
  public String validUserLoadModels(Model model) {

    model.addAttribute("testSiteFilter", new TestSiteFilter());
    model.addAttribute("user", loginService.getUser());
    model.addAttribute("testSite", testingSiteService.displayHtml());
    model.addAttribute("booking", new Booking());
    model.addAttribute("onSiteLocation", bookingService.getTestSite());
    model.addAttribute("interviewFormAnswers", new InterviewFormAnswers());

    return "Users/userLoggedIn";
  }
}
