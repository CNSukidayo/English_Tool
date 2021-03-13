package com.bilibili.ui;

import com.bilibili.ui.component.Home;

import javax.swing.*;

/**
 * @author sukidayo
 * @date 2021/1/26
 */
public class EnglishToolFrame {

    private JFrame jFrame;
    // 资源地址
    public static final String basePath = "D:/Java Project/English Tool/resource/";
    // 音频地址(仅对输入模式起效果)
    public static final String audioPath = "day14";

    // json地址
    public static final String json = "json";

    public void init() {
        this.jFrame = new JFrame();
        this.jFrame.setBounds(500, 300, 720, 480);
        this.jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.jFrame.setTitle("English Tool");
        this.jFrame.setResizable(false);
        this.jFrame.setLayout(null);
    }

    public void run() {
        init();
        Home home = new Home(this.jFrame);
        home.display();
        this.jFrame.setVisible(true);
    }

}
