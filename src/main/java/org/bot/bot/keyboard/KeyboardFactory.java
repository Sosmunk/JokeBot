package org.bot.bot.keyboard;

import api.longpoll.bots.model.objects.additional.Keyboard;
import api.longpoll.bots.model.objects.additional.buttons.Button;
import api.longpoll.bots.model.objects.additional.buttons.TextButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Клавиатура
 */
public class KeyboardFactory {
    /**
     * Клавиатура для вк бота
     * @return Клавиатура
     */
    public Keyboard createVKRateKeyboard() {

        List<List<Button>> buttons = new ArrayList<>();
        List<Button> buttonRow = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            buttonRow.add(
                    new TextButton(
                            Button.Color.POSITIVE,
                            new TextButton.Action(String.format("%s☆", i))));
        }
        buttons.add(buttonRow);

        Keyboard keyboard = new Keyboard(buttons);
        keyboard.setOneTime(true);
        return keyboard;
    }

    /**
     * Клавиатура для тг бота
     * @return клавиатура
     */
    public ReplyKeyboardMarkup createTgRateKeyboard(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        for (int i = 1; i < 6; i++) {
            row.add("\n" + i + "☆");
        }
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        return keyboardMarkup;
    }
}
