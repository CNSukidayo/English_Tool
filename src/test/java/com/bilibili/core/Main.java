package com.bilibili.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        run1();
    }

    public static void run1() {
        File basicPath = Paths.get("F:\\工具\\英语\\四级词汇 乱序版音频 成品\\核心词汇\\day30\\Day 30").toFile();
        File[] allFiles = basicPath.listFiles();
        List<File> list = new ArrayList<>(allFiles.length);
        for (File allFile : allFiles) {
            if (allFile.getName().contains("Da")) {
                list.add(allFile);
            }
        }
        int prefix = allFiles.length - list.size() + 1;
        int index = 0;
        Scanner scanner = new Scanner(System.in);
        AudioPlayer audioPlayer = new AudioPlayer();
        while (true) {
            File now = list.get(index);
            audioPlayer.stopPlay(now);
            System.out.println(now + "---" + prefix);
            String input = scanner.next();
            audioPlayer.stop();
            synchronized (AudioPlayer.class) {
                try {
                    Files.move(now.toPath(), now.toPath().resolveSibling(prefix + input + ".mp3"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            index++;
            prefix++;
        }
    }

    public static void run2() {
        Path path = Paths.get("F:\\工具\\英语\\四级词汇 乱序版音频 成品\\核心词汇\\1.txt");
        try {
            Files.move(path, path.resolveSibling("233"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void run3() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        //for循环的list.size()是动态变的，以及list这样会导致
        for (int i = 0; i < list.size(); i++) {
            list.remove(i);
        }
        System.out.println(list);
    }

}