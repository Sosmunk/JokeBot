package org.bot;

import jakarta.persistence.*;

/**
 * Модель анекдота
 */
@Entity
@Table(name = "jokes")
public class Joke {

    public Joke() {
    }

    public Joke(String text) {
        this.text = text;
    }

    /**
     * id анекдота
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    /**
     * Текст анекдота
     */
    @Column(columnDefinition = "TEXT")
    private String text;

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setText(String text) {
        this.text = text;
    }
}
