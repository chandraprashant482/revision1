package com.revision1.service;



import com.revision1.config.TwilioConfigSMS;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SmsService {

    private final TwilioConfigSMS twilioConfigSMS;

    @Autowired
    public SmsService(TwilioConfigSMS twilioConfigSMS) {
        this.twilioConfigSMS = twilioConfigSMS;
    }

    public String sendSms(String to, String body) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(to),                      // To phone number
                    new PhoneNumber(twilioConfigSMS.getTwilioPhoneNumber()), // From Twilio number
                    body                                     // SMS body
            ).create();

            return "Message sent successfully with SID: " + message.getSid();
        } catch (Exception e) {
            return "Failed to send SMS: " + e.getMessage();
        }
    }
}
