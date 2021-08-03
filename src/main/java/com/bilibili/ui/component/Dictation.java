package com.bilibili.ui.component;

import cn.hutool.json.JSONUtil;
import com.bilibili.core.AudioPlayer;
import com.bilibili.core.Mode;
import com.bilibili.core.Word;
import com.bilibili.core.WordCategory;
import com.bilibili.ui.EnglishToolFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;

/**
 * @author sukidayo
 * @date 2021/1/27
 */
public class Dictation extends AbsPage implements Page {

    private int current = 0;
    // 内存中寄存当前用户输入的所有单词
    private Map<Integer, Word> words;
    //schedule进度显示
    private final JLabel schedule = new JLabel();
    // 主页按钮
    private final JButton homeJButton = new JButton("主页");
    // 检查回答正确提示标签
    private final JLabel correct = new JLabel("正确");
    // 检查回答错误提示标签
    private final JLabel error = new JLabel("错误");
    // 点击检查后出现的结果标签
    private final JLabel result = new JLabel("结果");
    // 输入单词框
    private final JTextField input = new JTextField();
    // 查看按钮
    private final JButton check = new JButton("查看");
    // 标记按钮
    private final JButton sign = new JButton("标记单词");
    // 标记模式
    private final JLabel signMode = new JLabel("开启标记模式");
    private boolean signFlag = false;
    private boolean[] signArray;
    // 上一个按钮
    private final JButton pre = new JButton("上一个");
    // 下一个按钮
    private final JButton next = new JButton("下一个");
    // 播放按钮
    private final JButton play = new JButton("播放");
    private final JButton stop = new JButton("暂停");
    // ChinesePanel
    private final JPanel chineseJPanel = new JPanel();
    // allChineseInput
    protected AllChineseInput allChineseInput;
    // moveHorizontally
    private final JPanel moveHorizontally = new JPanel();
    // moveVertically
    private final JPanel moveVertically = new JPanel();
    // 全局移动
    private final JPanel fullyMobile = new JPanel();
    // 录入模式按钮
    private final JButton outputJSON = new JButton("输出JSON");
    // 刷新JSON
    private final JButton refreshJson = new JButton("刷新JSON");
    // 控制录入模式+主页+查看按钮的面板
    private final JPanel masterJPanel = new JPanel();
    // 让当前主窗口获得焦点的JPanel
    private final JTextField focus = new JTextField();
    // 宋体 加粗 40号字体
    private final Font promptFont = new Font("宋体", Font.BOLD, 40);
    // 宋体 无 25号字体
    private final Font writeFont = new Font("宋体", Font.BOLD, 20);
    // 宋体 加粗 25号字体
    private final Font resultFont = new Font("宋体", Font.BOLD, 25);
    // 宋体 无 35号字体
    private final Font inputFont = new Font("宋体", Font.PLAIN, 35);
    private final Font smallFont = new Font("宋体", Font.BOLD, 15);
    // 用于存储主页对象 相互引用 获得玩家在主页设置的信息
    private Home home;

    // 操作名称
    private String operatingName;
    // 黑暗绿色
    private final Color darkGreen = new Color(0x19610A);
    // 黑暗红色
    private final Color darkRed = new Color(0x580513);
    // 第几天
    private final int day = Integer.parseInt(EnglishToolFrame.audioPath.replaceAll("[day]",""));
    // 当前页面快捷键
    public Dictation(JFrame jFrame) {
        super(jFrame);
        this.allJComponent.add(correct);
        this.allJComponent.add(error);
        this.allJComponent.add(result);
        this.allJComponent.add(input);
        this.allJComponent.add(pre);
        this.allJComponent.add(next);
        this.allJComponent.add(play);
        this.allJComponent.add(stop);
        this.allJComponent.add(chineseJPanel);
        this.allJComponent.add(moveHorizontally);
        this.allJComponent.add(moveVertically);
        this.allJComponent.add(fullyMobile);
        this.allJComponent.add(masterJPanel);
        this.allJComponent.add(schedule);
        this.allJComponent.add(focus);
    }

    @Override
    public void display() {
        super.display();
        this.jFrame.setLayout(null);
        this.jFrame.setSize(720, 480);
        if (home.getMode() == Mode.INPUT) {
            this.masterJPanel.add(outputJSON, 0);
            this.masterJPanel.remove(refreshJson);
        } else {
            this.masterJPanel.remove(outputJSON);
            this.masterJPanel.add(refreshJson, 0);
        }
        this.check.setVisible(home.getMode() != Mode.INPUT);
        this.sign.setVisible(home.getMode() != Mode.INPUT);
        this.signMode.setVisible(home.getMode() != Mode.INPUT);
        // words = new HashMap<>(100);
        current = 0;
        signArray = new boolean[home.getWords().size()];
        flush();
    }


    @Override
    public void init() {
        this.masterJPanel.setBounds(605, 192, 100, 250);
        this.masterJPanel.setLayout(new GridLayout(5, 1));
        this.masterJPanel.add(outputJSON);
        this.masterJPanel.add(refreshJson);
        this.masterJPanel.add(check);
        this.masterJPanel.add(sign);
        this.sign.setBackground(green);
        this.sign.setFont(smallFont);
        this.masterJPanel.add(signMode);
        this.masterJPanel.add(homeJButton);

        this.refreshJson.setForeground(new Color(0xCB4BA2));

        this.correct.setBounds(310, -20, 100, 100);
        this.correct.setFont(promptFont);
        this.correct.setForeground(new Color(0x327911));

        this.error.setBounds(310, -20, 100, 100);
        this.error.setFont(promptFont);

        this.error.setForeground(darkRed);

        this.result.setBounds(320, 80, 300, 100);
        this.result.setFont(resultFont);

        this.input.setBounds(50, 50, 600, 50);
        this.input.setFont(inputFont);

        this.pre.setBounds(0, 100, 80, 50);
        this.pre.setName("pre");
        this.play.setBounds(80, 100, 80, 50);
        this.play.setName("play");
        this.stop.setBounds(160, 100, 80, 50);
        this.stop.setName("stop");
        this.next.setBounds(240, 100, 80, 50);
        this.next.setName("next");
        this.pre.addMouseListener(this.play());
        this.play.addMouseListener(this.play());
        this.next.addMouseListener(this.play());
        this.stop.addMouseListener(this.play());
        // 中文输入部分处理
        this.chineseJPanel.setBounds(0, 180, 470, 230);
        this.chineseJPanel.setBackground(Color.YELLOW);
        this.chineseJPanel.setLayout(null);
        this.chineseJPanel.addMouseWheelListener(getMouseWheelMouseAdapter());
        this.allChineseInput = new AllChineseInput(chineseJPanel, writeFont);
        this.allChineseInput.setFinishAdapter((s) -> {
            operatingName = s;
            playEvent.mousePressed(null);
        });
        // 垂直
        this.moveHorizontally.setBounds(470, 180, 30, 230);
        Color green = Color.GREEN;
        this.moveHorizontally.setBackground(green);
        this.moveHorizontally.setName("Vertically");
        this.moveHorizontally.addMouseListener(getUniversalMouseAdapter());
        this.moveHorizontally.addMouseMotionListener(getUniversalMouseAdapter());
        this.moveHorizontally.addMouseWheelListener(getMouseWheelMouseAdapter());

        // 水平
        this.moveVertically.setBounds(0, 410, 470, 30);
        this.moveVertically.setBackground(green);
        this.moveVertically.setName("Horizontally");
        this.moveVertically.addMouseListener(getUniversalMouseAdapter());
        this.moveVertically.addMouseMotionListener(getUniversalMouseAdapter());

        // 全面移动
        this.fullyMobile.setBounds(470, 410, 30, 30);
        this.fullyMobile.setBackground(new Color(0xFF4943));
        this.fullyMobile.setName("Fully");
        this.fullyMobile.addMouseListener(getUniversalMouseAdapter());
        this.fullyMobile.addMouseMotionListener(getUniversalMouseAdapter());

        this.check.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                check();
            }
        });

        this.outputJSON.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                saveWord();
                String outPath = EnglishToolFrame.basePath + EnglishToolFrame.json + "/" + EnglishToolFrame.audioPath + "." + EnglishToolFrame.json;
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outPath))) {
                    JSONUtil.parse(words.values()).write(writer, 4, 0);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        this.refreshJson.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                final Map<String, Word> tempMap = new HashMap<>(80);
                for (ChoseDayUnit day : home.getDays()) {
                    if (day.isChose()) {
                        List<Word> temp = JSONUtil.parseArray(JSONUtil.readJSON(day.getJsonFile().getAbsoluteFile(), StandardCharsets.UTF_8).toStringPretty()).toList(Word.class);
                        for (Word word : temp) {
                            tempMap.put(word.getEnglish(), word);
                        }
                    }
                }
                for (int i = 0; i < home.getWords().size(); i++) {
                    home.getWords().set(i, tempMap.get(home.getWords().get(i).getEnglish()));
                }
                allChineseInput.setAnswerLabelTextFromWord(home.getWords().get(current));
            }
        });

        this.sign.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                signArray[current] = !signArray[current];
                checkSign();
            }
        });
        this.signMode.setHorizontalAlignment(JLabel.CENTER);
        this.signMode.setForeground(darkGreen);
        this.signMode.setFont(smallFont);
        this.signMode.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                signFlag = !signFlag;
                if (signFlag) {
                    signMode.setText("关闭标记模式");
                    signMode.setForeground(darkRed);
                } else {
                    signMode.setText("开启标记模式");
                    signMode.setForeground(darkGreen);
                }
            }
        });
        this.homeJButton.addActionListener(e -> {
            destroy();
            home.display();
        });
        this.schedule.setBounds(0, 0, 200, 30);

        focus.setBounds(660, 0, 30, 50);
        focus.setBackground(darkGreen);
        focus.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                focus.setBackground(darkRed);
            }

            @Override
            public void focusLost(FocusEvent e) {
                focus.setBackground(darkGreen);
            }
        });
        focus.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                focus.setText("");
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    operatingName = "next";
                    playEvent.mousePressed(null);
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    operatingName = "pre";
                    playEvent.mousePressed(null);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    check();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    operatingName = "stop";
                    playEvent.mousePressed(null);
                }
            }
        });
    }

    private MouseAdapter playEvent = null;

    private MouseAdapter play() {
        if (playEvent == null) {
            // 音频播放器
            AudioPlayer audioPlayer = new AudioPlayer();
            playEvent = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    String name = operatingName;
                    if (e != null) {
                        name = ((JButton) e.getSource()).getName();
                    }
                    if (name.equals("stop")) {
                        audioPlayer.stop();
                        return;
                    }
                    correct.setVisible(false);
                    error.setVisible(false);
                    result.setVisible(false);
                    // 保存切换单词之前的单词
                    saveWord();
                    if (!name.equals("play")) {
                        flush();
                    }
                    if (name.equals("next")) {
                        if (home.getMode() == Mode.INPUT) {
                            if (current == allFiles.length - 1) {
                                current = 0;
                            } else {
                                current++;
                            }
                        } else {
                            if (current == home.getWords().size() - 1) {
                                current = 0;
                            } else {
                                current++;
                            }
                            if (signFlag) {
                                current = find(1);
                            }
                        }
                    } else if (name.equals("pre")) {
                        if (home.getMode() == Mode.INPUT) {
                            if (current == 0) {
                                current = allFiles.length - 1;
                            } else {
                                current--;
                            }
                        } else {
                            if (current == 0) {
                                current = home.getWords().size() - 1;
                            } else {
                                current--;
                            }
                        }
                        if (signFlag) {
                            current = find(-1);
                        }
                    }
                    if (home.getMode() == Mode.INPUT) {
                        audioPlayer.stopPlay(allFiles[current].getAbsoluteFile());
                        schedule.setText(current + 1 + "/" + allFiles.length);
                    } else {
                        audioPlayer.stopPlay(home.getWords().get(current).getAudioPath());
                        schedule.setText(current + 1 + "/" + home.getWords().size());
                    }
                    // 只要模式不是听写模式就显示当前播报的单词
                    result.setText("");
                    if (home.getMode() == Mode.INPUT) {
                        result.setText(allFiles[current].getName().substring(0, allFiles[current].getName().lastIndexOf('.')));
                    } else if (home.getMode() == Mode.TRANSLATION) {
                        result.setText(home.getWords().get(current).getEnglish());
                    }
                    result.setVisible(true);
                    // 加载新位置的单词
                    Word nowWord = words.get(current);
                    if (nowWord != null) {
                        input.setText(nowWord.getEnglish());
                        allChineseInput.setInPutTextFromWord(nowWord);
                    }
                    if (home.getMode() != Mode.INPUT) {
                        checkSign();
                    }
                }
            };
        }
        return playEvent;
    }

    private File[] allFiles;
    private Set<String> duplicateTree = new HashSet<>();

    /**
     * 当模式为录入模式时才会调用该方法
     */
    public void allFiles() {
        int current = 0;
        List<File> temp;
        temp = Arrays.asList(Objects.requireNonNull(Paths.get(EnglishToolFrame.basePath + EnglishToolFrame.audioPath).toFile().listFiles()));
        temp.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getName().substring(0, o.getName().indexOf('.')).replaceAll("[a-zA-Z|\\-|' ]", ""))));
        words = new HashMap<>(100);
        allFiles = new File[temp.size()];
        // 现在添加去重功能
        final List<File> duplicate = new ArrayList<>(26);
        duplicate.add(new File("D:\\Java Project\\English Tool\\resource\\json\\basic.json"));
        File baseFile = new File(EnglishToolFrame.basePath + EnglishToolFrame.json);
        for (int i = 1; i <= 25; i++) {
            duplicate.add(new File(baseFile, "day" + i + ".json"));
        }
        for (File file : duplicate) {
            for (Word word : JSONUtil.parseArray(JSONUtil.readJSON(file, StandardCharsets.UTF_8).toStringPretty()).toList(Word.class)) {
                duplicateTree.add(word.getEnglish());
            }
        }
        for (int i = 0; i < temp.size(); i++) {
            String name = temp.get(i).getName();
            if (!duplicateTree.contains(name.substring(0, name.indexOf('.')).replaceAll("[0-9]", ""))) {
                allFiles[current++] = temp.get(i);
            }
        }
        allFiles = Arrays.copyOf(allFiles, current, File[].class);
    }

    public void setHome(Home home) {
        this.home = home;
    }

    private MouseAdapter universalMouseAdapter;

    private MouseAdapter getUniversalMouseAdapter() {
        if (universalMouseAdapter == null) {
            universalMouseAdapter = new MouseAdapter() {
                private int clickX;
                private int clickY;
                private int status;
                private boolean clickFlag;

                @Override
                public void mousePressed(MouseEvent e) {
                    clickFlag = true;
                    clickX = 0;
                    clickY = 0;
                    clickX = e.getX();
                    clickY = e.getY();
                    if (((JComponent) e.getSource()).getName().equals("Fully")) {
                        fullyMobile.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                        status = 2;
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    clickFlag = false;
                    if (((JComponent) e.getSource()).getName().equals("Fully")) {
                        fullyMobile.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                    allChineseInput.mouseReleased();
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (status == 0) {
                        allChineseInput.moveXY(0, e.getY() - clickY);
                    }
                    if (status == 1) {
                        allChineseInput.moveXY(e.getX() - clickX, 0);
                    }
                    if (status == 2) {
                        allChineseInput.moveXY(e.getX() - clickX, e.getY() - clickY);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!clickFlag) {
                        if (((JComponent) e.getSource()).getName().equals("Horizontally")) {
                            moveHorizontally.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                            status = 1;
                        } else if (((JComponent) e.getSource()).getName().equals("Vertically")) {
                            moveVertically.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                            status = 0;
                        }
                    }
                }
            };
        }
        return universalMouseAdapter;
    }

    private MouseAdapter mouseWheelMouseAdapter;

    private MouseAdapter getMouseWheelMouseAdapter() {
        if (mouseWheelMouseAdapter == null) {
            mouseWheelMouseAdapter = new MouseAdapter() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    allChineseInput.moveXY(0, e.getWheelRotation() * 25);
                    allChineseInput.mouseReleased();
                }
            };
        }
        return mouseWheelMouseAdapter;
    }

    // 显示答案
    private void check() {
        correct.setVisible(false);
        error.setVisible(false);
        result.setVisible(true);
        result.setText(home.getWords().get(current).getEnglish());
        // 设置答案Label内容
        allChineseInput.setAnswerLabelTextFromWord(home.getWords().get(current));
        if (input.getText().equalsIgnoreCase(home.getWords().get(current).getEnglish())) {
            correct.setVisible(true);
        } else {
            error.setVisible(true);
        }
    }

    private void saveWord() {
        if (!allChineseInput.needSave()) {
            return;
        }
        Word word = new Word();
        word.setDays(day);
        word.setCategory(WordCategory.COGNIYIVE);
        if (home.getMode() == Mode.INPUT) {
            word.setEnglish(allFiles[current].getName().substring(0, allFiles[current].getName().lastIndexOf('.')).replaceAll("[0-9]", ""));
            word.setAudioPath(allFiles[current].getPath());
        } else {
            word.setEnglish(input.getText().trim());
        }
        allChineseInput.setWordFromInPutText(word);
        words.put(current, word);
    }

    private void flush() {
        input.setText("");
        allChineseInput.setInPutTextFromWord(null);
        correct.setVisible(false);
        error.setVisible(false);
        result.setText("");
    }

    private int find(int sign) {
        for (int i = current; i < signArray.length && i > -1; i += sign) {
            if (signArray[i]) {
                return i;
            }
        }
        for (int i = sign == 1 ? 0 : signArray.length - 1; i < signArray.length && i > -1; i += sign) {
            if (signArray[i]) {
                return i;
            }
        }
        return current;
    }

    private final Color red = new Color(0xFF8175);
    private final Color green = new Color(0x80FF62);

    private void checkSign() {
        if (signArray[current]) {
            sign.setBackground(red);
            sign.setText("解除标记");
        } else {
            sign.setBackground(green);
            sign.setText("标记单词");
        }
    }

}
