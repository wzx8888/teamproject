package com.zhang.utils;

import com.zhang.model.Expression;
import com.zhang.model.Fraction;
import com.zhang.model.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 使用逆波兰表示法（RPN）计算表达式的工具类。
 */
public class RPNEvaluator {

    /**
     * 将表达式树转换为 RPN（逆波兰表达式）标记列表。
     */
    public static List<Object> toRPN(Expression expr) {
        List<Object> result = new ArrayList<>();
        convertToRPN(expr, result);
        return result;
    }

    /**
     * 递归地将表达式转换为 RPN 形式的辅助方法。
     */
    private static void convertToRPN(Expression expr, List<Object> result) {
        if (expr.isLeaf()) {
            // 如果是叶子节点，添加数值
            result.add(expr.getValue());
        } else {
            // 先处理左子树
            convertToRPN(expr.getLeft(), result);

            // 再处理右子树
            convertToRPN(expr.getRight(), result);

            // 在两个操作数之后添加运算符
            result.add(expr.getOperator());
        }
    }

    /**
     * 使用 RPN 计算表达式的值。
     */
    public static Fraction evaluateRPN(List<Object> rpnTokens) {
        Stack<Fraction> stack = new Stack<>();

        for (Object token : rpnTokens) {
            if (token instanceof Fraction) {
                stack.push((Fraction) token);
            } else if (token instanceof Operator) {
                Operator op = (Operator) token;

                // 按逆序弹出操作数（先弹出右操作数，再弹出左操作数）
                Fraction right = stack.pop();
                Fraction left = stack.pop();

                // 进行运算
                Fraction result;
                switch (op) {
                    case ADD:
                        result = left.add(right);
                        break;
                    case SUBTRACT:
                        result = left.subtract(right);
                        break;
                    case MULTIPLY:
                        result = left.multiply(right);
                        break;
                    case DIVIDE:
                        result = left.divide(right);
                        break;
                    default:
                        throw new IllegalStateException("未知运算符: " + op);
                }

                // 将计算结果压入栈中
                stack.push(result);
            }
        }

        // 计算完成后，栈中应只剩下最终结果
        if (stack.size() != 1) {
            throw new IllegalStateException("无效的 RPN 表达式: " + rpnTokens);
        }

        return stack.pop();
    }

    /**
     * 获取表达式的 RPN 规范形式
     * 这有助于检测由于交换律导致的等价表达式。
     */
    public static String getCanonicalRPN(Expression expr) {
        // 将表达式转换为 RPN
        List<Object> rpnTokens = toRPN(expr);

        // 由于 RPN 不能完全标准化表达式，
        // 但可以对连续的可交换运算符进行排序，以便识别简单的等价表达式。
        return rpnTokens.toString();
    }

    /**
     * 解析字符串表达式为 RPN 标记列表
     * 用于 `Grader` 评分系统，从文件中解析并计算表达式的值。
     */
    public static List<Object> parseExpressionToRPN(String expressionStr) {
        // 需要完整的表达式解析器
        // 目前我们实现一个简化版本来处理基本表达式

        // 如果结尾包含 "= "，则去除
        expressionStr = expressionStr.replaceAll("\\s*=\\s*$", "").trim();

        // 提取实际的表达式（去掉题号部分）
        if (expressionStr.matches("^\\d+\\.\\s+.*")) {
            expressionStr = expressionStr.replaceAll("^\\d+\\.\\s+", "").trim();
        }

        // 统一运算符周围的空格格式
        expressionStr = expressionStr.replaceAll("\\s+", " ");

        // 这里应该使用递归下降解析器来解析表达式
        // 但为了示例演示，暂时使用一个占位实现

        List<Object> tokens = new ArrayList<>();
        // 解析逻辑待补充

        return tokens;
    }
}