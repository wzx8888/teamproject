package com.zhang.utils;

public class CommandLineParser {
    private int number = 0;
    private int range = 0;
    private String exerciseFile = null;
    private String answerFile = null;

    public CommandLineParser(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-n":
                    if (i + 1 < args.length) {
                        number = Integer.parseInt(args[++i]);
                    } else {
                        throw new IllegalArgumentException("Missing value for -n parameter");
                    }
                    break;
                case "-r":
                    if (i + 1 < args.length) {
                        range = Integer.parseInt(args[++i]);
                        if (range <= 0) {
                            throw new IllegalArgumentException("Range must be positive");
                        }
                    } else {
                        throw new IllegalArgumentException("Missing value for -r parameter");
                    }
                    break;
                case "-e":
                    if (i + 1 < args.length) {
                        exerciseFile = args[++i];
                    } else {
                        throw new IllegalArgumentException("Missing value for -e parameter");
                    }
                    break;
                case "-a":
                    if (i + 1 < args.length) {
                        answerFile = args[++i];
                    } else {
                        throw new IllegalArgumentException("Missing value for -a parameter");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown parameter: " + args[i]);
            }
        }
    }

    public boolean hasNumberAndRange() {
        return number > 0 && range > 0;
    }

    public boolean hasExerciseAndAnswerFiles() {
        return exerciseFile != null && answerFile != null;
    }

    public int getNumber() {
        return number;
    }

    public int getRange() {
        return range;
    }

    public String getExerciseFile() {
        return exerciseFile;
    }

    public String getAnswerFile() {
        return answerFile;
    }
}