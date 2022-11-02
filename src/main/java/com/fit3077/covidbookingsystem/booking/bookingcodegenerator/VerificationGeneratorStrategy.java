package com.fit3077.covidbookingsystem.booking.bookingcodegenerator;

public interface VerificationGeneratorStrategy {

  String generateCode(String patiendId);

  void verifyGeneratedCode();
}
