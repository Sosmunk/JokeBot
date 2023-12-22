package org.bot.command;

import org.bot.JokeScheduler;
import org.bot.bot.Bot;

import java.time.Instant;

/**
 * /subscribe HH-mm <br>
 * Подписка на ежедневные анекдоты
 */
public class SubscribeCommand implements BotCommand {

    private final JokeScheduler jokeScheduler;

    public SubscribeCommand(JokeScheduler jokeScheduler) {
        this.jokeScheduler = jokeScheduler;
    }

    @Override
    public void execute(String args, Long chatId, Bot bot) {
        //TODO: парсинг и отправка в нужное время
        jokeScheduler.schedule(bot.getChatPlatform(), chatId, Instant.now());


        bot.sendMessage(chatId, "TODO:");
    }
}
