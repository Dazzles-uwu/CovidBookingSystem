package com.fit3077.covidbookingsystem.service;

import com.fit3077.covidbookingsystem.covidtest.CovidType;
import com.fit3077.covidbookingsystem.covidtest.CovidTest;
import com.fit3077.covidbookingsystem.covidtest.InterviewFormAnswers;
import com.fit3077.covidbookingsystem.covidtest.InterviewFormDecision;
import org.springframework.stereotype.Service;

@Service
public class InterviewFormService {

  private CovidTest covidTest;

  public CovidTest reviewQuestions(InterviewFormAnswers interviewFormAnswers, CovidTest covidTest) {

    String decision = InterviewFormDecision.testTypeDecision(interviewFormAnswers).toUpperCase();

    if (decision.equals(CovidType.PCR.getCovidType())) {
      covidTest.setType(CovidType.PCR.getCovidType());
    } else if (decision.equals(CovidType.RAT.getCovidType())) {
      covidTest.setType(CovidType.RAT.getCovidType());
    } else {
      System.out.println("invalid");
    }

    this.covidTest = covidTest;
    return covidTest;
  }

  public CovidTest getCovidTest() {
    return covidTest;
  }
}
