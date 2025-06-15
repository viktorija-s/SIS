package lv.sis.model;


import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	@Column(name = "telnummurs")
	private String telnummurs; 
	
	@OneToMany(mappedBy = "pasniedzejs")
	private Collection<KursaDatumi> kursaDatumi;

    public Pasniedzeji(String vards, String uzvards, String epasts, String telnummurs) {
    	setVards(vards);
    	setUzvards(uzvards);
        setEpasts(epasts);
        setTelnummurs(telnummurs); 
        
    }



}// skatiities sheemaa mainiigos 
// uztaisiit visu kaa ir sertifikātos bet ar pasniedzējiem.. 
// tas pats ar kursss.. 

