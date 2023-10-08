package se331.lab.rest.dao;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import se331.lab.rest.entity.Event;
import se331.lab.rest.repository.EventRepository;

@Repository
@RequiredArgsConstructor
@Profile("db")
public class EventDaoDbImpl implements EventDao {
    final EventRepository eventRepository;

    @Override
    public Integer getEventSize() {
        return Math.toIntExact(eventRepository.count());
    }

    @Override
    public Page<Event> getEvents(Integer pageSize, Integer page) {
        return eventRepository.findAll(pageSize != null && page != null ? PageRequest.of(page - 1, pageSize)
                : PageRequest.of(0, getEventSize()));
    }

    @Override
    public Page<Event> getEvents(Integer pageSize, Integer page, String keyword) {
        return eventRepository
                .findByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContainingOrOrganizer_NameIgnoreCaseContaining(
                        keyword, keyword, keyword, pageSize != null && page != null ? PageRequest.of(page - 1, pageSize)
                                : PageRequest.of(0, getEventSize()));
    }

    @Override
    public Event getEvent(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }
}
