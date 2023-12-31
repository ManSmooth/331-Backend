package se331.lab.rest.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import se331.lab.rest.entity.Event;
import se331.lab.rest.entity.Organizer;
import se331.lab.rest.entity.Participant;
import se331.lab.rest.repository.EventRepository;
import se331.lab.rest.repository.OrganizerRepository;
import se331.lab.rest.repository.ParticipantRepository;
import se331.lab.rest.security.user.Role;
import se331.lab.rest.security.user.User;
import se331.lab.rest.security.user.UserRepository;
import se331.lab.rest.util.DBHelper;

@Component
@RequiredArgsConstructor
public class InitApp implements ApplicationListener<ApplicationReadyEvent> {
	final EventRepository eventRepository;
	final OrganizerRepository organizerRepository;
	final ParticipantRepository participantRepository;
	final UserRepository userRepository;
	final DBHelper dbHelper;

	@Override
	@Transactional
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		List<Organizer> organizers = dbHelper.getTable("organizers").stream()
				.map(organizer -> Organizer.builder()
						.id((Long) organizer.get("id"))
						.name((String) organizer.get("name"))
						.build())
				.toList();
		organizers.forEach(x -> organizerRepository.save(x));
		Random rand = new Random();
		List<Event> events = dbHelper.getTable("events").stream().map(event -> Event.builder()
				.id((Long) event.get("id"))
				.category((String) event.get("category"))
				.title((String) event.get("title"))
				.description((String) event.get("description"))
				.location((String) event.get("location"))
				.date((String) event.get("date"))
				.time((String) event.get("time"))
				.petAllowed(Boolean.parseBoolean((String) event.get("petsAllowed")))
				.organizer(organizers.get(rand.nextInt(organizers.size())))
				.build()).toList();
		events.forEach(x -> eventRepository.save(x));
		dbHelper.getTable("participants").stream()
				.map(participant -> Participant.builder()
						.id((Long) participant.get("id"))
						.name((String) participant.get("name"))
						.telNo((String) participant.get("telNo"))
						.eventHistory(pickNRandom(events, 3))
						.build())
				.forEach(x -> participantRepository.save(x));
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		List<User> users = dbHelper.getTable("users").stream()
				.map(user -> User.builder()
						.username((String) user.get("username"))
						.password(encoder.encode((String) user.get("password")))
						.firstname((String) user.get("first_name"))
						.lastname((String) user.get("last_name"))
						.email((String) user.get("email"))
						.roles(((ArrayList<Long>) user.get("role")).stream()
								.map(x -> x == 1 ? Role.ROLE_ADMIN : Role.ROLE_DISTRIBUTOR).toList())
						.build())
				.toList();
		for (int i = 0; i < organizers.size(); i++) {
			organizers.get(i).setUser(users.get(i));
			users.get(i).setOrganizer(organizers.get(i));
		}
		users.forEach(x -> userRepository.save(x));
		organizers.forEach(x -> organizerRepository.save(x));
	}

	private static <T> List<T> pickNRandom(List<T> lst, int n) {
		List<T> copy = new ArrayList<T>(lst);
		Collections.shuffle(copy);
		return n > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, n);
	}
}
