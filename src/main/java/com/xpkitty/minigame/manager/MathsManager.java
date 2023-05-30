package com.xpkitty.minigame.manager;

public class MathsManager {

    public static boolean isNumberBetweenValues(double a, double b, double testedNum) {
        double lowest = Math.min(a, b);
        double highest = Math.max(a, b);

        if (testedNum > lowest && testedNum < highest) {
            return true;
        }
        return false;
    }

    public static boolean isNumberInNumberRegion(double a, double b, double testedNum) {
        double lowest = Math.min(a, b);
        double highest = Math.max(a, b);

        if (testedNum >= lowest && testedNum <= highest) {
            return true;
        }
        return false;
    }
}
