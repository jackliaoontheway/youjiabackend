package com.polarj.common.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MeasurementUtil
{
    public static double fromCM2Inch(double cms)
    {
        return fromCM2Inch(cms, 2);
    }

    public static double fromCM2Inch(double cms, int presicion)
    {
        double inches = 1.0 * Math.round(cms * 0.393701d * Math.pow(10, presicion)) / Math.pow(10, presicion);
        return inches;
    }

    public static double fromInch2CM(double inches)
    {
        return fromInch2CM(inches, 2);
    }

    public static double fromInch2CM(double inches, int presicion)
    {
        double cms = 1.0 * Math.round(inches * 2.54d * Math.pow(10, presicion)) / Math.pow(10, presicion);
        return cms;
    }

    public static double fromLb2Kg(double lbs)
    {
        return fromLb2Kg(lbs, 2);
    }

    public static double fromLb2Kg(double lbs, int presicion)
    {
        double kgs = 1.0 * Math.round(lbs * 0.453592d * Math.pow(10, presicion)) / Math.pow(10, presicion);
        return kgs;
    }

    public static double fromKg2Lb(double kgs)
    {
        return fromKg2Lb(kgs, 2);
    }

    public static double fromKg2Lb(double kgs, int presicion)
    {
        double lbs = 1.0 * Math.round(kgs * 2.20462d * Math.pow(10, presicion)) / Math.pow(10, presicion);
        return lbs;
    }

    public static double fromKg2Oz(double kgs)
    {
        return fromKg2Oz(kgs, 2);
    }

    public static double fromKg2Oz(double kgs, int presicion)
    {
        double ozs = 1.0 * Math.round(kgs * 35.274d * Math.pow(10, presicion)) / Math.pow(10, presicion);
        return ozs;
    }
    
    public static double fromG2Kg(double g, int presicion)
    {
    	
    	BigDecimal bigDecimal = new BigDecimal(g);
    	BigDecimal unit = new BigDecimal(1000);
    	bigDecimal = bigDecimal.divide(unit,presicion,RoundingMode.HALF_UP);
    	
        return bigDecimal.doubleValue();
    }
    public static double fromKg2G(double kg, int presicion)
    {
    	
    	BigDecimal bigDecimal = new BigDecimal(kg);
    	BigDecimal unit = new BigDecimal(1000);
    	bigDecimal = bigDecimal.multiply(unit).setScale(presicion, RoundingMode.HALF_UP);
    	
        return bigDecimal.doubleValue();
    }
}
