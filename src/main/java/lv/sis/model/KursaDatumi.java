package lv.sis.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "KursaDatumi")
@Entity
public class KursaDatumi {
	@Setter(value = AccessLevel.NONE)
	@Id
	@Column(name = "KursaDatId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int kursaDatId;
	
	@Column(name = "SakumaDatums")
	@NotNull
	private LocalDate sakumaDatums;
	
	@Column(name = "BeiguDatums")
	@NotNull
	private LocalDate beiguDatums;
	
	@ManyToOne
	@JoinColumn(name = "Kid")
	private Kurss kurss;
	
	@ManyToOne
	@JoinColumn(name = "Pid")
	@ToString.Exclude
	private Pasniedzeji pasniedzejs;
	
	@OneToMany(mappedBy = "kursaDatumi", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@ToString.Exclude
	private List<Vertejumi> vertejumi;
	
	public KursaDatumi(LocalDate sakumaDatums, LocalDate beiguDatums, Kurss kurss) {
		setSakumaDatums(sakumaDatums);
		setBeiguDatums(beiguDatums);
		setKurss(kurss);
	}
	
	public KursaDatumi(LocalDate sakumaDatums, LocalDate beiguDatums, Kurss kurss, Pasniedzeji pasniedzejs) {
		setSakumaDatums(sakumaDatums);
		setBeiguDatums(beiguDatums);
		setKurss(kurss);
		setPasniedzejs(pasniedzejs);
	}
}
