package com.fit3077.covidbookingsystem.controller;

import com.fit3077.covidbookingsystem.covidtest.CovidTest;
import com.fit3077.covidbookingsystem.covidtest.InterviewFormAnswers;
import com.fit3077.covidbookingsystem.service.*;
import com.fit3077.covidbookingsystem.testsite.TestSiteFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CovidTestController {

  private final TestingSiteService testingSiteService;

  private final LoginService loginService;


  private final InterviewFormService interviewFormService;

  private final CovidTestService covidTestService;

  public CovidTestController(TestingSiteService testingSiteService, LoginService loginService,
      InterviewFormService interviewFormService, CovidTestService covidTestService) {
    this.testingSiteService = testingSiteService;
    this.loginService = loginService;
    this.interviewFormService = interviewFormService;
    this.covidTestService = covidTestService;
  }

  @PostMapping("/interviewformanswers")
  public String interviewLoginForm(@ModelAttribute InterviewFormAnswers interviewFormAnswers,
      CovidTest covidTest, Model model) {
    model.addAttribute("testSiteFilter", new TestSiteFilter());
    model.addAttribute("user", loginService.getUser());
    model.addAttribute("testSite", testingSiteService.displayHtml());
    model.addAttribute("interviewFormAnswers", interviewFormAnswers);
    model.addAttribute("covidTest",
        interviewFormService.reviewQuestions(interviewFormAnswers, covidTest));

    return "CovidTest/covidTest";
  }

  @PostMapping("/covidtest")
  public String covidTestForm(@ModelAttribute CovidTest covidTest, Model model) {
    boolean hasCreatedCovidTest = false;

    hasCreatedCovidTest = covidTestService.conductTest(covidTest, loginService.getUser());

    model.addAttribute("user", loginService.getUser());

    if (!hasCreatedCovidTest) {
      model.addAttribute("hasBookedSuccessfully", "false");
      covidTest.setType(interviewFormService.getCovidTest().getType());
      return "CovidTest/covidTest";
    }

    return "redirect:/validUser";
  }
}
