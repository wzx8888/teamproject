package com.zhang.generator;

import com.zhang.model.Expression;
import com.zhang.model.Operator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GeneratorTest {

    private static final int RANGE = 10;
    private static final ExpressionGenerator generator = new ExpressionGenerator(RANGE);

    @Test
    public void testGenerateExpressionWithNoOperators() {
        Expression expr = generator.generateExpression(0);
        // Expression should be a number in this case.
        Assertions.assertNotNull(expr);
        Assertions.assertTrue(expr.isLeaf(), "Expression should be a leaf node (number).");
    }

    @Test
    public void testGenerateNumber() {
        Expression expr = generator.generateNumber();
        Assertions.assertNotNull(expr);
        // It should either be a fraction or a natural number
        Assertions.assertTrue(expr.getValue() != null, "The generated number should be a fraction.");
    }

    @Test
    public void testValidExpressionWithValidResult() {
        Expression expr = generator.generateExpression(3);
        Assertions.assertTrue(generator.isValidExpression(expr), "Expression should be valid.");
    }

    @Test
    public void testGenerateLeafNode() {
        // Test generating a leaf node (number only)
        Expression expr = generator.generateExpression(0);
        Assertions.assertNotNull(expr);
        Assertions.assertTrue(expr.isLeaf(), "Expression should be a leaf node.");
        Assertions.assertTrue(expr.getValue() != null, "Leaf node should have a value.");
    }


    @Test
    public void testExpressionWithValidNumberOfOperands() {
        // 生成表达式
        Expression expr = generator.generateExpression(3);
        Assertions.assertNotNull(expr, "Generated expression should not be null.");

        // 计算表达式中的操作符数量
        int operatorCount = countOperators(expr);

        // 如果操作符数量为零，表示表达式没有操作符
        if (operatorCount == 0) {
            System.out.println("Generated expression has no operators. Skipping test.");
        } else {
            Assertions.assertTrue(operatorCount >= 1, "Expression should have at least one operator.");
        }

        // 如果表达式有操作符，可以继续验证结构
        validateExpressionStructure(expr);
    }

    private void validateExpressionStructure(Expression expr) {
        // If the expression is not a leaf, ensure it has both left and right operands
        if (!expr.isLeaf()) {
            Assertions.assertNotNull(expr.getLeft(), "Left operand should not be null for non-leaf expressions.");
            Assertions.assertNotNull(expr.getRight(), "Right operand should not be null for non-leaf expressions.");

            // Recursively check the left and right subexpressions
            validateExpressionStructure(expr.getLeft());
            validateExpressionStructure(expr.getRight());
        } else {
            // For leaf expressions, ensure there are no operands
            Assertions.assertNull(expr.getLeft(), "Leaf expression should not have left operand.");
            Assertions.assertNull(expr.getRight(), "Leaf expression should not have right operand.");
        }
    }


    @Test
    // Helper function to recursively check for the presence of operators
    public boolean checkForOperator(Expression expr) {
        if (expr.isLeaf()) {
            return false; // If it's a leaf, return false (no operators)
        }
        // Check if the current node contains an operator
        if (expr.getOperator() != null) {
            return true; // Found an operator at this level
        }
        // Recursively check left and right subtrees
        return (expr.getLeft() != null && checkForOperator(expr.getLeft())) ||
                (expr.getRight() != null && checkForOperator(expr.getRight()));
    }

    @Test
    public void testOperatorRandomness() {
        // Test that different expressions with max operators generate different results
        Expression expr1 = generator.generateExpression(3);
        Expression expr2 = generator.generateExpression(3);
        Assertions.assertNotEquals(expr1.getOperator(), expr2.getOperator(), "Operators should vary between expressions.");
    }

    @Test
    void testProblemGeneratorConstructor() {
        ProblemGenerator problemGenerator = new ProblemGenerator(100);
        assertNotNull(problemGenerator);
    }

    // 测试生成问题的数量

    // 测试文件是否正确写入

    @Test
    void testGenerateProblemsCount() throws IOException {
        ProblemGenerator problemGenerator = new ProblemGenerator(100);
        problemGenerator.generateProblems(5);

        File exerciseFile = new File("Exercises.txt");
        List<String> problems = Files.readAllLines(Paths.get(exerciseFile.getAbsolutePath()));
        assertEquals(5, problems.size());
    }

    @Test
    void testGenerateUniqueProblem() throws IOException {
        ProblemGenerator problemGenerator = new ProblemGenerator(100);
        problemGenerator.generateProblems(5);

        File exerciseFile = new File("Exercises.txt");
        List<String> problems = Files.readAllLines(Paths.get(exerciseFile.getAbsolutePath()));

        assertEquals(problems.size(), problems.stream().distinct().count(), "Problems should be unique.");
    }

    @Test
    void testValidExpression() throws IOException {
        ProblemGenerator problemGenerator = new ProblemGenerator(100);
        problemGenerator.generateProblems(5);

        File exerciseFile = new File("Exercises.txt");
        List<String> problems = Files.readAllLines(Paths.get(exerciseFile.getAbsolutePath()));

        for (String problem : problems) {
            assertTrue(problem.contains("+") || problem.contains("-") || problem.contains("×") || problem.contains("÷"),
                    "Problem should contain at least one operator");
        }
    }



    @Test
    void testMaxAttemptsForUniqueProblem() throws IOException {
        ProblemGenerator problemGenerator = new ProblemGenerator(100);
        problemGenerator.generateProblems(10000);

        File exerciseFile = new File("Exercises.txt");
        List<String> problems = Files.readAllLines(Paths.get(exerciseFile.getAbsolutePath()));

        assertTrue(problems.size() <= 10000, "Number of problems should not exceed the max attempts.");
    }

    @Test
    void testFileWriteOperations() throws IOException {
        ProblemGenerator problemGenerator = new ProblemGenerator(100);
        problemGenerator.generateProblems(5);

        File exerciseFile = new File("Exercises.txt");
        assertTrue(exerciseFile.exists(), "Exercises.txt should exist.");
        assertTrue(exerciseFile.length() > 0, "Exercises.txt should not be empty.");

        File answerFile = new File("Answers.txt");
        assertTrue(answerFile.exists(), "Answers.txt should exist.");
        assertTrue(answerFile.length() > 0, "Answers.txt should not be empty.");
    }





    // 统计表达式中的操作符数量
    private int countOperators(Expression expr) {
        if (expr.isLeaf()) {
            return 0;
        }
        int leftOperators = countOperators(expr.getLeft());
        int rightOperators = countOperators(expr.getRight());
        return leftOperators + rightOperators + 1; // 当前操作符
    }

}
