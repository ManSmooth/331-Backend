package se331.lab.rest.dao;

import java.util.List;

import se331.lab.rest.entity.Organizer;

public interface OrganizerDao {
    Integer getOrganizerSize();

    List<Organizer> getOrganizers(Integer pageSize, Integer page);

    Organizer getOrganizer(Long id);
}
