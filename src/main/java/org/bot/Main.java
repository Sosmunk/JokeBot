package org.bot;

import org.bot.bots.TelegramJokeBot;
import org.bot.bots.VkJokeBot;
import org.bot.commands.CommandProcessor;
import org.bot.dao.*;
import org.bot.utils.HibernateUtils;
import org.hibernate.SessionFactory;

/**
 * Класс, для запуска программы
 */

public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new HibernateUtils().createSessionFactory();
        JokeDAO jokeDAO = new JokeDAO(sessionFactory);
        RatingDAO ratingDAO = new RatingDAO(sessionFactory);
        JokeService jokeService = new JokeServiceImpl(jokeDAO);
        RatingService ratingService = new RatingServiceImpl(ratingDAO, jokeService);
        CommandProcessor commandProcessor = new CommandProcessor(jokeService, ratingService);
        TelegramJokeBot telegramJokeBot = new TelegramJokeBot(commandProcessor);
        VkJokeBot vkJokeBot = new VkJokeBot(commandProcessor);
        telegramJokeBot.start();
        vkJokeBot.start();
    }

}