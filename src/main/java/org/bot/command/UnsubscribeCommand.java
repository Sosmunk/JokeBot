package org.bot.command;

import org.bot.DatabaseScheduler;
import org.bot.command.data.ChatData;
import org.bot.enumerable.ChatPlatform;

/**
 * /unsubscribe <br>
 * Отписаться от ежедневных анекдотов
 */
public class UnsubscribeCommand implements BotCommand {

    private final DatabaseScheduler databaseScheduler;

    public UnsubscribeCommand(DatabaseScheduler databaseScheduler) {
        this.databaseScheduler = databaseScheduler;
    }

    @Override
    public String execute(String args, Long chatId, ChatPlatform chatPlatform) {
        databaseScheduler.deschedule(new ChatData(chatPlatform, chatId));
        return "Теперь вы не будете получать ежедневные анекдоты";
    }
}
