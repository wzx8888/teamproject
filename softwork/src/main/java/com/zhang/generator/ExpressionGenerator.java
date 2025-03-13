package com.zhang.generator;

import com.zhang.model.Expression;
import com.zhang.model.Fraction;
import com.zhang.model.Operator;

import java.util.*;

public class ExpressionGenerator {
    private final int range; // 数值范围
    private final Random random; // 随机数生成器


    public ExpressionGenerator(int range) {
        this.range = range;
        this.random = new Random();
    }

    /**
     * 生成一个表达式，最多包含指定数量的运算符
     * @param maxOperators 最大运算符数量
     * @return 生成的表达式
     */
    public Expression generateExpression(int maxOperators) {
        // 如果不再需要运算符或随机决定结束表达式，则生成一个数值
        if (maxOperators == 0 || random.nextInt(3) == 0) {
            return generateNumber();
        }

        // 生成一个操作
        Expression left = generateExpression(maxOperators - 1); // 左侧可以是复杂表达式
        Operator operator = generateOperator(); // 随机选择运算符
        Expression right = generateExpression(0); // 右侧仅为数值，简化生成过程

        Expression result = new Expression(left, operator, right);

        // 检查表达式是否符合要求
        if (!isValidExpression(result)) {
            // 如果无效，则重新尝试
            return generateExpression(maxOperators);
        }

        return result;
    }

    /**
     * 生成一个数值表达式（自然数或分数）
     */
    public Expression generateNumber() {
        // 30%的概率生成分数，70%的概率生成自然数
        if (random.nextInt(10) < 3) {
            return new Expression(generateFraction());
        } else {
            return new Expression(generateNaturalNumber());
        }
    }

    /**
     * 生成一个真分数
     */
    public Fraction generateFraction() {
        // 生成一个真分数（分子小于分母）
        int numerator = random.nextInt(range - 1) + 1; // 1到range-1之间
        int denominator = random.nextInt(range - numerator) + numerator + 1; // 确保分母大于分子

        // 20%的概率生成带整数部分的分数（带分数）
        if (random.nextInt(5) == 0 && range > 2) {
            int wholeNumber = random.nextInt(range - 1) + 1;
            return new Fraction(wholeNumber, numerator, denominator);
        } else {
            return new Fraction(numerator, denominator);
        }
    }

    /**
     * 生成一个自然数
     */
    public Fraction generateNaturalNumber() {
        int number = random.nextInt(range);
        return new Fraction(number);
    }

    /**
     * 随机生成一个运算符
     */
    public Operator generateOperator() {
        Operator[] operators = Operator.values();
        return operators[random.nextInt(operators.length)];
    }

    /**
     * 检查表达式是否有效（符合所有要求）
     */
    public boolean isValidExpression(Expression expr) {
        try {
            Fraction result = expr.evaluate();

            // 检查表达式在任何中间步骤中是否包含负数
            if (hasNegativeIntermediates(expr)) {
                return false;
            }

            // 检查除法结果是否为真分数
            if (hasDivisionWithImproperResult(expr)) {
                return false;
            }

            return result != null && !result.isNegative();
        } catch (ArithmeticException e) {
            return false; // 出现算术异常（如除以零）
        }
    }

    /**
     * 检查表达式计算过程中是否有负数
     */
    public boolean hasNegativeIntermediates(Expression expr) {
        if (expr.isLeaf()) {
            return expr.getValue().isNegative();
        }

        if (expr.getOperator() == Operator.SUBTRACT) {
            Fraction leftValue = expr.getLeft().evaluate();
            Fraction rightValue = expr.getRight().evaluate();

            // 如果减法操作中左侧小于右侧，会产生负数
            if (leftValue.compareTo(rightValue) < 0) {
                return true;
            }
        }

        // 递归检查左右子表达式
        return hasNegativeIntermediates(expr.getLeft()) ||
                hasNegativeIntermediates(expr.getRight());
    }

    /**
     * 检查表达式中的除法操作是否产生非真分数结果
     */
    public boolean hasDivisionWithImproperResult(Expression expr) {
        if (expr.isLeaf()) {
            return false;
        }

        if (expr.getOperator() == Operator.DIVIDE) {
            Fraction leftValue = expr.getLeft().evaluate();
            Fraction rightValue = expr.getRight().evaluate();

            if (rightValue.isZero()) {
                return true; // 除以零
            }

            Fraction result = leftValue.divide(rightValue);
            // 确保除法结果是真分数或整数
            if (!result.isProperFraction() && !result.isWholeNumber()) {
                return true; // 非真分数结果
            }
        }

        // 递归检查左右子表达式
        return hasDivisionWithImproperResult(expr.getLeft()) ||
                hasDivisionWithImproperResult(expr.getRight());
    }

}