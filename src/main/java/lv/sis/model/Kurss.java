package lv.sis.model;

import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "Kurss")
@Entity
public class Kurss {
	
	@Setter(value = AccessLevel.NONE)
	@Id
	@Column(name = "Kid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int kid;
	
	@ToString.Exclude
	@OneToMany(mappedBy = "kurss")
	private Collection<Vertejumi> vertejumi;

}
