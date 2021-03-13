package com.bilibili.ui.component;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sukidayo
 * @date 2021/1/27
 */
public abstract class AbsPage implements Page {

    protected final JFrame jFrame;
    private boolean initFlag = false;
    protected final List<JComponent> allJComponent = new ArrayList<>();

    public AbsPage(JFrame jFrame) {
        this.jFrame = jFrame;
    }

    @Override
    public void display() {
        if (!initFlag) {
            initFlag = true;
            init();
        }
        allJComponent.forEach(jComponent -> this.jFrame.getContentPane().add(jComponent));
    }

    @Override
    public void destroy() {
        allJComponent.forEach(jComponent -> this.jFrame.getContentPane().remove(jComponent));
        this.jFrame.repaint();
    }
}
