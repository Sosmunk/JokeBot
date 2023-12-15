package edu;

import edu.bot.BotConfiguration;
import edu.bot.TelegramBot;
import edu.bot.VkBot;
import edu.command.CommandProcessor;
import edu.dao.JokeDAO;
import edu.dao.JokeService;
import edu.util.HibernateUtils;
import edu.dao.JokeServiceImpl;
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
        TelegramBot telegramJokeBot = new TelegramBot(configuration,
                commandProcessor);
        VkBot vkJokeBot = new VkBot(configuration,
                commandProcessor);
        telegramJokeBot.start();
        vkJokeBot.start();
    }

}