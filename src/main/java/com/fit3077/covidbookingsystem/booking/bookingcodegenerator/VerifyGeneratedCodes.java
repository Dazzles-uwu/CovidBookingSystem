package com.fit3077.covidbookingsystem.booking.bookingcodegenerator;

/**
 * Awful name need to change class name once can think of something
 */

public class VerifyGeneratedCodes {

  private VerificationGeneratorStrategy verificationGeneratorStrategy;

  public VerifyGeneratedCodes(VerificationGeneratorStrategy strategy) {
    this.verificationGeneratorStrategy = strategy;
  }

  public void changeStrategy(VerificationGeneratorStrategy strategy) {
    this.verificationGeneratorStrategy = strategy;
  }

  public String generateValidCode(String patiendId) {
    return verificationGeneratorStrategy.generateCode(patiendId);
  }

  public void verifyValidCode() {
    verificationGeneratorStrategy.verifyGeneratedCode();
  }

}
