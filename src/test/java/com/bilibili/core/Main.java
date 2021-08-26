package com.bilibili.core;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        run4();
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

    public static void run4() {
        File[] files = new File("D:\\Java Project\\English Tool\\resource\\json").listFiles();
        for (File file : files) {
            List<Word> words = JSONUtil.parseArray(JSONUtil.readJSON(file, StandardCharsets.UTF_8).toStringPretty()).toList(Word.class);
            List<Word2> target = new ArrayList<>(words.size());
            for (Word word : words) {
                String prep = word.getAllChineseMap().remove(PartOfSpeechEnum.PHTASESANDCOLLOCATION);
                Word2 word2 = BeanUtil.copyProperties(word, Word2.class, "");
                word2.setPREPPhrase(prep);
                if (word2.getDiscriminate() == null) {
                    word2.setDiscriminate("");
                }
                target.add(word2);
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\Java Project\\English Tool\\resource\\newJson\\" + file.getName()))) {
                JSONUtil.parse(target).write(writer, 4, 0);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

}