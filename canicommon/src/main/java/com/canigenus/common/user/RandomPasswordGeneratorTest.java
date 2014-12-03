package com.canigenus.common.user;

public class RandomPasswordGeneratorTest {

    public static void main(String[] args) {
        int noOfCAPSAlpha = 1;
        int noOfDigits = 1;
        int noOfSplChars = 1;
        int minLen = 8;
        int maxLen = 12;
 
        for (int i = 0; i < 10; i++) {
            char[] pswd = RandomPasswordGenerator.generatePswd(minLen, maxLen,
                    noOfCAPSAlpha, noOfDigits, noOfSplChars);
            System.out.println("Len = " + pswd.length + ", " + new String(pswd));
        }
    }

}
