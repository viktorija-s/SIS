package lv.sis.model;

import java.awt.Window.Type;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "Sertifikati")
@Entity
public class Sertifikati {
	@Setter(value = AccessLevel.NONE)
	@Id
	@Column(name = "sid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int sid;
	
	@NotNull
	@Column(name = "tips")
	private CertificateType tips; 
	
	@NotNull
	@Column(name = "izdosanasDatums")
	@PastOrPresent // datums nevar būt nākotnē
	private Date izdosanasDatums;
	
	@NotNull
	@Column(name = "registracijasNumurs")
	@Min(1) // nevar būt negatīvs vai nulle
	private int registracijasNr;
	
	@NotNull
	@Column(name = "irParakstits")
	private boolean irParakstits;
}
