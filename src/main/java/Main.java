import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        LinkedList<Message> messages = new LinkedList<>();
        Thread rss = new Thread(new RssThread(messages));
        rss.start();
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            Bot myBot = new Bot(messages);
            BotSession botSession = botsApi.registerBot(myBot);
            System.out.println("Is Runing " + botSession.isRunning());

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
