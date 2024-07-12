package com.devlink.adminx.utils;

import java.security.SecureRandom;

public class Security {

    public static int generateUniqueCode() {
        SecureRandom secureRandom = new SecureRandom();
        final int MIN = 100000;
        return secureRandom.nextInt((999999 - MIN) + 1) + MIN;
    }
}
