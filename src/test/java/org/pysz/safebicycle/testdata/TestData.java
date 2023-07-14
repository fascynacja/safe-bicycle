package org.pysz.safebicycle.testdata;

import org.pysz.safebicycle.dto.Coverage;

import java.math.BigDecimal;

public class TestData {
   public static final Coverage RED_BICYCLE_COVERAGE = Coverage.EXTRA;
   public static final BigDecimal RED_BICYCLE_SUM_INSURED = new BigDecimal("1000.00");
   public static final BigDecimal RED_BICYCLE_PREMIUM = new BigDecimal("30.000");
   public static final String RED_BICYCLE_MODEL = "Romet";
   public static final int RED_BICYCLE_MAKE_MANUFACTURE_YEAR = 2015;
   public static final String RED_BICYCLE_MAKE = "Wigry";
   public static final String RISK_TYPE_THEFT = "THEFT";
   public static final BigDecimal RISK_THEFT_SUM_INSURED = new BigDecimal("1000.00");
   public static final BigDecimal BICYCLES_ALL_PREMIUM = new BigDecimal("48.95");

   private static String round(BigDecimal amount) {
      return  amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
   }

}
