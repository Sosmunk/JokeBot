package org.bot;

import jakarta.persistence.*;

/**
 * Рейтинг анекдота
 */
@Entity
@Table(name = "ratings")
public class Rate {
    public Rate() {
    }

    public Rate(Long chatId, Byte stars, Joke joke) {
        this.chatId = chatId;
        this.stars = stars;
        this.joke = joke;
    }

    /**
     * Идентификатор рейтинга
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * id чата в котором оценивается анекдот
     */
    private Long chatId;

    /**
     * Количество звезд рейтинга (1-5)
     */
    private Byte stars;

    /**
     * Анекдот для оценивания
     */
    @ManyToOne(fetch = FetchType.EAGER)
    private Joke joke;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Byte getStars() {
        return stars;
    }

    public void setStars(Byte stars) {
        this.stars = stars;
    }

    public Joke getJoke() {
        return joke;
    }

    public void setJoke(Joke joke) {
        this.joke = joke;
    }
}
