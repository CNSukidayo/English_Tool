package com.bilibili;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sukidayo
 * @date 2021/3/11
 */
class MainUITest {
    public static void main(String[] args) {
        Path path = Paths.get("F:\\常用\\CloudMusicPJ\\download\\所有音乐歌单");
        Map<String, File> allMusic = new HashMap<>(100);
        for (File file : path.toFile().listFiles()) {
            allMusic.put(file.getName(), file);
        }
        Path path2 = Paths.get("F:\\常用\\CloudMusicPJ\\download\\可以听歌单");
        for (File file : path2.toFile().listFiles()) {
            String target = file.getName().replaceAll(".lnk", "").replaceAll(" - 快捷方式", "");
            File file1 = allMusic.get(target);
            try {
                Files.copy(file1.toPath(), Paths.get("F:\\常用\\CloudMusicPJ\\download\\temp", target));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}