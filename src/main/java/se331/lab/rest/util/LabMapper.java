package se331.lab.rest.util;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import se331.lab.rest.entity.Event;
import se331.lab.rest.entity.EventDTO;
import se331.lab.rest.entity.Organizer;
import se331.lab.rest.entity.OrganizerAuthDTO;
import se331.lab.rest.entity.OrganizerDTO;
import se331.lab.rest.entity.Participant;
import se331.lab.rest.entity.ParticipantDTO;

@Mapper
public interface LabMapper {
    LabMapper INSTANCE = Mappers.getMapper(LabMapper.class);

    EventDTO getEventDto(Event event);

    List<EventDTO> getEventDto(List<Event> events);

    OrganizerDTO getOrganizerDTO(Organizer organizer);

    List<OrganizerDTO> getOrganizerDTO(List<Organizer> organizers);

    ParticipantDTO getParticipantDTO(Participant participant);

    List<ParticipantDTO> getParticipantDTO(List<Participant> participants);

    @Mapping(target = "roles", source = "user.roles")
    OrganizerAuthDTO getOrganizerAuthDTO(Organizer organizer);
}
