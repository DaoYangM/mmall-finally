package top.daoyang.demo.util;

import java.math.BigDecimal;

public class BigDecimalUtils {

    public static BigDecimal mul(double v1, double v2) {
        return BigDecimal.valueOf(v1).multiply(BigDecimal.valueOf(v2));
    }
}
