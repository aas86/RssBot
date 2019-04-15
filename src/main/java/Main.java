import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotSession;

import java.util.LinkedList;

//https://hidemyna.me/ru/proxy-list/?type=5#list The list of working proxy servers
public class Main {
    private static String PROXY_HOST = "45.76.187.188";
    private static Integer PROXY_PORT = 21345;

    public static void main(String[] args) {
        LinkedList<Message> messages = new LinkedList<>();
        Thread rss = new Thread(new RssThread(messages));
        rss.start();
        try {
            ApiContextInitializer.init();
            TelegramBotsApi botsApi = new TelegramBotsApi();
            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
            botOptions.setProxyHost(PROXY_HOST);
            botOptions.setProxyPort(PROXY_PORT);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
            Bot myBot = new Bot(botOptions, messages);
            botsApi.registerBot(myBot);

            // отдельный поток отслеживает изменился ли список фидов, если да, то он должен добавить звёздочку
            Thread b = new Thread(() -> {
                while (true) {
                    if (RssThread.listChanged) {
                        System.out.println("Изменился флаг isChanged => нужно добавить *");
                        myBot.changeButtons();
                        RssThread.listChanged = false;
                    }
                }
            });
            b.start();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
