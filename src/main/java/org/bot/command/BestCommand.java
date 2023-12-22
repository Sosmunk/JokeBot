package org.bot.command;

import org.bot.service.RatingService;

import java.util.ArrayList;
import java.util.List;

/**
 * /best отобразить анекдоты с самым высоким рейтингом (до 10 шт)
 */
public class BestCommand implements BotCommand {
    private final RatingService ratingService;

    private final GetJokeCommand getJokeCommand;

    public BestCommand(GetJokeCommand getJokeCommand, RatingService ratingService) {
        this.getJokeCommand = getJokeCommand;
        this.ratingService = ratingService;
    }

    @Override
    public String execute(String args, Long chatId) {
        List<Integer> jokeids = ratingService.getBestJokeIds();

        List<String> jokesStrings = new ArrayList<>();
        for (int i = 0; i < jokeids.size(); i++) {
            jokesStrings.add(i+1 + ". " + getJokeCommand.execute(jokeids.get(i).toString(), chatId));
        }

        return "Лучшие анекдоты по мнению пользователей: \n\n" + String.join("\n\n", jokesStrings);
    }
}
