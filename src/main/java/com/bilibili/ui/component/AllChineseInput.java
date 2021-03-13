package com.bilibili.ui.component;

import com.bilibili.core.PartOfSpeechEnum;
import com.bilibili.core.Word;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sukidayo
 * @date 2021/1/29
 */
public class AllChineseInput {
    private final List<ChineseInput> allChineseInputList;
    private final JPanel targetPanel;

    public AllChineseInput(JPanel targetPanel, Font writeFont) {
        this.targetPanel = targetPanel;
        this.allChineseInputList = new ArrayList<>(PartOfSpeechEnum.values().length);
        for (int i = 0; i < PartOfSpeechEnum.values().length; i++) {
            allChineseInputList.add(new ChineseInput(PartOfSpeechEnum.values()[i], writeFont, targetPanel, i));
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

    void mouseReleased() {
        for (ChineseInput chineseInput : allChineseInputList) {
            chineseInput.mouseReleased();
        }
    }

}
