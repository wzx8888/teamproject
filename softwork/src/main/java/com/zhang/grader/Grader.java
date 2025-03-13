package com.zhang.grader;

import com.zhang.model.Expression;
import com.zhang.model.Fraction;
import com.zhang.utils.ExpressionParser;
import com.zhang.utils.RPNEvaluator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grader {

    /**
     * 评估用户答案并生成评分报告
     * @param exerciseFile 练习题文件的路径
     * @param answerFile 答案文件的路径
     */
    public void grade(String exerciseFile, String answerFile) throws IOException {
        List<String> exercises = readLines(exerciseFile);
        List<String> userAnswers = readLines(answerFile);

        List<Integer> correctProblems = new ArrayList<>(); // 记录答对的题号
        List<Integer> wrongProblems = new ArrayList<>(); // 记录答错的题号

        // 检查题目数量和答案数量是否匹配
        int minSize = Math.min(exercises.size(), userAnswers.size());

        for (int i = 0; i < minSize; i++) {
            String exercise = exercises.get(i);
            String userAnswer = userAnswers.get(i);

            // 提取题号和标准答案
            int problemNumber = extractProblemNumber(exercise);

            try {
                Fraction expectedAnswer = calculateAnswer(exercise);
                Fraction providedAnswer = parseAnswer(userAnswer);

                // 检查用户答案是否正确
                if (expectedAnswer != null && providedAnswer != null &&
                        expectedAnswer.equals(providedAnswer)) {
                    correctProblems.add(problemNumber);
                } else {
                    wrongProblems.add(problemNumber);
                }
            } catch (Exception e) {
                wrongProblems.add(problemNumber);
                System.err.println("处理题目 " + problemNumber + " 时出错: " + e.getMessage());
            }
        }

        // 将评分结果写入 Grade.txt 文件
        try (PrintWriter writer = new PrintWriter(new FileWriter("Grade.txt"))) {
            writer.println("正确: " + correctProblems.size() + " " + formatNumberList(correctProblems));
            writer.println("错误: " + wrongProblems.size() + " " + formatNumberList(wrongProblems));
        }

        System.out.println("评分完成，结果已保存到 Grade.txt");
    }

    /**
     * 读取文件中的所有行
     */
    private List<String> readLines(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * 从题目行中提取题号
     */
    private int extractProblemNumber(String line) {
        Pattern pattern = Pattern.compile("^(\\d+)\\.");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    /**
     * 计算题目的标准答案
     */
    private Fraction calculateAnswer(String exercise) {
        // 提取题目中的表达式
        String expressionStr = exercise.replaceAll("^\\d+\\.\\s+", "").trim();
        expressionStr = expressionStr.replaceAll("\\s*=\\s*$", "").trim();

        // 解析表达式字符串为 Expression 对象
        Expression expr = ExpressionParser.parse(expressionStr);

        // 转换为逆波兰表达式（RPN）并计算结果
        List<Object> rpnTokens = RPNEvaluator.toRPN(expr);
        return RPNEvaluator.evaluateRPN(rpnTokens);
    }

    /**
     * 解析答案行中的用户答案
     */
    private Fraction parseAnswer(String answerLine) {
        // 提取答案部分（去除题号）
        Pattern pattern = Pattern.compile("^\\d+\\.\\s+(.+)$");
        Matcher matcher = pattern.matcher(answerLine);
        if (matcher.find()) {
            String answerStr = matcher.group(1).trim();
            return Fraction.parse(answerStr);
        }
        return null;
    }

    /**
     * 格式化数字列表，例如 (1, 2, 3)
     */
    private String formatNumberList(List<Integer> numbers) {
        if (numbers.isEmpty()) {
            return "()";
        }

        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < numbers.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(numbers.get(i));
        }
        sb.append(")");
        return sb.toString();
    }
}

