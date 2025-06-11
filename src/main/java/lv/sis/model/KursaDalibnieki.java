package lv.sis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	@Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$")
	@Column(name = "TelefonaNr")
	private String telefonaNr;
	
	@NotNull
	@Pattern(regexp = "[a-zA-Z0-9_-]")
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
	@Pattern(regexp = "^[A-Za-zĀČĒĢĪĶĻŅŠŪŽāčēģīķļņšūž0-9\\s]+$\r\n+")
	@Column(name = "IelasNosaukumsNumurs")
	private String ielasNosaukumsNumurs;
	
	@Min(0)
	@Max(250)
	@Column(name = "DzivoklaNr")
	private int dzivoklaNr;
	

	@Pattern(regexp = "^[A-Z]{0,3}[-\\s]?\\d{3,6}([-\\s]?\\d{0,4})?[A-Z]{0,3}$")
	@Column(name = "pastaIndekss")
	private String pastaIndekss;
}
