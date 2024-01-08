package org.bot;

import org.bot.bot.TelegramBot;
import org.bot.bot.VkBot;
import org.bot.command.CommandProcessor;
import org.bot.dao.JokeDAO;
import org.bot.dao.RatingDAO;
import org.bot.service.JokeService;
import org.bot.service.JokeServiceImpl;
import org.bot.service.RatingService;
import org.bot.service.RatingServiceImpl;
import org.bot.util.HibernateUtils;
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

        TelegramBot telegramJokeBot = new TelegramBot(commandProcessor);
        VkBot vkJokeBot = new VkBot(commandProcessor);

        telegramJokeBot.start();
        vkJokeBot.start();
    }

}