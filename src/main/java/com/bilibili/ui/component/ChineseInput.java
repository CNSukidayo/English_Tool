package com.bilibili.ui.component;

import com.bilibili.core.PartOfSpeechEnum;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * @author sukidayo
 * @date 2021/1/29
 */
public class ChineseInput {
    // 当前是什么词性
    private final PartOfSpeechEnum partOfSpeech;
    //布局
    private final SpringLayout springLayout = new SpringLayout();
    private final JPanel rowJPanel;
    private final JLabel partOfSpeechJLabel;
    private final JTextComponent chineseInputJText;
    private final JLabel answerJLabel;
    // 共用的字体Font
    private final Font writeFont;
    //
    private int Y;
    private int X = 0;
    private final int index;

    //
    public ChineseInput(PartOfSpeechEnum partOfSpeech, Font writeFont, int index) {
        this.partOfSpeech = partOfSpeech;
        this.writeFont = writeFont;
        this.rowJPanel = new JPanel(springLayout);
        this.partOfSpeechJLabel = new JLabel(partOfSpeech.getName() + ".");
        if (partOfSpeech == PartOfSpeechEnum.PHTASESANDCOLLOCATION) {
            this.chineseInputJText = new JTextArea();
        } else {
            this.chineseInputJText = new JTextField();
        }
        this.answerJLabel = new JLabel("占位符");
        this.index = index;
        this.Y = index * 40;
        init();
    }

    public ChineseInput(PartOfSpeechEnum partOfSpeech, Font writeFont, JPanel targetPanel, int index) {
        this(partOfSpeech, writeFont, index);
        addAllComponentToPanel(targetPanel);
    }

    private final Color yellow = Color.YELLOW;

    /**
     * 将当前复合组件的所有组件添加到一个目标面板里
     *
     * @param targetPanel 被添加组件的面板
     */
    public void addAllComponentToPanel(JPanel targetPanel) {
        rowJPanel.setBounds(this.X, this.Y, 7000, 40);
        rowJPanel.setBackground(yellow);
        JPanel partOfSpeechJLabelPanel = new JPanel();
        partOfSpeechJLabelPanel.add(partOfSpeechJLabel);
        partOfSpeechJLabelPanel.setBackground(yellow);

        JPanel answerJLabelPanel = new JPanel();
        answerJLabelPanel.add(answerJLabel);
        answerJLabelPanel.setBackground(yellow);
        JComponent chineseInputJTextFieldPanel;
        if (partOfSpeech == PartOfSpeechEnum.PHTASESANDCOLLOCATION) {
            rowJPanel.setSize(7000, 3000);
            chineseInputJTextFieldPanel = new JScrollPane(chineseInputJText, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        } else {
            chineseInputJTextFieldPanel = new JPanel();
            chineseInputJTextFieldPanel.add(chineseInputJText);
            chineseInputJTextFieldPanel.setBackground(yellow);
        }
        springLayout.getConstraints(partOfSpeechJLabelPanel).setX(Spring.constant(0));
        springLayout.getConstraints(partOfSpeechJLabelPanel).setY(Spring.constant(0));
        springLayout.getConstraints(chineseInputJTextFieldPanel).setConstraint(SpringLayout.WEST, springLayout.getConstraints(partOfSpeechJLabelPanel).getConstraint(SpringLayout.EAST));
        springLayout.getConstraints(answerJLabelPanel).setConstraint(SpringLayout.WEST, springLayout.getConstraints(chineseInputJTextFieldPanel).getConstraint(SpringLayout.EAST));
        rowJPanel.add(chineseInputJTextFieldPanel);
        rowJPanel.add(partOfSpeechJLabelPanel);
        rowJPanel.add(answerJLabelPanel);
        rowJPanel.setBackground(yellow);
        targetPanel.add(rowJPanel);
    }


    public void changeXY(int X, int Y) {
        if (this.X - X >= 0) {
            this.X = 0;
            X = 0;
        }
        rowJPanel.setLocation(this.X - X, this.Y - Y);
    }

    void mouseReleased() {
        int temp = this.index * 40;
        if (this.rowJPanel.getY() > temp) {
            rowJPanel.setLocation(rowJPanel.getX(), temp);
        }
        this.Y = rowJPanel.getY();
        this.X = rowJPanel.getX();
    }

    private void init() {
        this.partOfSpeechJLabel.setFont(writeFont);
        this.chineseInputJText.setFont(writeFont);
        this.answerJLabel.setFont(writeFont);
        if (chineseInputJText instanceof JTextField) {
            ((JTextField) this.chineseInputJText).setColumns(35);
        } else if (chineseInputJText instanceof JTextArea) {
            ((JTextArea) this.chineseInputJText).setColumns(30);
            ((JTextArea) this.chineseInputJText).setRows(5);
        }
        this.partOfSpeechJLabel.setVisible(true);
        this.chineseInputJText.setVisible(true);
        this.answerJLabel.setVisible(true);
    }

    public JLabel getPartOfSpeechJLabel() {
        return partOfSpeechJLabel;
    }

    public JTextComponent getChineseInputJTextField() {
        return chineseInputJText;
    }

    public JLabel getAnswerJLabel() {
        return answerJLabel;
    }

    public PartOfSpeechEnum getPartOfSpeech() {
        return partOfSpeech;
    }
}
