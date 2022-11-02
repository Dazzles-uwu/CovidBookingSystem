package com.fit3077.covidbookingsystem.controller;

import com.fit3077.covidbookingsystem.service.TestingSiteService;
import com.fit3077.covidbookingsystem.testsite.TestSiteFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TestSiteController {

  private final TestingSiteService testingSiteService;

  public TestSiteController(TestingSiteService testingSiteService) {
    this.testingSiteService = testingSiteService;
  }

  @PostMapping({"/filter/test-sites", "/filter/user/test-sites"})
  public String filterTestSites(@ModelAttribute TestSiteFilter testSiteFilter, Model model,
      @RequestParam(value = "action", required = false) String action,
      HttpServletRequest httpServletRequest) {

    if (action.equals("Filter")) {
      testingSiteService.filterLocations(testSiteFilter);
    } else if (action.equals("Clear")) {
      testingSiteService.refreshTestSiteDetails();
    }
      if (httpServletRequest.getRequestURI().equals("/filter/user/test-sites")) {
          return "redirect:/validUser";
      }

    return "redirect:/";
  }
}
