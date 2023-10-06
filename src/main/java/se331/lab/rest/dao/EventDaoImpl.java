package se331.lab.rest.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import se331.lab.rest.entity.Event;

@Repository
@Profile("manual")
public class EventDaoImpl implements EventDao {
    List<Event> eventList;

    @PostConstruct
    public void init() {
        eventList = new ArrayList<>();
        List<LinkedHashMap<String, Object>> events = DBHelper.getTable("events");
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
    public Page<Event> getEvents(Integer pageSize, Integer page) throws IndexOutOfBoundsException {
        pageSize = pageSize != null ? pageSize : eventList.size();
        page = page != null ? page : 1;
        Integer initial = (page - 1) * pageSize;
        if (initial >= getEventSize()) {
            throw new IndexOutOfBoundsException("Page out of bound.");
        }
        return new PageImpl<Event>(
                eventList.subList(initial, initial + pageSize > getEventSize() ? getEventSize() : initial + pageSize),
                PageRequest.of(page, pageSize), eventList.size());
    }

    @Override
    public Event getEvent(Long id) {
        return eventList.stream().filter(event -> event.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Event save(Event event) {
        event.setId(eventList.get(eventList.size() - 1).getId() + 1);
        eventList.add(event);
        return event;
    }

}