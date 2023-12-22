package org.bot.command;

import org.bot.bot.Bot;

/**
 * Команда /help
 * Отправить справку о работе бота
 */
public class HelpCommand implements BotCommand {
    @Override
    public void execute(String args, Long chatId, Bot bot) {
        bot.sendMessage(chatId,
                """
                        Вот всё что я умею:
                                        
                        😂 Показать случайный анекдот (/joke)
                            
                        😂🔢 Показать анекдот по номеру
                             (/getJoke <номер анекдота>)
                            
                        👶🏼 Справка о командах бота (/help)
                                        
                        ⭐ Оценить анекдот
                           (/rate <номер анекдота> <оценка от 1 до 5>)
                        """);
    }
}
