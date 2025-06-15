package lv.sis.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "MacibuRezultatiTable")
@Entity
public class MacibuRezultati {
	
	@Setter(value = AccessLevel.NONE)
	@Id
	@Column(name = "Mrid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int mrid;
	
	
	@Column(name = "MacibuRezultats")
	private String macibuRezultats;
	
	//private Kurss kid
	
	public MacibuRezultati(String macibuRezultats) {
		setMacibuRezultats(macibuRezultats);
	}

}
