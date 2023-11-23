package org.bot;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, отвечающий за обработку клавиатуры
 */
public class KeyboardHandler {

    /**
     * Метод, устанавливающий клавиатуру для TG бота
     * @param chatId id чата
     * @param message сообщение
     * @return SendMessage
     */
    public SendMessage setKeyboardTG(String chatId, String message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add("/start");
        row.add("/help");
        row.add("/joke");
        row.add("/getJoke");
        row.add("/rate");

        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }

    /**
     * Метод, устанавливающий клавиатуру для Vk бота
     *
     * @param chatId  id чата
     * @param message сообщение пользователя
     */
    public Keyboard setKeyboardVK(String chatId, String message){
        // TODO : Сделать клавиатуру для VK бота
        Keyboard keyboard = new Keyboard();
        List<List<KeyboardButton>> list = new ArrayList<>();
        List<KeyboardButton> row = new ArrayList<>();
        row.add(new KeyboardButton());
        list.add(row);
        keyboard.setButtons(list);
        return keyboard;
    }
}
