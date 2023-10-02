package se331.lab.rest.controller;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import se331.lab.rest.entity.Event;

@Controller
public class EventController {
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

    @GetMapping("events")
    public ResponseEntity<?> getEventLists(
            @RequestParam(value = "_limit", required = false) Integer perPage,
            @RequestParam(value = "_page", required = false) Integer page) {
        HttpHeaders respondHeaders = new HttpHeaders();
        respondHeaders.set("x-total-count", String.valueOf(eventList.size()));
        perPage = perPage != null ? perPage : eventList.size();
        page = page != null ? page : 1;
        Integer initial = (page - 1) * perPage;
        if (initial >= eventList.size()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("<h1>400 BAD_REQUEST</h1><hr><p>Page out of range.</p>");
        } else if (initial + perPage > eventList.size()) {
            return ResponseEntity.status(HttpStatus.OK).headers(respondHeaders).body(eventList.subList(initial, eventList.size()));
        }
        return ResponseEntity.status(HttpStatus.OK).headers(respondHeaders).body(eventList.subList(initial, initial + perPage));
    }

    @GetMapping("events/{id}")
    public ResponseEntity<?> getEvent(@PathVariable("id") Long id) {
        Optional<Event> matchingEvent = eventList.stream().filter(event -> event.getId().equals(id)).findFirst();
        return matchingEvent.isPresent() ? ResponseEntity.ok(matchingEvent.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("<h1>404 NOT_FOUND</h1><hr><p>Event not found.</p>");
    }

}
