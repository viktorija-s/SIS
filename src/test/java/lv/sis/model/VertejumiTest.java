package lv.sis.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class VertejumiTest {

	private static Vertejumi vertejumiDefault;
	private static Vertejumi vertejumiGood;
	private static Vertejumi vertejumiBad;
	private static Vertejumi vertejumiNull;

	private static Validator validator;

	@BeforeAll
	static void setUp() {
		vertejumiDefault = new Vertejumi();
		vertejumiGood = new Vertejumi(8.5f, LocalDate.now(), null, null);

		vertejumiBad = new Vertejumi(12.0f, LocalDate.now().plusDays(1), null, null);

		vertejumiNull = null;

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void testVertejumiGood() {
		assertEquals(8.5f, vertejumiGood.getVertejums(), 0.001);
		assertEquals(LocalDate.now(), vertejumiGood.getDatums());
		assertNull(vertejumiGood.getKursaDalibnieki());
		assertNull(vertejumiGood.getKursaDatumi());
	}

	@Test
	void testVertejumiNull() {
		assertThrows(NullPointerException.class, () -> {
			vertejumiNull.getVid();
		});
	}

	@Test
	void testVertejumiValidationVertejumsTooHigh() {
		Vertejumi v1 = new Vertejumi(11.0f, LocalDate.now(), null, null);

		Set<ConstraintViolation<Vertejumi>> result = validator.validate(v1);

		assertEquals(1, result.size());
		assertEquals("vertejums", result.iterator().next().getPropertyPath().toString());
	}

	@Test
	void testVertejumiValidationVertejumsTooLow() {
		Vertejumi v1 = new Vertejumi(-1.0f, LocalDate.now(), null, null);

		Set<ConstraintViolation<Vertejumi>> result = validator.validate(v1);

		assertEquals(1, result.size());
		assertEquals("vertejums", result.iterator().next().getPropertyPath().toString());
	}

	@Test
	void testVertejumiValidationDatumsNull() {
		Vertejumi v1 = new Vertejumi(5.0f, null, null, null);

		Set<ConstraintViolation<Vertejumi>> result = validator.validate(v1);

		assertEquals(1, result.size());
		assertEquals("datums", result.iterator().next().getPropertyPath().toString());
	}

	@Test
	void testVertejumiValidationDatumsFuture() {
		Vertejumi v1 = new Vertejumi(5.0f, LocalDate.now().plusDays(1), null, null);

		Set<ConstraintViolation<Vertejumi>> result = validator.validate(v1);

		assertEquals(1, result.size());
		assertEquals("datums", result.iterator().next().getPropertyPath().toString());
	}

	@Test
	void testVertejumiValidationMultipleViolations() {
		Set<ConstraintViolation<Vertejumi>> result = validator.validate(vertejumiBad);

		assertEquals(2, result.size());
	}
}
