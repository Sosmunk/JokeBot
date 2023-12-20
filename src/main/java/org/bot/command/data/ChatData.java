package org.bot.command.data;

import org.bot.enumerable.ChatPlatform;

import java.io.Serializable;

public record ChatData(ChatPlatform chatPlatform, Long chatId) implements Serializable {
}
