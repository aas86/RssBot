import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
    private Boolean listChanged = false;
    private int i = 0;
    private SendMessage message = new SendMessage();

    protected Bot(DefaultBotOptions botOptions, LinkedList<Message> messages) {
        super(botOptions);
        this.messages = messages;
       // setButtons(this.message, false);
    }


    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = new SendMessage();

        setButtons(message, listChanged);

        if (update.hasMessage() && update.getMessage().getText().equals("/start")
                || update.getMessage().getText().equals("From the beginning")
                || update.getMessage().getText().equals("From the beginning *")) {
            i = 0;
            message.setChatId(update.getMessage().getChatId());
            message.setText(/*tempList*/messages.get(i).getLink());

        } else if (update.hasMessage() && update.getMessage().getText().equals("Next news")) {
            i++;
            message.setChatId(update.getMessage().getChatId());
            message.setText(/*tempList*/messages.get(i).getLink());
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

    public Boolean getListChanged() {
        return listChanged;
    }

    public void setListChanged(Boolean listChanged) {
        this.listChanged = listChanged;
    }

    private synchronized void setButtons(SendMessage sendMessage, Boolean listChanged) {
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

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        System.out.println("listChanged " + listChanged);
        // Добавляем кнопки во вторую строчку клавиатуры
        if (!listChanged) {
            keyboardSecondRow.add(new KeyboardButton("From the beginning"));
        } else{
            keyboardSecondRow.add(new KeyboardButton("From the beginning *"));
        }
        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);

        // и устанваливаем этот список клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
       /* try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }*/
    }

    public void changeButtons() {
        System.out.println("Метод добавления *!");
        this.setButtons(message, true);
    }

}
