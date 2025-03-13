package com.zhang.utils;

import com.zhang.model.Expression;
import com.zhang.model.Fraction;
import com.zhang.model.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 解析字符串表达式为 Expression 对象
 */
public class ExpressionParser {

    /**
     * 解析字符串表达式为 Expression 对象
     */
    public static Expression parse(String expressionStr) {
        // 对表达式进行标记化处理（分割为标记）
        List<String> tokens = tokenize(expressionStr);

        // 使用 Shunting-Yard 算法将标记转换为表达式树
        return parseTokens(tokens);
    }

    /**
     * 将字符串表达式拆分为标记
     */
    private static List<String> tokenize(String expressionStr) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();

        // 规范化运算符周围的空格
        expressionStr = expressionStr.replaceAll("\\s*\\+\\s*", " + ");
        expressionStr = expressionStr.replaceAll("\\s*-\\s*", " - ");
        expressionStr = expressionStr.replaceAll("\\s*×\\s*", " × ");
        expressionStr = expressionStr.replaceAll("\\s*÷\\s*", " ÷ ");
        expressionStr = expressionStr.replaceAll("\\s*\\(\\s*", " ( ");
        expressionStr = expressionStr.replaceAll("\\s*\\)\\s*", " ) ");

        // 按空格拆分
        String[] parts = expressionStr.trim().split("\\s+");

        for (String part : parts) {
            if (!part.isEmpty()) {
                tokens.add(part);
            }
        }

        return tokens;
    }

    /**
     * 使用 Shunting-Yard 算法解析标记为表达式树
     */
    private static Expression parseTokens(List<String> tokens) {
        Stack<Expression> output = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (isNumber(token) || isFraction(token)) {
                // 如果是数字或分数，压入输出栈
                output.push(new Expression(parseFraction(token)));
            } else if (isOperator(token)) {
                // 根据优先级处理运算符
                while (!operators.isEmpty() && isOperator(operators.peek()) &&
                        getPrecedence(operators.peek()) >= getPrecedence(token)) {
                    // 弹出运算符并创建表达式节点
                    String op = operators.pop();
                    if (output.size() < 2) {
                        throw new IllegalArgumentException("无效表达式: 运算符 " + op + " 缺少操作数");
                    }

                    Expression right = output.pop();
                    Expression left = output.pop();
                    output.push(new Expression(left, getOperator(op), right));
                }
                operators.push(token);
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                // 处理运算符直到匹配的 "("
                boolean foundOpenParen = false;
                while (!operators.isEmpty()) {
                    if (operators.peek().equals("(")) {
                        operators.pop(); // 弹出 "(" 并丢弃
                        foundOpenParen = true;
                        break;
                    }

                    String op = operators.pop();
                    if (output.size() < 2) {
                        throw new IllegalArgumentException("无效表达式: 运算符 " + op + " 缺少操作数");
                    }

                    Expression right = output.pop();
                    Expression left = output.pop();
                    output.push(new Expression(left, getOperator(op), right));
                }

                if (!foundOpenParen) {
                    throw new IllegalArgumentException("括号不匹配");
                }
            } else {
                throw new IllegalArgumentException("无效标记: " + token);
            }
        }

        // 处理剩余的运算符
        while (!operators.isEmpty()) {
            String op = operators.pop();
            if (op.equals("(") || op.equals(")")) {
                throw new IllegalArgumentException("括号不匹配");
            }

            if (output.size() < 2) {
                throw new IllegalArgumentException("无效表达式: 运算符 " + op + " 缺少操作数");
            }

            Expression right = output.pop();
            Expression left = output.pop();
            output.push(new Expression(left, getOperator(op), right));
        }

        if (output.size() != 1) {
            throw new IllegalArgumentException("无效表达式: 操作数过多");
        }

        return output.pop();
    }

    /**
     * 判断是否为整数
     */
    private static boolean isNumber(String token) {
        return token.matches("\\d+");
    }

    /**
     * 判断是否为分数
     */
    private static boolean isFraction(String token) {
        return token.matches("\\d+/\\d+") || token.matches("\\d+'\\d+/\\d+");
    }

    /**
     * 判断是否为运算符
     */
    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("×") || token.equals("÷");
    }

    /**
     * 获取运算符的优先级
     */
    private static int getPrecedence(String op) {
        if (op.equals("+") || op.equals("-")) {
            return 1;
        } else if (op.equals("×") || op.equals("÷")) {
            return 2;
        }
        return 0;
    }

    /**
     * 获取运算符对应的 Operator 枚举值
     */
    private static Operator getOperator(String op) {
        switch (op) {
            case "+": return Operator.ADD;
            case "-": return Operator.SUBTRACT;
            case "×": return Operator.MULTIPLY;
            case "÷": return Operator.DIVIDE;
            default: throw new IllegalArgumentException("未知运算符: " + op);
        }
    }

    /**
     * 解析字符串为 Fraction 对象
     */
    private static Fraction parseFraction(String token) {
        return Fraction.parse(token);
    }
}