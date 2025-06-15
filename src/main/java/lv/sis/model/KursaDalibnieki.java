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
@Table(name = "KursaDalibniekiTable")
@Entity
public class KursaDalibnieki {
	
	@Setter(value = AccessLevel.NONE)
	@Id
	@Column(name = "Kdid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int kdid;
	
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
	/// @Pattern(regexp = "^(\\\\+371)?\\\\d{8}$")
	@Column(name = "TelefonaNr")
	private String telefonaNr;
	
	@NotNull
	@Pattern(regexp = "[a-zA-Z0-9_-]+")
	@Column(name = "PersonasId")
	private String personasId;
	
	@NotNull
	@Pattern(regexp = "[A-ZĒŪĪĻĶĢŠĀČŅ]{1}[a-zēūīļķģšāžčņ]+")
	@Size(min = 3, max = 20)
	@Column(name = "Pilseta")
	private String pilseta;
	
	@NotNull
	@Pattern(regexp = "[A-ZĒŪĪĻĶĢŠĀČŅ]{1}[a-zēūīļķģšāžčņ]+")
	@Size(min = 3, max = 20)
	@Column(name = "Valsts")
	private String valsts;
	
	@NotNull
	// @Pattern(regexp = "^[A-Za-zĀČĒĢĪĶĻŅŠŪŽāčēģīķļņšūž0-9\\\\s,./-]+$")
	@Column(name = "IelasNosaukumsNumurs")
	private String ielasNosaukumsNumurs;
	
	@Min(0)
	@Max(250)
	@Column(name = "DzivoklaNr")
	private int dzivoklaNr;
	

	@Pattern(regexp = "^[A-Z]{0,3}[-\\s]?\\d{3,6}([-\\s]?\\d{0,4})?[A-Z]{0,3}$")
	@Column(name = "PastaIndekss")
	private String pastaIndekss;
	

	@OneToMany(mappedBy = "dalibnieks")
	private Collection<Sertifikati> sertifikati;

	
	@ToString.Exclude
	@OneToMany(mappedBy = "kursaDalibnieki")
	private Collection<Vertejumi> vertejumi;
	
	public KursaDalibnieki(String vards, String uzvards, String epasts, String telefonaNr, String personasId, String pilseta, String valsts, String ielasNosaukumsNumurs, int dzivoklaNr, String pastaIndekss) {
		setVards(vards);
		setUzvards(uzvards);
		setEpasts(epasts);
		setTelefonaNr(telefonaNr);
		setPersonasId(personasId);
		setPilseta(pilseta);
		setValsts(valsts);
		setIelasNosaukumsNumurs(ielasNosaukumsNumurs);
		setDzivoklaNr(dzivoklaNr);
		setPastaIndekss(pastaIndekss);
	}
	
	
}
