package se331.lab.rest.service;

import org.springframework.data.domain.Page;

import se331.lab.rest.entity.Event;

public interface EventService {
    Integer getEventSize();

    Page<Event> getEvents(Integer pageSize, Integer page);

    Page<Event> getEvents(Integer pageSize, Integer page, String keyword);

    Event getEvent(Long id);

    Event save(Event event);
}
