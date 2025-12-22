package lv.sis.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "MyAuthorityTable")
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class MyAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "AId")
    @Setter(value = AccessLevel.NONE)
    private int aId;

    @NotNull
    @Size(min = 3, max = 10)
    @Pattern(regexp = "[A-Z_]+")
    @Column(name = "Title")
    private String title;

    // one to many

    @OneToMany(mappedBy = "authority", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<MyUser> users;

    public MyAuthority(String title) {
        this.title = title;
    }



}
