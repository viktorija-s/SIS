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
import lv.sis.model.enums.CertificateType;

class SertifikatiTest {

	private static Sertifikati sertDefault;
	private static Sertifikati sertGood;
	private static Sertifikati sertBad;
	private static Sertifikati sertNull;

	private static Validator validator;

	@BeforeAll
	static void setUp() {
		sertDefault = new Sertifikati();
		sertGood = new Sertifikati(CertificateType.FULL, LocalDate.now(), "AB1234", true, null, null);
		sertBad = new Sertifikati(null, LocalDate.now().plusDays(1), "a1", false, null, null);
		sertNull = null;

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void testSertifikatiGood() {
		assertEquals(CertificateType.FULL, sertGood.getTips());
		assertEquals(LocalDate.now(), sertGood.getIzdosanasDatums());
		assertEquals("AB1234", sertGood.getCertificateNo());
		assertTrue(sertGood.isIrParakstits());
		assertNull(sertGood.getDalibnieks());
		assertNull(sertGood.getKurss());
	}

	@Test
	void testSertifikatiNull() {
		assertThrows(NullPointerException.class, () -> {
			sertNull.getSid();
		});
	}

	@Test
	void testSertifikatiValidationCertificateNoPattern() {
		Sertifikati s1 = new Sertifikati(CertificateType.FULL, LocalDate.now(), "Ab1234", false, null, null);

		Set<ConstraintViolation<Sertifikati>> result = validator.validate(s1);

		assertEquals(1, result.size());
		assertEquals("certificateNo", result.iterator().next().getPropertyPath().toString());
	}

	@Test
	void testSertifikatiValidationDatumsFuture() {
		Sertifikati s1 = new Sertifikati(CertificateType.FULL, LocalDate.now().plusDays(1), // nākotnē
				"AB1234", false, null, null);

		Set<ConstraintViolation<Sertifikati>> result = validator.validate(s1);

		assertEquals(1, result.size());
		assertEquals("izdosanasDatums", result.iterator().next().getPropertyPath().toString());
	}

	@Test
	void testSertifikatiValidationMultipleViolations() {
		Set<ConstraintViolation<Sertifikati>> result = validator.validate(sertBad);

		assertTrue(result.size() >= 3);
	}
}
