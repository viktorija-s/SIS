package lv.sis.model;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class KursaDatumiTest {

    @Test
    void testNoArgsConstructor() {
        KursaDatumi kd = new KursaDatumi();
        assertNotNull(kd);
        assertEquals(0, kd.getKursaDatId()); // no setter, default int = 0
    }

    @Test
    void testConstructorWithSakumaBeiguKurss_setsFields() {
        LocalDate start = LocalDate.of(2026, 5, 1);
        LocalDate end = LocalDate.of(2026, 5, 10);

        Kurss kurss = new Kurss();

        KursaDatumi kd = new KursaDatumi(start, end, kurss);

        assertEquals(0, kd.getKursaDatId());
        assertEquals(start, kd.getSakumaDatums());
        assertEquals(end, kd.getBeiguDatums());
        assertSame(kurss, kd.getKurss());
        assertNull(kd.getPasniedzejs());
    }

    @Test
    void testConstructorWithAllParameters_setsFields() {
        LocalDate start = LocalDate.of(2026, 6, 1);
        LocalDate end = LocalDate.of(2026, 6, 30);

        Kurss kurss = new Kurss();
        Pasniedzeji pasn = new Pasniedzeji();

        KursaDatumi kd = new KursaDatumi(start, end, kurss, pasn);

        assertEquals(0, kd.getKursaDatId());
        assertEquals(start, kd.getSakumaDatums());
        assertEquals(end, kd.getBeiguDatums());
        assertSame(kurss, kd.getKurss());
        assertSame(pasn, kd.getPasniedzejs());
    }

    @Test
    void testSettersAndGetters_setsAllFields() {
        KursaDatumi kd = new KursaDatumi();

        LocalDate start = LocalDate.of(2026, 1, 15);
        LocalDate end = LocalDate.of(2026, 2, 15);
        Kurss kurss = new Kurss();
        Pasniedzeji pasn = new Pasniedzeji();

        kd.setSakumaDatums(start);
        kd.setBeiguDatums(end);
        kd.setKurss(kurss);
        kd.setPasniedzejs(pasn);

        assertEquals(start, kd.getSakumaDatums());
        assertEquals(end, kd.getBeiguDatums());
        assertSame(kurss, kd.getKurss());
        assertSame(pasn, kd.getPasniedzejs());
    }

    @Test
    void testVertejumiList_setAndGet() {
        KursaDatumi kd = new KursaDatumi();

        ArrayList<Vertejumi> vertejumi = new ArrayList<>();
        kd.setVertejumi(vertejumi);

        assertSame(vertejumi, kd.getVertejumi());
    }

    @Test
    void testNullableFields_canBeNull() {
        KursaDatumi kd = new KursaDatumi();

        kd.setSakumaDatums(null);
        kd.setBeiguDatums(null);
        kd.setKurss(null);
        kd.setPasniedzejs(null);
        kd.setVertejumi(null);

        assertNull(kd.getSakumaDatums());
        assertNull(kd.getBeiguDatums());
        assertNull(kd.getKurss());
        assertNull(kd.getPasniedzejs());
        assertNull(kd.getVertejumi());
    }

    @Test
    void testToString_doesNotThrow() {
        KursaDatumi kd = new KursaDatumi();
        assertDoesNotThrow(kd::toString);
    }
}