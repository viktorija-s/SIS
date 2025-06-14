package lv.sis.model;

import java.sql.Date;
import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "Sertifikati")
@Entity
public class KursaDatumi {
	@Setter(value = AccessLevel.NONE)
	@Id
	@Column(name = "kursaDatId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int kursaDatId;
	
	@Column(name = "sakumaDatums")
	@NotNull
	private Date sakumaDatums;
	
	@Column(name = "beiguDatums")
	@NotNull
	private Date beiguDatums;
	
	@ToString.Exclude
	@OneToMany(mappedBy = "kursaDatumi")
	private Collection<Vertejumi> vertejumi;
	
	// TODO saite ar kursu
	// TODO saite ar pasniedzeju
	
	public KursaDatumi(Date sakumaDatums, Date beiguDatums) {
		setSakumaDatums(sakumaDatums);
		setBeiguDatums(beiguDatums);
	}
}
