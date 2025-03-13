package com.zhang.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fraction implements Comparable<Fraction> {
    private final int numerator; // 分子
    private final int denominator; // 分母

    /**
     * 构造一个分数
     * @param numerator 分子
     * @param denominator 分母
     */
    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("除数不能为零");
        }

        // 始终保持分母为正
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }

        // 简化分数
        int gcd = gcd(Math.abs(numerator), denominator);
        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;
    }

    /**
     * 构造一个自然数（分母为1的分数）
     */
    public Fraction(int wholeNumber) {
        this(wholeNumber, 1);
    }

    /**
     * 构造一个带分数（整数部分+分数部分）
     */
    public Fraction(int wholeNumber, int numerator, int denominator) {
        this(wholeNumber * denominator + numerator, denominator);
    }

    /**
     * 计算最大公约数 (GCD)
     */
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    /**
     * 分数加法
     */
    public Fraction add(Fraction other) {
        int newNumerator = this.numerator * other.denominator + other.numerator * this.denominator;
        int newDenominator = this.denominator * other.denominator;
        return new Fraction(newNumerator, newDenominator);
    }

    /**
     * 分数减法
     */
    public Fraction subtract(Fraction other) {
        int newNumerator = this.numerator * other.denominator - other.numerator * this.denominator;
        int newDenominator = this.denominator * other.denominator;
        return new Fraction(newNumerator, newDenominator);
    }

    /**
     * 分数乘法
     */
    public Fraction multiply(Fraction other) {
        int newNumerator = this.numerator * other.numerator;
        int newDenominator = this.denominator * other.denominator;
        return new Fraction(newNumerator, newDenominator);
    }

    /**
     * 分数除法
     */
    public Fraction divide(Fraction other) {
        if (other.numerator == 0) {
            throw new ArithmeticException("除数不能为零");
        }
        int newNumerator = this.numerator * other.denominator;
        int newDenominator = this.denominator * other.numerator;
        return new Fraction(newNumerator, newDenominator);
    }

    /**
     * 判断分数是否为负
     */
    public boolean isNegative() {
        return numerator < 0;
    }

    /**
     * 判断分数是否为零
     */
    public boolean isZero() {
        return numerator == 0;
    }

    /**
     * 判断是否为真分数（分子绝对值小于分母）
     */
    public boolean isProperFraction() {
        return Math.abs(numerator) < denominator;
    }

    /**
     * 判断是否为整数（分母为1）
     */
    public boolean isWholeNumber() {
        return denominator == 1;
    }

    /**
     * 将分数转换为字符串表示
     */
    @Override
    public String toString() {
        if (denominator == 1) {
            // 自然数
            return String.valueOf(numerator);
        } else if (Math.abs(numerator) < denominator) {
            // 真分数
            return numerator + "/" + denominator;
        } else {
            // 带分数
            int wholeNumber = numerator / denominator;
            int remainingNumerator = Math.abs(numerator % denominator);
            if (remainingNumerator == 0) {
                return String.valueOf(wholeNumber);
            } else {
                return wholeNumber + "'" + remainingNumerator + "/" + denominator;
            }
        }
    }

    /**
     * 判断两个分数是否相等
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Fraction other = (Fraction) obj;
        return this.numerator == other.numerator && this.denominator == other.denominator;
    }

    @Override
    public int hashCode() {
        return 31 * numerator + denominator;
    }

    /**
     * 比较两个分数的大小
     */
    @Override
    public int compareTo(Fraction other) {
        long thisVal = (long) this.numerator * other.denominator;
        long otherVal = (long) other.numerator * this.denominator;
        return Long.compare(thisVal, otherVal);
    }

    /**
     * 从字符串解析分数
     */
    public static Fraction parse(String str) {
        str = str.trim();

        // 整数模式
        if (str.matches("\\d+")) {
            return new Fraction(Integer.parseInt(str));
        }

        // 真分数模式: a/b
        Pattern properFractionPattern = Pattern.compile("(\\d+)/(\\d+)");
        Matcher properFractionMatcher = properFractionPattern.matcher(str);
        if (properFractionMatcher.matches()) {
            int numerator = Integer.parseInt(properFractionMatcher.group(1));
            int denominator = Integer.parseInt(properFractionMatcher.group(2));
            return new Fraction(numerator, denominator);
        }

        // 带分数模式: a'b/c
        Pattern mixedNumberPattern = Pattern.compile("(\\d+)'(\\d+)/(\\d+)");
        Matcher mixedNumberMatcher = mixedNumberPattern.matcher(str);
        if (mixedNumberMatcher.matches()) {
            int wholeNumber = Integer.parseInt(mixedNumberMatcher.group(1));
            int numerator = Integer.parseInt(mixedNumberMatcher.group(2));
            int denominator = Integer.parseInt(mixedNumberMatcher.group(3));
            return new Fraction(wholeNumber, numerator, denominator);
        }

        throw new IllegalArgumentException("无效的分数格式: " + str);
    }
}
