package com.zhang.generator;

import com.zhang.model.Expression;
import com.zhang.model.Fraction;
import com.zhang.model.Problem;
import com.zhang.utils.RPNEvaluator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProblemGenerator {
    private final ExpressionGenerator expressionGenerator; // 表达式生成器
    private final Set<String> problemSignatures = new HashSet<>(); // 用于存储已生成题目的签名，防止重复

    public ProblemGenerator(int range) {
        this.expressionGenerator = new ExpressionGenerator(range);
    }

    /**
     * 生成指定数量的题目并写入文件
     * @param count 题目数量
     */
    public void generateProblems(int count) throws IOException {
        List<Problem> problems = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Problem problem = generateUniqueProblem();
            if (problem == null) {
                System.err.println("Warning: Could only generate " + i + " unique problems.");
                break;
            }
            problems.add(problem);
        }

        // 将题目写入文件
        try (PrintWriter exerciseWriter = new PrintWriter(new FileWriter("Exercises.txt"));
             PrintWriter answerWriter = new PrintWriter(new FileWriter("Answers.txt"))) {

            for (int i = 0; i < problems.size(); i++) {
                Problem problem = problems.get(i);
                exerciseWriter.println((i + 1) + ". " + problem.getExpression() + " = ");
                answerWriter.println((i + 1) + ". " + problem.getAnswer());
            }
        }

        System.out.println("Generated " + problems.size() + " problems.");
        System.out.println("Problems saved to Exercises.txt");
        System.out.println("Answers saved to Answers.txt");
    }

    /**
     * 生成一个唯一的题目（不与之前生成的重复）
     */
    private Problem generateUniqueProblem() {
        // 生成唯一题目的最大尝试次数
        final int MAX_ATTEMPTS = 10000;

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            // 生成一个最多包含3个运算符的表达式
            Expression expression = expressionGenerator.generateExpression(3);

            //检查表达式是否有效，至少包含一个运算符
            if(!isValidExpression(expression)){
                continue;
            }

            // 获取表达式的规范形式
            String signature = getCanonicalForm(expression);

            // 检查题目是否重复
            if (!problemSignatures.contains(signature)) {
                problemSignatures.add(signature);

                // 使用RPN计算答案
                Fraction answer = RPNEvaluator.evaluateRPN(RPNEvaluator.toRPN(expression));
                return new Problem(expression, answer);
            }
        }

        System.err.println("After multiple attempts, could not generate more unique problems.");
        return null;
    }

    private boolean isValidExpression(Expression expression) {
        String expr =expression.toString();
        return  expr.contains("+")||expr.contains("-")||expr.contains("×")||expr.contains("÷");
    }

    /**
     * 获取表达式的规范形式，用于检测重复题目
     * 使用RPN表示并考虑操作符的可交换性
     */
    private String getCanonicalForm(Expression expression) {
        return RPNEvaluator.getCanonicalRPN(expression);
    }
}