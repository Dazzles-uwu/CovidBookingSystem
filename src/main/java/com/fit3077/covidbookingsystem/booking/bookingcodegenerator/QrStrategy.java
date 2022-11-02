package com.fit3077.covidbookingsystem.booking.bookingcodegenerator;

import java.util.UUID;

public class QrStrategy implements VerificationGeneratorStrategy {


  @Override
  public String generateCode(String patiendId) {
    return UUID.randomUUID().toString();
  }

  @Override
  public void verifyGeneratedCode() {

  }
}
