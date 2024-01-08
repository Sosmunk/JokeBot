package org.bot.command.data;

import java.io.Serializable;

public record ChatData(String chatPlatform, Long chatId) implements Serializable {
}
