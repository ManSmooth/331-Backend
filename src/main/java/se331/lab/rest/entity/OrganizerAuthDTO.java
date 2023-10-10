package se331.lab.rest.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se331.lab.rest.security.user.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerAuthDTO {
    Long id;
    String name;
    @Builder.Default
    List<Role> roles = new ArrayList<>(0);
}
