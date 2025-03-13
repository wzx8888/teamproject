package com.zhang.model;

public enum Operator {
    ADD("+", 1, true),      // 加法，优先级1，可交换
    SUBTRACT("-", 1, false), // 减法，优先级1，不可交换
    MULTIPLY("×", 2, true),  // 乘法，优先级2，可交换
    DIVIDE("÷", 2, false);   // 除法，优先级2，不可交换

    private final String symbol; // 运算符符号
    private final int precedence; // 优先级（越高越先计算）
    private final boolean commutative; // 是否可交换

    Operator(String symbol, int precedence, boolean commutative) {
        this.symbol = symbol;
        this.precedence = precedence;
        this.commutative = commutative;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getPrecedence() {
        return precedence;
    }

    public boolean isCommutative() {
        return commutative;
    }
}

