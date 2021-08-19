import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
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

    protected Bot(/*DefaultBotOptions botOptions,*/ LinkedList<Message> messages) {
        super(/*botOptions*/);
        this.messages = messages;
        setButtons(this.message, false);
    }

    /*protected Bot() {

    }*/

    @Override
    public void onUpdateReceived(Update update) {

        if (update.getMessage().getChatId() != 400738858){
          SendMessage notifyMessage = new SendMessage();
          notifyMessage.setChatId("400738858");
          //String somebodyName = update.getMessage().getFrom().getFirstName();
          String somebodyLastName = update.getMessage().getFrom().toString();
          notifyMessage.setText(somebodyLastName + " This guy use your bot!");
            try {
                execute(notifyMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        System.out.println();
        if (update.hasMessage() && update.getMessage().getText().equals("/start")
                || update.getMessage().getText().equals("From the beginning")
                || update.getMessage().getText().equals("From the beginning *")) {
            i = 0;
            if (update.getMessage().getText().equals("From the beginning *")) {
                setButtons(message, false);
            }
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(messages.get(i).getLink());

        } else if (update.hasMessage() && update.getMessage().getText().equals("Next news")) {
            i++;
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(messages.get(i).getLink());
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
        //System.out.println("listChanged " + listChanged);
        // Добавляем кнопки во вторую строчку клавиатуры
        if (!listChanged) {
            keyboardSecondRow.add(new KeyboardButton("From the beginning"));
        } else {
            keyboardSecondRow.add(new KeyboardButton("From the beginning *"));
        }
        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);

        // и устанваливаем этот список клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public void changeButtons() {
        this.setButtons(message, true);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
