import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 437-5 on 10.04.2019.
 */
public class Bot extends TelegramLongPollingBot {
    private LinkedList<Message> messages;

    protected Bot(DefaultBotOptions botOptions, LinkedList<Message> messages) {
        super(botOptions);
        this.messages = messages;
    }

    private int i = 0;

    @Override
    public void onUpdateReceived(Update update) {

        SendMessage message = new SendMessage();
        setButtons(message);
        //SendPhoto photo = new SendPhoto();
        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            i = 0;
            message.setChatId(update.getMessage().getChatId());
            message.setText(messages.get(i).getLink());



        } else if (update.hasMessage() && update.getMessage().getText().equals("Next news")) {
            i++;
            message.setChatId(update.getMessage().getChatId());
            message.setText(messages.get(i).getLink());
           /* photo.setChatId(update.getMessage().getChatId());
            photo.setPhoto(messages.get(i).getImg());*/
        }
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "Duxa_bot";
    }

    @Override
    public String getBotToken() {
        return "672951422:AAFGUpS0k3MBJAgSdBRwPqBa7p_0qbRZojc";
    }

    private synchronized void setButtons(SendMessage sendMessage) {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton("Next news"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);

        // и устанваливаем этот список клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);

    }
}
