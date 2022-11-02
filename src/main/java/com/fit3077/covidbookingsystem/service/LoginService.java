package com.fit3077.covidbookingsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fit3077.covidbookingsystem.APIKEY;
import com.fit3077.covidbookingsystem.api.ConsumeRestApiAbstract;
import com.fit3077.covidbookingsystem.api.ConsumeRestApiFacade;
import com.fit3077.covidbookingsystem.loginsystem.LoginVerify;
import com.fit3077.covidbookingsystem.loginsystem.User;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class LoginService {

  private User user;

  public boolean login(User user) {

    try {
      //this.user = ConsumeRestApiFacade.userLogin(user);
      HttpResponse<String> response = ConsumeRestApiFacade.userLogin(user);

      if (response.statusCode() == 200) {
        System.out.println("User is valid");
        ObjectNode responseNode = new ObjectMapper().readValue(response.body(), ObjectNode.class);

        LoginVerify loginVerify = new LoginVerify(responseNode.get("jwt").textValue());
        System.out.println(loginVerify.getPayload());
        ObjectNode payloadNode = new ObjectMapper().readValue(loginVerify.getPayload(),
            ObjectNode.class);
        user.setId(payloadNode.get("sub").textValue());
        user.setFirstName(payloadNode.get("givenName").textValue());
        user.setLastName(payloadNode.get("familyName").textValue());

        user.setCustomer(payloadNode.get("isCustomer").asBoolean());
        user.setHealthcareWorker(payloadNode.get("isHealthcareWorker").asBoolean());
        user.setReceptionist(payloadNode.get("isReceptionist").asBoolean());
        this.user = user;
        return true;
      }
      return false;

    } catch (Exception ex) {

      System.out.println(ex.getMessage());
      return false;
    }
  }

  public User getUser() {
    return user;
  }

}
