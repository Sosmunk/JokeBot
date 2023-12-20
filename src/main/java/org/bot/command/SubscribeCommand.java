package org.bot.command;

import org.bot.DatabaseScheduler;
import org.bot.command.data.ChatData;
import org.bot.enumerable.ChatPlatform;

import java.time.Instant;

/**
 * /subscribe HH-mm <br>
 * Подписка на ежедневные анекдоты
 */
public class SubscribeCommand implements BotCommand {
    private final DatabaseScheduler databaseScheduler;

    public SubscribeCommand(DatabaseScheduler databaseScheduler) {
        this.databaseScheduler = databaseScheduler;
    }

    @Override
    public String execute(String args, Long chatId, ChatPlatform chatPlatform) {

        // TODO: parse dateTime to instant

        databaseScheduler.schedule(new ChatData(chatPlatform, chatId), Instant.now());

        //TODO: Scheduling
        return "ПОКА НЕ РАБОТАЕТ";
    }
}
