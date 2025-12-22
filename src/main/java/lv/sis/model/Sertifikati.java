package lv.sis.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
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
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate izdosanasDatums;

	@NotBlank
	@Pattern(regexp = "[A-Z]{2}[0-9]{4}")
	@Column(name = "CertificateNo", length=191, unique = true)
	private String certificateNo;

	@NotNull
	@Column(name = "IrParakstits")
	private boolean irParakstits;

	@ManyToOne
	@JoinColumn(name = "Kdid")
	private KursaDalibnieki dalibnieks;

	@ManyToOne
	@JoinColumn(name = "Kid")
	private Kurss kurss;

	public Sertifikati(CertificateType tips, LocalDate izdosanasDatums, String certificateNo, boolean irParakstits,
			KursaDalibnieki dalibnieks, Kurss kurss) {
		setTips(tips);
		setIzdosanasDatums(izdosanasDatums);
		setCertificateNo(certificateNo);
		setIrParakstits(irParakstits);
		setDalibnieks(dalibnieks);
		setKurss(kurss);
	}
}