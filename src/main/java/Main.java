import com.sun.syndication.feed.synd.SyndEnclosureImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Main {
    private static String PROXY_HOST = "45.76.187.188";
    private static Integer PROXY_PORT = 21345;
    public static void main(String[] args) {
        LinkedList<Message> messages = new LinkedList<>();
        try {
            URL feedSource = new URL("https://lenta.ru/rss/news ");
            // создает экземпляр SyndFeedInput, который будет работать с любыми типами каналов
            // распространения (версии RSS и Atom).
            SyndFeedInput input = new SyndFeedInput();

            // указывается, что SyndFeedInput читает фид синдикации из входного потока на основе символов URL-адреса,
            // указывающего на фид.
            SyndFeed feed = input.build(new XmlReader(feedSource));
            List entries = feed.getEntries();
            Iterator entriesIterator = entries.iterator();
            while (entriesIterator.hasNext()) {
                Message message = new Message();
                SyndEntry entrie = (SyndEntry) entriesIterator.next();
                message.setTitle(entrie.getTitle());
                message.setDescription(entrie.getDescription().getValue());
                message.setLink(entrie.getLink());
                List enclosures = entrie.getEnclosures();
                Iterator enclousersIterator = enclosures.iterator();
                while (enclousersIterator.hasNext()) {
                    SyndEnclosureImpl entrie1 = (SyndEnclosureImpl) enclousersIterator.next();
                    message.setImg(entrie1.getUrl());
                }
                messages.add(message);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FeedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     /*   for (Message msg : messages) {
            System.out.println(msg.getTitle());
            System.out.println(msg.getDescription());
            System.out.println(msg.getLink());
            System.out.println(msg.getImg());
        }*/

    }


}
