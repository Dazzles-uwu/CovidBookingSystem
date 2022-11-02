package com.fit3077.covidbookingsystem.booking.bookingcodegenerator;

public class VideoUrlStrategy implements VerificationGeneratorStrategy {


  @Override
  public String generateCode(String patientId) {
    return "videoUrl.com.au/" + patientId;
  }

  @Override
  public void verifyGeneratedCode() {

  }
}
