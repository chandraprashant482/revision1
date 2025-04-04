package com.revision1.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {
    private final HashMap<String, OtpDetails> otpStorage = new HashMap<>();
    private static final int OTP_EXPIRY_TIME_MINUTES = 5; // Expiry time in minutes

    public String generateOtp(String mobileNumber) {
        String otp = String.format("%06d", new Random().nextInt(1000000)); // 6-digit OTP
        OtpDetails otpDetails = new OtpDetails(otp, System.currentTimeMillis());
        otpStorage.put(mobileNumber, otpDetails);
        return otp;
    }

    public boolean validateOtp(String mobileNumber, String otp) {
        OtpDetails otpDetails = otpStorage.get(mobileNumber);
        if (otpDetails == null) {
            return false; // No OTP found for the number
        }

        long currentTime = System.currentTimeMillis();
        long otpTime = otpDetails.getTimeStamp();
        long timeDifferenceMinutes = TimeUnit.MILLISECONDS.toMinutes(currentTime - otpTime);

        if (timeDifferenceMinutes > OTP_EXPIRY_TIME_MINUTES) {
            otpStorage.remove(mobileNumber);
            return false;
        }

        if (otpDetails.getOtp().equals(otp)) {
            otpStorage.remove(mobileNumber);
            return true;
        }

        return otpDetails.getOtp().equals(otp);
    }

    private static class OtpDetails {
        private final String otp;
        private final long timeStamp;

        public OtpDetails(String otp, long timeStamp) {
            this.otp = otp;
            this.timeStamp = timeStamp;
        }

        public String getOtp() {
            return otp;
        }

        public long getTimeStamp() {
            return timeStamp;
        }
    }
}
