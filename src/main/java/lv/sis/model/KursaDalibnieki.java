package lv.sis.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "KursaDalibniekiTable")
@Entity
public class KursaDalibnieki {

    @Setter(value = AccessLevel.NONE)
    @Id
    @Column(name = "Kdid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int kdid;

    @NotNull(message = "Vārds ir obligāts")
    @Pattern(
            regexp = "[A-ZĒŪĪĻĶĢŠĀČŅ]{1}[a-zēūīļķģšāžčņ]+",
            message = "Vārdam jāsākas ar lielo burtu un jāsatur tikai burti"
    )
    @Size(min = 3, max = 20, message = "Vārda garumam jābūt no 3 līdz 20")
    @Column(name = "Vards")
    private String vards;

    @NotNull(message = "Uzvārds ir obligāts")
    @Pattern(
            regexp = "[A-ZĒŪĪĻĶĢŠĀČŅ]{1}[a-zēūīļķģšāžčņ]+",
            message = "Uzvārdam jāsākas ar lielo burtu un jāsatur tikai burti"
    )
    @Size(min = 3, max = 20, message = "Uzvārda garumam jābūt no 3 līdz 20")
    @Column(name = "Uzvards")
    private String uzvards;

    @NotNull(message = "E-pasts ir obligāts")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Nepareizs e-pasta formāts"
    )
    @Column(name = "Epasts")
    private String epasts;

    @NotNull(message = "Telefona numurs ir obligāts")
    // @Pattern(regexp = "^(\\\\+371)?\\\\d{8}$", message = "Telefona numuram jābūt formātā +371XXXXXXXX vai XXXXXXXX")
    @Column(name = "TelefonaNr")
    private String telefonaNr;

    @NotNull(message = "Personas ID ir obligāts")
    @Pattern(
            regexp = "[a-zA-Z0-9_-]+",
            message = "Personas ID drīkst saturēt tikai burtus, ciparus, _ un -"
    )
    @Column(name = "PersonasId")
    private String personasId;

    @NotNull(message = "Pilsēta ir obligāta")
    @Pattern(
            regexp = "[A-ZĒŪĪĻĶĢŠĀČŅ]{1}[a-zēūīļķģšāžčņ]+",
            message = "Pilsētai jāsākas ar lielo burtu un jāsatur tikai burti"
    )
    @Size(min = 3, max = 20, message = "Pilsētas nosaukumam jābūt no 3 līdz 20")
    @Column(name = "Pilseta")
    private String pilseta;

    @NotNull(message = "Valsts ir obligāta")
    @Pattern(
            regexp = "[A-ZĒŪĪĻĶĢŠĀČŅa-zēūīļķģšāžčņ ]+",
            message = "Valsts drīkst saturēt tikai burtus un atstarpes"
    )
    @Size(min = 3, max = 20, message = "Valsts nosaukumam jābūt no 3 līdz 20")
    @Column(name = "Valsts")
    private String valsts;

    @NotNull(message = "Ielas nosaukums un numurs ir obligāts")
    // @Pattern(regexp = "^[A-Za-zĀČĒĢĪĶĻŅŠŪŽāčēģīķļņšūž0-9\\\\s,./-]+$", message = "Nepareizs ielas formāts")
    @Column(name = "IelasNosaukumsNumurs")
    private String ielasNosaukumsNumurs;

    @Min(value = 0, message = "Dzīvokļa numurs nevar būt mazāks par 0")
    @Max(value = 250, message = "Dzīvokļa numurs nevar būt lielāks par 250")
    @Column(name = "DzivoklaNr")
    private Integer dzivoklaNr;

    @Pattern(
            regexp = "^[A-Z]{0,3}[-\\s]?\\d{3,6}([-\\s]?\\d{0,4})?[A-Z]{0,3}$",
            message = "Nepareizs pasta indeksa formāts"
    )
    @Column(name = "PastaIndekss")
    private String pastaIndekss;

    @OneToMany(mappedBy = "dalibnieks", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Sertifikati> sertifikati;

    @ToString.Exclude
    @OneToMany(mappedBy = "kursaDalibnieki", cascade = CascadeType.REMOVE)
    private List<Vertejumi> vertejumi;

    @Transient
    private Float avgGrade;

    public KursaDalibnieki(String vards, String uzvards, String epasts, String telefonaNr, String personasId,
                           String pilseta, String valsts, String ielasNosaukumsNumurs, int dzivoklaNr, String pastaIndekss) {
        setVards(vards);
        setUzvards(uzvards);
        setEpasts(epasts);
        setTelefonaNr(telefonaNr);
        setPersonasId(personasId);
        setPilseta(pilseta);
        setValsts(valsts);
        setIelasNosaukumsNumurs(ielasNosaukumsNumurs);
        setDzivoklaNr(dzivoklaNr);
        setPastaIndekss(pastaIndekss);
    }
}