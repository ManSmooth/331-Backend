package se331.lab.rest.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import se331.lab.rest.entity.Organizer;

@Repository
@Profile("manual")
public class OrganizerDaoImpl implements OrganizerDao {
    List<Organizer> organizerList;

    @PostConstruct
    public void init() {
        organizerList = new ArrayList<>();
        List<LinkedHashMap<String, Object>> organizers = DBHelper.getTable("organizers");
        for (LinkedHashMap<String, Object> organizer : organizers) {
            organizerList.add(Organizer.builder()
                    .id((Long) organizer.get("id"))
                    .name((String) organizer.get("name"))
                    .address((String) organizer.get("address"))
                    .build());
        }
    }

    @Override
    public Integer getOrganizerSize() {
        return organizerList.size();
    }

    @Override
    public Page<Organizer> getOrganizers(Integer pageSize, Integer page) throws IndexOutOfBoundsException {
        pageSize = pageSize != null ? pageSize : organizerList.size();
        page = page != null ? page : 1;
        Integer initial = (page - 1) * pageSize;
        if (initial >= getOrganizerSize()) {
            throw new IndexOutOfBoundsException("Page out of bound.");
        }
        return new PageImpl<Organizer>(organizerList.subList(initial,
                initial + pageSize > getOrganizerSize() ? getOrganizerSize() : initial + pageSize),
                PageRequest.of(page, pageSize), organizerList.size());
    }

    @Override
    public Organizer getOrganizer(Long id) {
        return organizerList.stream().filter(organizer -> organizer.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Organizer save(Organizer organizer) {
        organizer.setId(organizerList.get(organizerList.size() - 1).getId() + 1);
        organizerList.add(organizer);
        return organizer;
    }
}
