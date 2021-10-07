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

public class RssThread implements Runnable {
    private LinkedList<Message> messages = new LinkedList<>();
    private LinkedList<Message> tempList;
    public static Boolean listChanged = Boolean.FALSE; //флаг изменения текущего списка фидов


    public RssThread(LinkedList<Message> messages) {
        this.messages = messages;
        this.tempList = new LinkedList<>();
    }

    @Override
    public void run() {
        try {
            while (true) {
                URL feedSource = new URL("https://lenta.ru/rss/news");

                // создает экземпляр SyndFeedInput, который будет работать с любыми типами каналов
                // распространения (версии RSS и Atom).
                SyndFeedInput input = new SyndFeedInput();

                // указывается, что SyndFeedInput читает фид синдикации из входного потока на основе символов URL-адреса,
                // указывающего на фид.
                XmlReader readerXML = new XmlReader(feedSource);
                SyndFeed feed = input.build(readerXML);
                System.out.println("Поток RssThread обновил RSS ленту с сайта lenta.ru");
                List entries = feed.getEntries();
                Iterator entriesIterator = entries.iterator();
                int j = 0;
                while (entriesIterator.hasNext() /*&& j < 10*/) {
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
                    if (messages.size() == 0 /*|| messages.size() < 10*/|| messages.size() < entries.size()) {
                        messages.add(message);
                    } else {
                        messages.remove(j - 1);
                        messages.add(j - 1, message);
                    }
                }

                if (tempList.size() == 0) {
                    tempList = new LinkedList<>(messages);
                } else if (!tempList.getFirst().getTitle().equals(messages.getFirst().getTitle())) {
                    System.out.println("Добавились новые фиды!!!!");
                    listChanged = true;
                    for (int i = messages.size() - 1; i >= 0; i--) {
                        tempList.addFirst(messages.get(i));
                        tempList.removeLast();
                    }
                }
                Thread.sleep(9000); // спать потоку обновления ленты RSS 10 минут
            }
        } catch (MalformedURLException | FeedException | InterruptedException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println(e.toString());
        }
    }
}
