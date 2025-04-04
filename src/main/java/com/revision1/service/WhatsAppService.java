//package com.revision1.service;
//
//
//
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class WhatsAppService {
//
//    @Value("${twilio.whatsapp.from}")
//    private String from;
//
//    public String sendWhatsAppMessage(String to, String body) {
//        try {
//            Message message = Message.creator(
//                    new PhoneNumber("whatsapp:" + to),   // ✅ Ensure correct format
//                    new PhoneNumber("whatsapp:" + from),             // From Twilio WhatsApp Sandbox
//                    body                                // Message body
//            ).create();
//
//            return "WhatsApp message sent successfully with SID: " + message.getSid();
//        } catch (Exception e) {
//            return "Failed to send WhatsApp message: " + e.getMessage();
//        }
//    }
//}
