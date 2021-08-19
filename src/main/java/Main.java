import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.LinkedList;

//https://hidemyna.me/ru/proxy-list/?type=5#list The list of working proxy servers
public class Main {
    //private static final String PROXY_HOST = "8.135.28.152";
    //private static final Integer PROXY_PORT = 1080;

    public static void main(String[] args) {
        LinkedList<Message> messages = new LinkedList<>();
        Thread rss = new Thread(new RssThread(messages));
        rss.start();
        try {
            //ApiContextInitializer.init();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            //DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
            Bot myBot = new Bot(messages);
            BotSession botSession = botsApi.registerBot(myBot);
            //botOptions.setProxyHost(PROXY_HOST);
            //botOptions.setProxyPort(PROXY_PORT);
            //botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
            //Bot myBot = new Bot(botOptions, messages);
            //BotSession botSession = botsApi.registerBot(myBot);
            System.out.println("Is Runing" + botSession.isRunning());
            //botsApi.registerBot(myBot);

            // отдельный поток отслеживает изменился ли список фидов, если да, то он должен добавить звёздочку
            // поток проверяет флаг listChanged
            Thread changeCheckerThread = new Thread(() -> {
                while (true) {
                    if (RssThread.listChanged) {
                        //System.out.println("Изменился флаг isChanged => нужно добавить *");
                        myBot.changeButtons();
                        RssThread.listChanged = false;
                    }
                }
            });
            changeCheckerThread.start();
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            //e.printStackTrace();
        }
    }
}
