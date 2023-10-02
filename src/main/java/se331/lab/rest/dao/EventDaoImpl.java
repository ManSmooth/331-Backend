package se331.lab.rest.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import se331.lab.rest.entity.Event;

@Repository
public class EventDaoImpl implements EventDao {
    List<Event> eventList;
    JsonParser jsonParser = new BasicJsonParser();
    File dbFile;

    @PostConstruct
    public void init() {
        eventList = new ArrayList<>();
        try {
            dbFile = ResourceUtils.getFile("classpath:db.min.json");
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
        byte[] bytes = new byte[(int) dbFile.length()];
        try (FileInputStream fis = new FileInputStream(dbFile)) {
            fis.read(bytes);
        } catch (IOException e) {
            System.err.println(e);
        }
        Map<String, Object> db = jsonParser
                .parseMap(new String(bytes, StandardCharsets.UTF_8));

        // :smiley:
        @SuppressWarnings("unchecked")
        ArrayList<LinkedHashMap<String, Object>> events = (ArrayList<LinkedHashMap<String, Object>>) db.get("events");
        for (LinkedHashMap<String, Object> event : events) {
            eventList.add(Event.builder()
                    .id((Long) event.get("id"))
                    .category((String) event.get("category"))
                    .title((String) event.get("title"))
                    .description((String) event.get("description"))
                    .location((String) event.get("location"))
                    .date((String) event.get("date"))
                    .time((String) event.get("time"))
                    .petAllowed((Boolean) event.get("petAllowed"))
                    .organizer((String) event.get("organizer"))
                    .build());
        }
    }

    @Override
    public Integer getEventSize() {
        return eventList.size();
    }

    @Override
    public List<Event> getEvents(Integer pageSize, Integer page) {
        pageSize = pageSize != null ? pageSize : eventList.size();
        page = page != null ? page : 1;
        Integer initial = (page - 1) * pageSize;
        return eventList.subList(initial, initial + pageSize > getEventSize() ? initial + pageSize : getEventSize());
    }

    @Override
    public Event getEvent(Long id) {
        Optional<Event> matchingEvent = eventList.stream().filter(event -> event.getId().equals(id)).findFirst();
        return matchingEvent.isPresent() ? matchingEvent.get() : null;
    }
}
