package softuni.WebFinderserver.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.WebFinderserver.domain.entities.categories.BaseEntity;
import softuni.WebFinderserver.domain.enums.RoleEnum;

import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User extends BaseEntity {

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Column
    private String username;
    @Column
    private String email;
    @Column
    private String password;
    @ManyToMany
    private List<Role> roles;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private List<Like> likes;


}
