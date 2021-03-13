package com.bilibili.ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.function.Function;

/**
 * @author sukidayo
 * @date 2021/1/27
 */
public class ChoseDayUnit implements Function<File, JComponent> {

    private final Color green;
    private final Color red;
    private boolean chose = false;
    private JComponent jComponent;
    private File jsonFile;
    public ChoseDayUnit(Color green, Color red) {
        this.green = green;
        this.red = red;
    }

    @Override
    public JComponent apply(File file) {
        this.jsonFile = file;
        JPanel daysClickJPanel = new JPanel(new BorderLayout());
        JLabel daysClickJLabel = new JLabel(file.getName().substring(0, file.getName().lastIndexOf('.')));
        daysClickJLabel.setHorizontalAlignment(JLabel.CENTER);
        daysClickJPanel.add(daysClickJLabel, BorderLayout.CENTER);
        daysClickJPanel.setBackground(green);

        daysClickJPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setChose(!chose);
            }
        });
        this.jComponent = daysClickJPanel;
        return daysClickJPanel;
    }

    public void setChose(boolean chose) {
        this.chose = chose;
        if (chose) {
            jComponent.setBackground(red);
        } else {
            jComponent.setBackground(green);
        }
    }

    public boolean isChose() {
        return chose;
    }

    public File getJsonFile() {
        return jsonFile;
    }
}
