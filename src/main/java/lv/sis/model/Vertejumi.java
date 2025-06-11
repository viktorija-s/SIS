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
	
	
	//private KursaDatumi kursaDatumi;
	
	private KursaDalibnieki kursaDalibnieki;
	
	@Min(0)
	@Max(10)
	@Column(name = "Vertejums")
	private float vertejums;
	

	@NotNull
	@Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])[-.](0[1-9]|1[0-2])[-.](19|20)\\d{2}$/gm")
	@Column(name = "PhoneNo")
	private String datums;
	
	public Vertejumi(float vertejumi, String datums) {
		setVertejums(vertejumi);
		setDatums(datums);
	}

}
