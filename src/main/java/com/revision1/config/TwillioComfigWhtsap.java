//package com.revision1.config;
//
//
//
//import com.twilio.Twilio;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class TwillioComfigWhtsap {
//    @Value("${twilio.account.sidWhatsapp}")
//    private String accountSid;
//
//    @Value("${twilio.auth.tokenWhatsapp}")
//    private String authToken;
//
//    @Bean
//    public void initializeTwilio() {
//        Twilio.init(accountSid, authToken);
//    }
//}
