package se331.lab.rest.service;

import org.springframework.data.domain.Page;

import se331.lab.rest.entity.Participant;

public interface ParticipantService {
    Integer getParticipantSize();

    Page<Participant> getParticipants(Integer pageSize, Integer page);

    Participant getParticipant(Long id);

    Participant save(Participant event);
}
