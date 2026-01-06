package lv.sis.model;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; 
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "Pasniedzeji")
@Entity
public class Pasniedzeji {
    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pid")
    private int pid;

    @NotNull
	@Pattern(regexp = "[A-ZĒŪĪĻĶĢŠĀČŅ]{1}[a-zēūīļķģšāžčņ]+")
	@Size(min = 3, max = 20)
	@Column(name = "Vards")
	private String vards;
	
	@NotNull
	@Pattern(regexp = "[A-ZĒŪĪĻĶĢŠĀČŅ]{1}[a-zēūīļķģšāžčņ]+")
	@Size(min = 3, max = 20)
	@Column(name = "Uzvards")
	private String uzvards;
	
	@NotNull
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
	@Column(name = "Epasts")
	private String epasts;
	
	@NotNull
	@Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$")
	@Column(name = "TelefonaNr")
	private String telefonaNr; 
	
	@OneToMany(mappedBy = "pasniedzejs", cascade = CascadeType.REMOVE)
	@ToString.Exclude
	private List<KursaDatumi> kursaDatumi;
	
	@OneToOne
	@JoinColumn(name = "UId")
	private MyUser user;

    public Pasniedzeji(String vards, String uzvards, String epasts, String telefonaNr) {
    	setVards(vards);
    	setUzvards(uzvards);
        setEpasts(epasts);
        setTelefonaNr(telefonaNr);
    }
}
