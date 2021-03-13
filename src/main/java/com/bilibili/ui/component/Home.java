package com.bilibili.ui.component;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.bilibili.core.Mode;
import com.bilibili.core.Word;
import com.bilibili.ui.EnglishToolFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sukidayo
 * @date 2021/1/27
 */
public class Home extends AbsPage implements Page {

    private File[] allJsons = null;

    private final Font bigFont = new Font("宋体", Font.BOLD, 20);

    private final Color green = Color.GREEN;
    private final Color ren = Color.RED;
    // 所选择的所有天数的集合
    private final List<ChoseDayUnit> days = new ArrayList<>(15);
    // 橙色
    private final Color modeNoChoseColor = new Color(0xFFC754);
    private final Color modeChoseColor = new Color(0x876F19);
    // 模式 0代表听写 1代表翻译
    private Mode mode = Mode.DICTATION;

    private final Dictation dictation = new Dictation(this.jFrame);
    // 当前用户所选的所有单词的缓存
    private List<Word> words;

    public Home(JFrame jFrame) {
        super(jFrame);
    }

    @Override
    public void init() {
        allJsons = Paths.get(EnglishToolFrame.basePath + EnglishToolFrame.json).toFile().listFiles();
        Arrays.sort(allJsons, (o1, o2) -> {
            String path1 = o1.getName().substring(0, o1.getName().lastIndexOf('.'));
            String path2 = o2.getName().substring(0, o2.getName().lastIndexOf('.'));
            if (path1.length() != path2.length()) {
                return path1.length() - path2.length();
            } else {
                return path1.compareTo(path2);
            }
        });

        for (int i = 0; i < allJsons.length; i++) {
            ChoseDayUnit choseDayUnit = new ChoseDayUnit(green, ren);
            days.add(choseDayUnit);
            this.allJComponent.add(choseDayUnit.apply(allJsons[i]));
        }
        // 全选
        JPanel allIn = new JPanel(new BorderLayout());
        JLabel allInJLabel = new JLabel("全选");
        allInJLabel.setHorizontalAlignment(JLabel.CENTER);
        allInJLabel.setFont(bigFont);
        allIn.add(allInJLabel, BorderLayout.CENTER);
        allIn.setBackground(new Color(0x6EFFC4));
        this.allJComponent.add(allIn);
        allIn.addMouseListener(new MouseAdapter() {
            private boolean allInFlag = true;

            @Override
            public void mousePressed(MouseEvent e) {
                if (allInFlag) {
                    allInJLabel.setText("全不选");
                } else {
                    allInJLabel.setText("全选");
                }
                for (ChoseDayUnit day : days) {
                    day.setChose(allInFlag);
                }
                allInFlag = !allInFlag;
            }
        });
        // start
        JPanel start = new JPanel(new BorderLayout());
        JLabel startJLabel = new JLabel("start");
        startJLabel.setHorizontalAlignment(JLabel.CENTER);
        startJLabel.setFont(bigFont);
        start.add(startJLabel, BorderLayout.CENTER);
        start.setBackground(new Color(0xFE7EFF));
        start.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                words = new ArrayList<>(100);
                // 获得用户当前选中的所有单词
                for (ChoseDayUnit day : days) {
                    if (day.isChose()) {
                        List<Word> temp = JSONUtil.parseArray(JSONUtil.readJSON(day.getJsonFile().getAbsoluteFile(), StandardCharsets.UTF_8).toStringPretty()).toList(Word.class);
                        words.addAll(randomEleList(temp, temp.size()));
                    }
                }
                destroy();
                dictation.setHome(Home.this);
                if (mode == Mode.INPUT) {
                    dictation.allFiles();
                }
                dictation.display();
            }
        });
        this.allJComponent.add(start);
        // 听写模式
        JPanel dictation = new JPanel(new BorderLayout());
        JLabel dictationJLabel = new JLabel("听写模式");
        dictationJLabel.setHorizontalAlignment(JLabel.CENTER);
        dictationJLabel.setFont(bigFont);
        dictation.add(dictationJLabel, BorderLayout.CENTER);
        dictation.setBackground(modeChoseColor);
        this.allJComponent.add(dictation);
        // 英译中
        JPanel translation = new JPanel(new BorderLayout());
        JLabel translationLabel = new JLabel("翻译模式");
        translationLabel.setHorizontalAlignment(JLabel.CENTER);
        translationLabel.setFont(bigFont);
        translation.add(translationLabel, BorderLayout.CENTER);
        translation.setBackground(modeNoChoseColor);
        this.allJComponent.add(translation);
        // 录入模式
        JPanel input = new JPanel(new BorderLayout());
        JLabel inputLabel = new JLabel("录入模式");
        inputLabel.setHorizontalAlignment(JLabel.CENTER);
        inputLabel.setFont(bigFont);
        input.add(inputLabel, BorderLayout.CENTER);
        input.setBackground(modeNoChoseColor);
        this.allJComponent.add(input);

        dictation.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mode = Mode.DICTATION;
                dictation.setBackground(modeChoseColor);
                translation.setBackground(modeNoChoseColor);
                input.setBackground(modeNoChoseColor);
            }
        });
        translation.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mode = Mode.TRANSLATION;
                dictation.setBackground(modeNoChoseColor);
                translation.setBackground(modeChoseColor);
                input.setBackground(modeNoChoseColor);
            }
        });
        input.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mode = Mode.INPUT;
                dictation.setBackground(modeNoChoseColor);
                translation.setBackground(modeNoChoseColor);
                input.setBackground(modeChoseColor);
            }
        });

    }

    @Override
    public void display() {
        super.display();
        GridLayout gridLayout = new GridLayout(allJsons.length / 5 + 1, 5);
        gridLayout.setHgap(10);
        gridLayout.setVgap(10);
        this.jFrame.setLayout(gridLayout);
        this.jFrame.setSize(720, 480);
    }

    public Mode getMode() {
        return mode;
    }

    public List<Word> getWords() {
        return words;
    }

    public List<ChoseDayUnit> getDays() {
        return days;
    }

    private <T> List<T> randomEleList(List<T> source, int count) {
        if (count > source.size()) {
            return source;
        }
        final int[] randomList = ArrayUtil.sub(RandomUtil.randomInts(source.size()), 0, count);
        List<T> result = new ArrayList<>();
        for (int e : randomList) {
            result.add(source.get(e));
        }
        return result;
    }

}
