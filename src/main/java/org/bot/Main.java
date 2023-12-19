package org.bot;

import org.bot.bot.TelegramBot;
import org.bot.bot.VkBot;
import org.bot.command.CommandProcessor;
import org.bot.dao.JokeDAO;
import org.bot.service.JokeService;
import org.bot.service.JokeServiceImpl;
import org.bot.util.HibernateUtils;
import org.hibernate.SessionFactory;

/**
 * Класс, для запуска программы
 */

public class Main {
    public static void main(String[] args) {
        // Данное sessionFactory в будущем будет использоваться для других DAO
        SessionFactory sessionFactory = new HibernateUtils().createSessionFactory();
        // Инициализируем DAO здесь, потому что передается sessionFactory
        JokeDAO jokeDAO = new JokeDAO(sessionFactory);
        JokeService jokeService = new JokeServiceImpl(jokeDAO);
        // В будущем сюда будут передаваться другие сервисы
        CommandProcessor commandProcessor = new CommandProcessor(jokeService);
        TelegramBot telegramJokeBot = new TelegramBot(commandProcessor);
        VkBot vkJokeBot = new VkBot(commandProcessor);
        telegramJokeBot.start();
        vkJokeBot.start();
    }

}