import java.util.LinkedList;

//https://hidemyna.me/ru/proxy-list/?type=5#list The list of working proxy servers
public class Main {
    public static Boolean listChanged;
    public static void main(String[] args) {
        //TODO засинхронизировать доступ к messages и запилить звёздочку на кнопке, если появились новые сообщения.
        LinkedList<Message> messages = new LinkedList<>();
        Thread rss = new Thread(new RssThread(messages));
        rss.start();
        Thread bot = new Thread(new BotThread(messages));
        bot.start();

    }
}
