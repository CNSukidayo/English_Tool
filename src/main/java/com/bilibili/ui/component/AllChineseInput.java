package com.bilibili.ui.component;

import com.bilibili.core.PartOfSpeechEnum;
import com.bilibili.core.Word;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author sukidayo
 * @date 2021/1/29
 */
public class AllChineseInput {
    private final List<ChineseInput> allChineseInputList;
    private final JPanel targetPanel;
    private Consumer<String> finishAdapter;

    public AllChineseInput(JPanel targetPanel, Font writeFont) {
        this.targetPanel = targetPanel;
        this.allChineseInputList = new ArrayList<>(PartOfSpeechEnum.values().length);
        for (int i = 0; i < PartOfSpeechEnum.values().length; i++) {
            ChineseInput chineseInput = new ChineseInput(PartOfSpeechEnum.values()[i], writeFont, targetPanel, i);
            chineseInput.getChineseInputJTextField().addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        finishAdapter.accept("next");
                    } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        finishAdapter.accept("pre");
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        int focus = getFocus();
                        if (focus == 0) {
                            focus = allChineseInputList.size();
                        }
                        allChineseInputList.get(--focus).getChineseInputJTextField().requestFocus();
                        moveXY(0, 40 * focus);
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        int focus = getFocus();
                        if (focus == allChineseInputList.size() - 1) {
                            focus = -1;
                        }
                        allChineseInputList.get(++focus).getChineseInputJTextField().requestFocus();
                        moveXY(0, 40 * focus);
                    }
                }
            });
            allChineseInputList.add(chineseInput);
        }
    }

    /**
     * 只需要关注改变的量即可
     *
     * @param X X轴的偏移量
     * @param Y Y轴的偏移量
     */
    public void moveXY(int X, int Y) {
        for (ChineseInput chineseInput : allChineseInputList) {
            chineseInput.changeXY(X, Y);
        }
        targetPanel.repaint();
    }

    /**
     * 调用该方法 传入单词对象,该方法会将所有的中文输入框中的内容设置到单词的PartOfSpeech枚举类上<br>
     * 无需关注具体实现,非常好用.
     *
     * @param word 传入将要被设置中文意思的单词
     */
    public void setWordFromInPutText(Word word) {
        for (ChineseInput chineseInput : this.allChineseInputList) {
            word.getAllChineseMap().put(chineseInput.getPartOfSpeech(), chineseInput.getChineseInputJTextField().getText());
        }
    }

    /**
     * 传入单词对象,设置中文输入框中的内容为单词内容.该方法主要是回忆功能
     *
     * @param word 传入单词对象 如果传入值为null则将中文输入框中的内容清除
     */
    public void setInPutTextFromWord(Word word) {
        if (word == null) {
            for (ChineseInput chineseInput : this.allChineseInputList) {
                chineseInput.getAnswerJLabel().setText("");
                chineseInput.getChineseInputJTextField().setText("");
            }
        } else {
            for (ChineseInput chineseInput : this.allChineseInputList) {
                chineseInput.getChineseInputJTextField().setText(word.getAllChineseMap().get(chineseInput.getPartOfSpeech()));
            }
        }
    }

    /**
     * 传入单词对象,设置答案框的内容为单词内容.该方法主要是检查功能
     *
     * @param word 传入单词对象
     */
    public void setAnswerLabelTextFromWord(Word word) {
        for (ChineseInput chineseInput : this.allChineseInputList) {
            String text = word.getAllChineseMap().get(chineseInput.getPartOfSpeech());
            if (text.contains("\n")) {
                text = "<html><body>" + text.replaceAll("\n", "<br>") + "<body><html>";
            }
            chineseInput.getAnswerJLabel().setText(text);
        }
    }

    public boolean needSave() {
        boolean flag = false;
        for (ChineseInput chineseInput : allChineseInputList) {
            if (!chineseInput.getChineseInputJTextField().getText().isEmpty()) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public void setFinishAdapter(Consumer<String> finishAdapter) {
        this.finishAdapter = finishAdapter;
    }

    void mouseReleased() {
        for (ChineseInput chineseInput : allChineseInputList) {
            chineseInput.mouseReleased();
        }
    }

    private int getFocus() {
        for (int i = 0; i < allChineseInputList.size(); i++)
            if (allChineseInputList.get(i).getChineseInputJTextField().hasFocus()) return i;
        return 0;
    }

}
