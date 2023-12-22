package org.bot.bot;

import java.util.ArrayList;
import java.util.List;

public class FakeBot implements Bot {

    private final List<MessageData> messages = new ArrayList<>();

    @Override
    public void sendMessage(Long chatId, String message) {
        messages.add(new MessageData(message, chatId));
    }

    public String getLastMessageText() {
        return messages.get(messages.size() - 1).text();
    }

    public Long getLastMessageId() {
        return messages.get(messages.size() - 1).chatId();
    }
}
