package org.bot.commands;

/**
 * Команда /help
 */

public class HelpCommand implements BotCommand {
    /**
     * Получить справку о работе бота (/help)
     * @param args аргументы команды
     * @return справка
     */
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
