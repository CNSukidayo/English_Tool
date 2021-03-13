package com.bilibili.ui.component;

/**
 * @author sukidayo
 * @date 2021/1/27
 */
public interface Page {

    /**
     * 初始化当前界面
     */
    void init();

    /**
     * 显示所有资源
     */
    void display();


    /**
     * 销毁所有资源
     */
    void destroy();

}
