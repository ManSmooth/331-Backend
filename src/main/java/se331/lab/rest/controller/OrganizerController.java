package se331.lab.rest.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import se331.lab.rest.entity.Organizer;
import se331.lab.rest.service.OrganizerService;

@Controller
@RequiredArgsConstructor
public class OrganizerController {
    final OrganizerService organizerService;

    @GetMapping("organizers")
    public ResponseEntity<?> getOrganizerLists(
            @RequestParam(value = "_limit", required = false) Integer pageSize,
            @RequestParam(value = "_page", required = false) Integer page) {
        Integer organizerSize = organizerService.getOrganizerSize();
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set("x-total-count", String.valueOf(organizerSize));
        try {
            return ResponseEntity.status(HttpStatus.OK).headers(responseHeader)
                    .body(organizerService.getOrganizers(pageSize, page));
        } catch (IndexOutOfBoundsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("organizers/{id}")
    public ResponseEntity<?> getOrganizer(@PathVariable("id") Long id) {
        Organizer output = organizerService.getOrganizer(id);
        if (output != null) {
            return ResponseEntity.ok(output);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Organizer not found.");
        }
    }
}
