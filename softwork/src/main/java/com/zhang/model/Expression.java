package com.zhang.model;


public class Expression {
    private Expression left; // 左子表达式
    private Expression right; // 右子表达式
    private Operator operator; // 运算符
    private Fraction value; // 叶节点的值（如果是数值）

    // 叶节点构造函数（数值）
    public Expression(Fraction value) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.operator = null;
    }

    // 操作节点构造函数
    public Expression(Expression left, Operator operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
        this.value = null;
    }

    /**
     * 判断是否为叶节点（即单个数值）
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }

    public Fraction getValue() {
        return value;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public Operator getOperator() {
        return operator;
    }

    /**
     * 计算表达式的值
     */
    public Fraction evaluate() {
        if (isLeaf()) {
            return value;
        }

        Fraction leftValue = left.evaluate();
        Fraction rightValue = right.evaluate();

        switch (operator) {
            case ADD:
                return leftValue.add(rightValue);
            case SUBTRACT:
                return leftValue.subtract(rightValue);
            case MULTIPLY:
                return leftValue.multiply(rightValue);
            case DIVIDE:
                return leftValue.divide(rightValue);
            default:
                throw new IllegalStateException("未知运算符: " + operator);
        }
    }

    /**
     * 将表达式转换为字符串
     */
    @Override
    public String toString() {
        if (isLeaf()) {
            return value.toString();
        }

        StringBuilder sb = new StringBuilder();

        // 如果需要，为左表达式添加括号
        if (!left.isLeaf() && left.getOperator().getPrecedence() < operator.getPrecedence()) {
            sb.append("(").append(left).append(")");
        } else {
            sb.append(left);
        }

        // 添加带空格的运算符
        sb.append(" ").append(operator.getSymbol()).append(" ");

        // 如果需要，为右表达式添加括号
        if (!right.isLeaf() &&
                (right.getOperator().getPrecedence() < operator.getPrecedence() ||
                        (right.getOperator().getPrecedence() == operator.getPrecedence() && !operator.isCommutative()))) {
            sb.append("(").append(right).append(")");
        } else {
            sb.append(right);
        }

        return sb.toString();
    }
}
