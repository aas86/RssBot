import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.LinkedList;


public class BotThread implements Runnable {
    private static String PROXY_HOST = "45.76.187.188";
    private static Integer PROXY_PORT = 21345;
    private LinkedList<Message> messages = new LinkedList<>();


    public BotThread(LinkedList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void run() {
        try {
            ApiContextInitializer.init();
            TelegramBotsApi botsApi = new TelegramBotsApi();
            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
            botOptions.setProxyHost(PROXY_HOST);
            botOptions.setProxyPort(PROXY_PORT);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
            Bot myBot = new Bot(botOptions, messages);
            botsApi.registerBot(myBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
