package com.zhang;

import com.zhang.generator.ProblemGenerator;
import com.zhang.grader.Grader;
import com.zhang.utils.CommandLineParser;

public class Main {
    public static void main(String[] args) {
        try {
            // 解析命令行参数
            CommandLineParser parser = new CommandLineParser(args);


            if (parser.hasExerciseAndAnswerFiles()) {
                // 评分模式 - 检查答案文件
                Grader grader = new Grader();
                grader.grade(parser.getExerciseFile(), parser.getAnswerFile());
            } else if (parser.hasNumberAndRange()) {
                // 生成模式 - 生成新的题目
                int count = parser.getNumber();
                int range = parser.getRange();

                ProblemGenerator generator = new ProblemGenerator(range);
                generator.generateProblems(count);
            } else {
                // 如果参数不正确，显示帮助信息
                printHelp();
            }
        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
            printHelp();
        }
    }

    // 打印帮助信息
    private static void printHelp() {
        System.out.println("使用方法:");
        System.out.println("  生成模式: java -jar Myapp.jar -n <数量> -r <范围>");
        System.out.println("  评分模式: java -jar Myapp.jar -e <题目文件>.txt -a <答案文件>.txt");
        System.out.println("");
        System.out.println("选项:");
        System.out.println("  -n <数量>    要生成的题目数量");
        System.out.println("  -r <范围>    数值范围（自然数、分数和分母）");
        System.out.println("  -e <文件>    要评分的题目文件");
        System.out.println("  -a <文件>    要评分的答案文件");
    }

}