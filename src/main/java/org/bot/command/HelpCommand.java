package org.bot.command;

/**
 * Команда /help
 * Получить справку о работе бота
 */

public class HelpCommand implements BotCommand {
    @Override
    public String execute(String args) {
        return """
                Вот всё что я умею:
                                
                😂 Показать случайный анекдот (/joke)
                    
                😂🔢 Показать анекдот по номеру
                     (/getJoke <номер анекдота>)
                    
                👶🏼 Справка о командах бота (/help)
                """;
    }
}
