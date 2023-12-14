package org.util;

import org.bot.BotConfiguration;
import org.bot.TelegramJokeBot;
import org.bot.VkJokeBot;
import org.command.CommandProcessor;
import org.dao.JokeDAO;
import org.dao.JokeService;
import org.dao.JokeServiceImpl;
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
        BotConfiguration configuration = new BotConfiguration();
        CommandProcessor commandProcessor = new CommandProcessor(jokeService);
        TelegramJokeBot telegramJokeBot = new TelegramJokeBot(configuration,
                commandProcessor);
        VkJokeBot vkJokeBot = new VkJokeBot(configuration,
                commandProcessor);
    }

}