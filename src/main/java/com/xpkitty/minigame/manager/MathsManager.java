// 2023. Author: S.Frynas (XpKitty), e-mail: sebastian.frynas@outlook.com, licence: GNU GPL v3

package com.xpkitty.minigame.manager;

public class MathsManager {

    public static boolean isNumberBetweenValues(double a, double b, double testedNum) {
        double lowest = Math.min(a, b);
        double highest = Math.max(a, b);

        return testedNum > lowest && testedNum < highest;
    }

    public static boolean isNumberInNumberRegion(double a, double b, double testedNum) {
        double lowest = Math.min(a, b);
        double highest = Math.max(a, b);

        return testedNum >= lowest && testedNum <= highest;
    }
}
