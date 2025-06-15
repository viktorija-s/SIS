package lv.sis.model;

import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "Kurss")
@Entity
public class Kurss {
	
	@Setter(value = AccessLevel.NONE)
	@Id
	@Column(name = "Kid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int kid;
	
	@Column(name = "Nosaukums")
	@Pattern(regexp = "[A-ZĒŪĪĻĶĢŠĀČŅ]{1}[a-zēūīļķģšāžčņ]+")
	@NotNull
	private String nosaukums;
	
	@Column(name = "Stundas")
	@NotNull
	@Min(1)
	@Max(20)
	private int stundas;
	
	@Column(name = "Limenis")
	@NotNull
	private String limenis; 
	
	@ToString.Exclude
	@OneToMany(mappedBy = "kurss")
	private Collection<Vertejumi> vertejumi;
	
	public Kurss(String nosaukums, int stundas, String limenis) {
		setNosaukums(nosaukums);
		setStundas(stundas);
		setLimenis(limenis);
	}
	
}
