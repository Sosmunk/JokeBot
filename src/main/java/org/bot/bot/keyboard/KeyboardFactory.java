package org.bot.bot.keyboard;

import api.longpoll.bots.model.objects.additional.Keyboard;
import api.longpoll.bots.model.objects.additional.buttons.Button;
import api.longpoll.bots.model.objects.additional.buttons.TextButton;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {

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
}
