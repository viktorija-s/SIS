package lv.sis.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "VertejumiTable")
@Entity
public class Vertejumi {
	
	@Setter(value = AccessLevel.NONE)
	@Id
	@Column(name = "Vid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int vid;

	@Min(0)
	@Max(10)
	@Column(name = "Vertejums")
	private float vertejums;
	
	@NotNull
	@PastOrPresent
	@Column(name = "Datums")
	private Date datums;
	
	@ManyToOne
	@JoinColumn(name = "kdid")
	private KursaDalibnieki kursaDalibnieki;
	
	@ManyToOne
	@JoinColumn(name = "kursaDatId")
	private KursaDatumi kursaDatumi;
	
	public Vertejumi(float vertejums, Date datums, KursaDalibnieki kursaDalibnieki, KursaDatumi kursaDatumi) {
		setVertejums(vertejums);
		setDatums(datums);
		setKursaDalibnieki(kursaDalibnieki);
		setKursaDatumi(kursaDatumi);
	}

}
