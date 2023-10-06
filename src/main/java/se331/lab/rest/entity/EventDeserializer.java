package se331.lab.rest.entity;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import se331.lab.rest.dao.OrganizerDao;

@RequiredArgsConstructor
public class EventDeserializer extends JsonDeserializer<Event> {
    final OrganizerDao organizerDao;

    @Override
    public Event deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        Organizer organizer = organizerDao.getOrganizer(node.get("organizer").asLong());
        return Event.builder()
                .id(null)
                .category(node.get("category").asText())
                .title(node.get("title").asText())
                .description(node.get("description").asText())
                .location(node.get("location").asText())
                .date(node.get("date").asText())
                .time(node.get("time").asText())
                .petAllowed(Boolean.parseBoolean(node.get("time").asText()))
                .organizer(organizer)
                .build();
    }

}
