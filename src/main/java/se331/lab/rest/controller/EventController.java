package se331.lab.rest.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import se331.lab.rest.entity.Event;
import se331.lab.rest.service.EventService;

@RestController
@RequiredArgsConstructor
public class EventController {
    final EventService eventService;

    @GetMapping("events")
    public ResponseEntity<?> getEventLists(
            @RequestParam(value = "_limit", required = false) Integer pageSize,
            @RequestParam(value = "_page", required = false) Integer page) {
        HttpHeaders responseHeader = new HttpHeaders();
        Page<Event> pageOut = eventService.getEvents(pageSize, page);
        responseHeader.set("x-total-count", String.valueOf(pageOut.getTotalElements()));
        if (pageOut.getContent().isEmpty()) {
            return new ResponseEntity<>(pageOut.getContent(), responseHeader, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pageOut.getContent(), responseHeader, HttpStatus.OK);
    }

    @GetMapping("events/{id}")
    public ResponseEntity<?> getEvent(@PathVariable("id") Long id) {
        Event output = eventService.getEvent(id);
        if (output != null) {
            return ResponseEntity.ok(output);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found.");
        }
    }

    @PostMapping("/events")
    public ResponseEntity<?> addEvent(@RequestBody Event event) {
        Event output = eventService.save(event);
        return ResponseEntity.ok(output);
    }

}
