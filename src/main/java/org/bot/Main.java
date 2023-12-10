package org.bot;

import org.bot.bots.TelegramJokeBot;
import org.bot.bots.VkJokeBot;
import org.bot.commands.CommandProcessor;
import org.bot.dao.JokeDAO;
import org.bot.dao.JokeService;
import org.bot.dao.JokeServiceImpl;
import org.bot.utils.HibernateUtils;
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
        TelegramJokeBot telegramJokeBot = new TelegramJokeBot(commandProcessor);
        VkJokeBot vkJokeBot = new VkJokeBot(commandProcessor);
        telegramJokeBot.start();
        vkJokeBot.start();
    }

}