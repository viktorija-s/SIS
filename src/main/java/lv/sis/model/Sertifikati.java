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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lv.sis.model.enums.CertificateType;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "Sertifikati")
@Entity
public class Sertifikati {
	@Setter(value = AccessLevel.NONE)
	@Id
	@Column(name = "Sid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int sid;
	
	@NotNull
	@Column(name = "Tips")
	private CertificateType tips; 
	// private Limeni limenis 
	
	@NotNull
	@Column(name = "IzdosanasDatums")
	@PastOrPresent // datums nevar būt nākotnē
	private Date izdosanasDatums;
	
	@NotNull
	@Column(name = "RegistracijasNumurs")
	@Min(1) 
	private int registracijasNr;
	
	@NotNull
	@Column(name = "IrParakstits")
	private boolean irParakstits;
	
	@ManyToOne
	@JoinColumn(name = "Kdid")
	private KursaDalibnieki dalibnieks;
	
	@ManyToOne
	@JoinColumn(name = "Kid")
	private Kurss kurss;
	
	public Sertifikati(CertificateType tips, Date izdosanasDatums, int registracijasNr, boolean irParakstits, KursaDalibnieki dalibnieks) {
		setTips(tips);
		setIzdosanasDatums(izdosanasDatums);
		setRegistracijasNr(registracijasNr);
		setIrParakstits(irParakstits);
		setDalibnieks(dalibnieks);
	}
}
