package com.bilibili.core;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 音频播放器
 *
 * @author sukidayo
 * @date 2021/1/27
 */
public class AudioPlayer {

    private final BlockingQueue<File> queue;
    private boolean flag = true;
    private Player player;
    private final ExecutorService service = Executors.newSingleThreadExecutor();

    public AudioPlayer() {
        this.queue = new LinkedBlockingDeque<>();
        start();
    }

    /**
     * 播放音频
     *
     * @param path 音频路径
     */
    public void play(String path) {
        service.execute(() -> {
            try {
                queue.put(new File(path));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void play(File path) {
        service.execute(() -> {
            try {
                queue.put(path);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void stopPlay(String path) {
        service.execute(() -> {
            stop();
            try {
                queue.put(new File(path));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void stopPlay(File path) {
        service.execute(() -> {
            stop();
            try {
                queue.put(path);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }


    public void stop() {
        if (player != null) {
            player.close();
        }
        queue.clear();
    }

    public void destroy() {
        this.flag = false;
    }

    private void start() {
        Thread consumer = new Thread(() -> {
            while (flag) {
                File wordPath = null;
                try {
                    wordPath = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try (BufferedInputStream playInputStream = new BufferedInputStream(new FileInputStream(wordPath))) {
                    player = new Player(playInputStream);
                    player.play();
                } catch (JavaLayerException | IOException e) {
                    e.printStackTrace();
                } finally {
                    player.close();
                }
            }
        });
        consumer.setDaemon(true);
        consumer.start();
    }


}
