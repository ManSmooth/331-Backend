package se331.lab.rest.config;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import se331.lab.rest.dao.DBHelper;
import se331.lab.rest.entity.Event;
import se331.lab.rest.entity.Organizer;
import se331.lab.rest.repository.EventRepository;
import se331.lab.rest.repository.OrganizerRepository;

@Component
@RequiredArgsConstructor
public class InitApp implements ApplicationListener<ApplicationReadyEvent> {
    final EventRepository eventRepository;
    final OrganizerRepository organizerRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        List<LinkedHashMap<String, Object>> events = DBHelper.getTable("events");
        for (LinkedHashMap<String, Object> event : events) {
            eventRepository.save(Event.builder()
                    .id((Long) event.get("id"))
                    .category((String) event.get("category"))
                    .title((String) event.get("title"))
                    .description((String) event.get("description"))
                    .location((String) event.get("location"))
                    .date((String) event.get("date"))
                    .time((String) event.get("time"))
                    .petAllowed(Boolean.parseBoolean((String) event.get("petsAllowed")))
                    .organizer((String) event.get("organizer"))
                    .build());
        }
        List<LinkedHashMap<String, Object>> organizers = DBHelper.getTable("organizers");
        for (LinkedHashMap<String, Object> organizer : organizers) {
            organizerRepository.save(Organizer.builder()
                    .id((Long) organizer.get("id"))
                    .name((String) organizer.get("name"))
                    .address((String) organizer.get("address"))
                    .build());
        }
    }
}
