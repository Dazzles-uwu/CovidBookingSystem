package com.fit3077.covidbookingsystem;


import com.fit3077.covidbookingsystem.loginsystem.User;

import com.fit3077.covidbookingsystem.service.TestingSiteService;
import com.fit3077.covidbookingsystem.testsite.TestSiteFilter;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationController {

  private final TestingSiteService testingSiteService;


  public ApplicationController(TestingSiteService testingSiteService) {
    this.testingSiteService = testingSiteService;
  }


  @GetMapping("/")
  public String startApp(Model model) {

    model.addAttribute("user", new User());
    model.addAttribute("testSiteFilter", new TestSiteFilter());
    model.addAttribute("testSite", testingSiteService.displayHtml());

    return "home"; // HTML NAME
  }

}