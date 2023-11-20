package org.bot;

import jakarta.persistence.*;

/**
 * Модель анекдота
 */
@Entity
@Table(name="jokes")
public class Joke {
    /**
     * id анекдота
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

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
