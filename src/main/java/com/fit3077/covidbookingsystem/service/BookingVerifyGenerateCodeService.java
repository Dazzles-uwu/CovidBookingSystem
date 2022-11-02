package com.fit3077.covidbookingsystem.service;

import com.fit3077.covidbookingsystem.booking.bookingcodegenerator.QrStrategy;
import com.fit3077.covidbookingsystem.booking.bookingcodegenerator.VerifyGeneratedCodes;
import com.fit3077.covidbookingsystem.booking.bookingcodegenerator.VideoUrlStrategy;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class BookingVerifyGenerateCodeService {

  private VerifyGeneratedCodes verifyGeneratedCodes;

  public BookingVerifyGenerateCodeService() {
    this.verifyGeneratedCodes = new VerifyGeneratedCodes(new QrStrategy());
  }

  public Map<String, String> processOffsiteBookingCustomerCode(String patiendId) {
    Map<String, String> jsonCodesMapped = new HashMap();
    verifyGeneratedCodes.changeStrategy(new QrStrategy());
    jsonCodesMapped.put("qrCode", verifyGeneratedCodes.generateValidCode(patiendId));
    verifyGeneratedCodes.changeStrategy(new VideoUrlStrategy());
    jsonCodesMapped.put("videoUrlCode", verifyGeneratedCodes.generateValidCode(patiendId));
    return jsonCodesMapped;
  }


}
