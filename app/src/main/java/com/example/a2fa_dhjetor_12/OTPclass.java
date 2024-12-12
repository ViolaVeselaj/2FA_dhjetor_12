package com.example.a2fa_dhjetor_12;

import java.security.SecureRandom;

public class OTPclass {

    public static String generate(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            otp.append(characters.charAt(index));
        }

        return otp.toString();
    }
}
