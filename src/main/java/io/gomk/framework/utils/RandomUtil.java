package io.gomk.framework.utils;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {
    private static final Random GENERATOR;;
    private static String[] randomValues = new String[] {"0","1","2","3","4","5","6","7","8","9"};
    
    static {
        GENERATOR = new Random();
        GENERATOR.setSeed(System.currentTimeMillis());
    }

    public static String getSixDigits() {
        String number = String.valueOf(GENERATOR.nextInt(1000000) + 1000000);

        return number.substring(1, number.length());
    }
    public static String getTwelveDigits() {
    	int a[] = new int[12];
    	String number ="";
        for(int i = 0;i < a.length; i++) {
            a[i] = (int)(10 * (Math.random()));
            number = number + String.valueOf(a[i]);
        }
    	return number;
    }

    public static String getGlobalUniqueId() {
        return UUID.randomUUID().toString();
    }
    
    public static String getDigitsAndAlphabet(int length) {
    	StringBuffer str = new StringBuffer();
		for (int i = 0; i < length; i++) {
			Double number = Math.random() * (randomValues.length - 1);
			str.append(randomValues[number.intValue()]);
		}
		
		return str.toString();
    }
    
    public static String getSixDigitsAndAlphabet() {
        return getDigitsAndAlphabet(6);
    }

    // 返回一个在[0, maxValue)之间的随机整数
    public static int getInteger(int maxValue) {
        return GENERATOR.nextInt(maxValue);
    }
    
}
