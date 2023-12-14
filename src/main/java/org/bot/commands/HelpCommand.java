package org.bot.commands;

import org.bot.enumerable.ChatPlatform;

/**
 * Команда /help
 * Получить справку о работе бота
 */

public class HelpCommand implements BotCommand {
    @Override
    public String execute(String args, Long chatId, ChatPlatform chatPlatform) {
        return """
                Вот всё что я умею:
                                
                😂 Показать случайный анекдот (/joke)
                    
                😂🔢 Показать анекдот по номеру
                     (/getJoke <номер анекдота>)
                    
                👶🏼 Справка о командах бота (/help)
                """;
    }
}
