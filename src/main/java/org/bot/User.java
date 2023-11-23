package org.bot;

/**
 * Класс пользователя
 * TODO : Проверить класс пользователя
 */
public class User {
    private final Long chatId;

    public User(Long chatId){
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }
}
