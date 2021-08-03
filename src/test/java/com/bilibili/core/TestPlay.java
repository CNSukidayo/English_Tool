package com.bilibili.core;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author sukidayo
 * @date 2021/1/26
 */
public class TestPlay {
    public static void main(String[] args) {
        File basicPath = Paths.get("F:\\工具\\英语\\四级词汇 乱序版音频 成品\\核心词汇\\target").toFile();
        File[] allFiles = basicPath.listFiles();
        List<File> list = new ArrayList<>(allFiles.length);
        for (File allFile : allFiles) {
            if (!allFile.getName().contains("基_")) {
                list.add(allFile);
            }
        }
        list.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getName().substring(0, o.getName().indexOf('.')).replaceAll("[a-zA-Z|-]", ""))));
        for (int i = 0; i < list.size(); i++) {
            File file = list.get(i);
            file.renameTo(new File("F:\\工具\\英语\\四级词汇 乱序版音频 成品\\核心词汇\\target",i+1 + file.getName().replaceAll(".mp3","").replaceAll("[0-9]", "") + ".mp3"));
        }
    }
}
