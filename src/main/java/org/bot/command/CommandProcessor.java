package org.bot.command;

import org.bot.DatabaseScheduler;
import org.bot.bot.TelegramBot;
import org.bot.bot.VkBot;
import org.bot.command.data.CommandData;
import org.bot.dao.RatingService;
import org.bot.enumerable.ChatPlatform;
import org.bot.service.JokeService;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для обработки команд из сообщений бота
 */
public class CommandProcessor {

    /**
     * Хранилище ключ-значение для ассоциаций:
     * текст-команды : команда на выполнение
     */
    private final Map<String, BotCommand> commandMap;
    private final CommandParser commandParser;

    public CommandProcessor(JokeService jokeService, RatingService ratingService) {
        this.commandParser = new CommandParser();
        commandMap = new HashMap<>();
        RateLastCommand rateLastCommand = new RateLastCommand(jokeService, ratingService);
        commandMap.put("/start", new StartCommand());
        commandMap.put("/help", new HelpCommand());
        commandMap.put("/joke", new JokeCommand(jokeService));
        commandMap.put("/getJoke", new GetJokeCommand(jokeService));
        commandMap.put("/rate", new RateCommand(ratingService));
        commandMap.put("1☆", rateLastCommand);
        commandMap.put("2☆", rateLastCommand);
        commandMap.put("3☆", rateLastCommand);
        commandMap.put("4☆", rateLastCommand);
        commandMap.put("5☆", rateLastCommand);

    }

    /**
     * Выполнить пользовательскую команду
     *
     * @param command      команда из сообщения
     * @param chatId       id чата
     * @param chatPlatform чат платформа (Telegram/VK)
     * @return сообщение пользователю
     */
    public String runCommand(String command, Long chatId, ChatPlatform chatPlatform) {
        CommandData commandData = commandParser.parseMessage(command);
        BotCommand botCommand = commandMap.get(commandData.command());

        if (botCommand == null) {
            return "Команда не найдена";
        }
        if (botCommand.getClass() == RateLastCommand.class) {
            return botCommand.execute(commandData.command().substring(0, 1), chatId, chatPlatform);
        } else {
            return botCommand.execute(commandData.args(), chatId, chatPlatform);
        }
    }

    public void enableSchedulingForBots(TelegramBot telegramBot, VkBot vkBot) {

        DatabaseScheduler databaseScheduler = new DatabaseScheduler(telegramBot, vkBot, (JokeCommand) commandMap.get("/joke"));
        commandMap.put("/subscribe",
                new SubscribeCommand(databaseScheduler));
        commandMap.put("/unsubscribe", new UnsubscribeCommand(databaseScheduler));
    }
}
