package org.bot;

/**
 * Конфигуратор для бота ТГ
 */
public class BotConfiguration {
    private final String tgToken = System.getenv("TG_TOKEN");
    private final String vkToken = System.getenv("VK_TOKEN");
    public String getTgToken() {
        return tgToken;
    }

    public String getVkToken() {
        return vkToken;
    }
}
