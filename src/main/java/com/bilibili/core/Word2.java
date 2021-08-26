package com.bilibili.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sukidayo
 * @date 2021/1/26
 */
public class Word2 {
    private String english;
    /**
     * Key:词性 Value:对应的中文
     **/
    private Map<PartOfSpeechEnum, String> allChineseMap;
    private int days;
    private WordCategory category;
    private String audioPath;
    private String PREPPhrase;
    private String discriminate;

    public Word2() {
        allChineseMap = new HashMap<>(PartOfSpeechEnum.values().length);
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public WordCategory getCategory() {
        return category;
    }

    public void setCategory(WordCategory category) {
        this.category = category;
    }

    public Map<PartOfSpeechEnum, String> getAllChineseMap() {
        return allChineseMap;
    }

    public void setAllChineseMap(Map<PartOfSpeechEnum, String> allChineseMap) {
        this.allChineseMap = allChineseMap;
    }

    public String getPREPPhrase() {
        return PREPPhrase;
    }

    public void setPREPPhrase(String PREPPhrase) {
        this.PREPPhrase = PREPPhrase;
    }

    public String getDiscriminate() {
        return discriminate;
    }

    public void setDiscriminate(String discriminate) {
        this.discriminate = discriminate;
    }

    @Override
    public String toString() {
        return "Word{" +
                "english='" + english + '\'' +
                ", allChineseMap=" + allChineseMap +
                ", days=" + days +
                ", category=" + category +
                ", audioPath='" + audioPath + '\'' +
                '}';
    }
}
