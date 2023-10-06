package se331.lab.rest.dao;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import se331.lab.rest.entity.Participant;
import se331.lab.rest.repository.ParticipantRepository;

@Repository
@RequiredArgsConstructor
@Profile("db")
public class ParticipantDaoDbImpl implements ParticipantDao {
    final ParticipantRepository participantRepository;

    @Override
    public Integer getParticipantSize() {
        return Math.toIntExact(participantRepository.count());
    }

    @Override
    public Page<Participant> getParticipants(Integer pageSize, Integer page) {
        if (pageSize != null && page != null) {
            return participantRepository.findAll(PageRequest.of(page - 1, pageSize));
        }
        return participantRepository.findAll(PageRequest.of(0, getParticipantSize()));
    }

    @Override
    public Participant getParticipant(Long id) {
        return participantRepository.findById(id).orElse(null);
    }

    @Override
    public Participant save(Participant participant) {
        return participantRepository.save(participant);
    }

}
