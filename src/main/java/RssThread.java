import com.sun.syndication.feed.synd.SyndEnclosureImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 437-5 on 11.04.2019.
 */
public class RssThread implements Runnable {
    private LinkedList<Message> messages = new LinkedList<>();

    public RssThread(LinkedList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void run() {
        try {
            while (true) {
                URL feedSource = new URL("https://lenta.ru/rss/news ");

                // создает экземпляр SyndFeedInput, который будет работать с любыми типами каналов
                // распространения (версии RSS и Atom).
                SyndFeedInput input = new SyndFeedInput();

                // указывается, что SyndFeedInput читает фид синдикации из входного потока на основе символов URL-адреса,
                // указывающего на фид.
                SyndFeed feed = input.build(new XmlReader(feedSource));
                List entries = feed.getEntries();
                Iterator entriesIterator = entries.iterator();
                int j = 0;
                while (entriesIterator.hasNext() && j < 10) {
                    j++;
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
                    if (messages.size() == 0 || messages.size() < 10) {
                        messages.add(message);
                    } else {
                        messages.remove(j - 1);
                        messages.add(j - 1, message);
                    }
                }
                Thread.sleep(15000); // спать потоку обновления ленты RSS 1,5 минуты
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FeedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
