package lv.sis.model;

import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
	
	@Min(0)
	@Max(10)
	@Column(name = "MacibuRezultats")
	private int macibuRezultats;
	
	//private Kurss kid

}
