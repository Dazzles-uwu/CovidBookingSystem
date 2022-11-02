package com.fit3077.covidbookingsystem.loginsystem;

import com.fit3077.covidbookingsystem.APIKEY;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;

public class LoginVerify {

  private String payload = "";

  public LoginVerify(@NotNull String jwt) {

    String[] chunks = jwt.split("\\.");

    Base64.Decoder decoder = Base64.getUrlDecoder();

    //String header = new String(decoder.decode(chunks[0]));
    payload = new String(decoder.decode(chunks[1]));
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }
}
