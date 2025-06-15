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
	@Column(name = "KursaDatId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int kursaDatId;
	
	@Column(name = "SakumaDatums")
	@NotNull
	private Date sakumaDatums;
	
	@Column(name = "BeiguDatums")
	@NotNull
	private Date beiguDatums;
	
	// TODO saite ar kursu
	// TODO saite ar pasniedzeju
	
	@OneToMany(mappedBy = "kursaDatumi")
	@ToString.Exclude
	private Collection<Vertejumi> vertejumi;
	
	public KursaDatumi(Date sakumaDatums, Date beiguDatums) {
		setSakumaDatums(sakumaDatums);
		setBeiguDatums(beiguDatums);
	}
}
