package se331.lab.rest.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import se331.lab.rest.dao.ParticipantDao;
import se331.lab.rest.entity.Participant;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    final ParticipantDao participantDao;

    @Override
    public Integer getParticipantSize() {
        return participantDao.getParticipantSize();
    }

    @Override
    public Page<Participant> getParticipants(Integer pageSize, Integer page) {
        return participantDao.getParticipants(pageSize, page);
    }

    @Override
    public Participant getParticipant(Long id) {
        return participantDao.getParticipant(id);
    }

    @Override
    public Participant save(Participant participant) {
        return participantDao.save(participant);
    }
}
