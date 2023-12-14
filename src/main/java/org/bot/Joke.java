package org.bot;

import jakarta.persistence.*;

import java.util.List;
import java.util.OptionalDouble;

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
     * Рейтинги анекдота
     */
    @OneToMany(mappedBy = "joke", fetch = FetchType.EAGER)
    private List<Rate> ratings;

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

    public List<Rate> getRatings() {
        return this.ratings;
    }

    public void setRatings(List<Rate> ratings) {
        this.ratings = ratings;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * Получить средний рейтинг анекдота
     *
     * @return строка со средним рейтингом
     */
    public String getAverageRatingString() {
        List<Rate> jokeRatings = this.getRatings();

        if (jokeRatings == null) {
            return "";
        }

        OptionalDouble optionalDouble = jokeRatings.stream()
                .mapToDouble(Rate::getStars)
                .average();

        return optionalDouble.isPresent()
                ? "\n Рейтинг анекдота: " + optionalDouble.getAsDouble()
                : "";
    }
}
