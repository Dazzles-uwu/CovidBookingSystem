package com.fit3077.covidbookingsystem.covidtest;

public class InterviewFormDecision {

  public static String testTypeDecision(InterviewFormAnswers answers) {
    System.out.println(answers.getSymptomatic());
    System.out.println(answers.getCloseContact());
    System.out.println(answers.getTravelled());

    if (answers.getSymptomatic().toLowerCase().equals("yes") || answers.getTravelled().toLowerCase()
        .equals("yes")) {
      return CovidType.PCR.getCovidType().toUpperCase();
    }
    return CovidType.RAT.getCovidType().toUpperCase();
  }
}
