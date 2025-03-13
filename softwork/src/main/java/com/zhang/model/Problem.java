package com.zhang.model;

public class Problem {
    private final Expression expression; // 表达式
    private final Fraction answer; // 答案

    public Problem(Expression expression, Fraction answer) {
        this.expression = expression;
        this.answer = answer;
    }

    public Expression getExpression() {
        return expression;
    }

    public Fraction getAnswer() {
        return answer;
    }
}
