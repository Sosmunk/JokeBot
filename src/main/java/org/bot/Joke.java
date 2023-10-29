package org.bot;

/**
 * Модель анекдота
 */

public class Joke {
    /**
     * id анекдота
     */
    private Integer id;

    /**
     * Текст анекдота
     */
    private String text;

    /**
     * Получить id анекдота
     */
    public Integer getId() {
        return id;
    }

    /**
     * Получить текст анекдота
     * @return
     */
    public String getText() {
        return text;
    }
}
