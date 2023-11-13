package softuni.WebFinderserver.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import softuni.WebFinderserver.model.entities.categories.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "blacklist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlackList extends BaseEntity {

    @Column(nullable = false,unique = true)
    private String blockedIpAddress;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime timeOfBan;


}
