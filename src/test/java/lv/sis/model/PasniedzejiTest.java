package lv.sis.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

class PasniedzejiTest {
	private static Pasniedzeji pasnDefault;
	private static Pasniedzeji pasnGood;
	private static Pasniedzeji pasnNull;
	private static Pasniedzeji pasnNullVariables;
	
	private static Validator validator;
	
	@BeforeAll
	static void setUp() {
		pasnDefault = new Pasniedzeji();
		pasnGood = new Pasniedzeji("Vija", "Gaile", "vija@example.com", "+37123232343");
		pasnNull = null;
		pasnNullVariables = new Pasniedzeji(null, null, null, null);
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	@Test
	void testPasnDefault() {
		assertEquals(null, pasnDefault.getVards());
		assertEquals(null, pasnDefault.getUzvards());
		assertEquals(null, pasnDefault.getEpasts());
		assertEquals(null, pasnDefault.getTelefonaNr());
	}
	
	@Test
	void testPasnGood() {
		assertEquals("Vija", pasnGood.getVards());
		assertEquals("Gaile", pasnGood.getUzvards());
		assertEquals("vija@example.com", pasnGood.getEpasts());
		assertEquals("+37123232343", pasnGood.getTelefonaNr());
	}
	
	@Test
	void testPasnNull() {
		assertThrows(NullPointerException.class, () -> {pasnNull.getPid();});
	}
	
	@Test
	void testPasnNullVariables() {
		assertEquals(null, pasnNullVariables.getVards());
		assertEquals(null, pasnNullVariables.getUzvards());
		assertEquals(null, pasnNullVariables.getEpasts());
		assertEquals(null, pasnNullVariables.getTelefonaNr());
	}
	
	// ---------- VALIDATION ----------
	
	@Test
	void testPasnValidationNameSurnameTooShort() {
		Pasniedzeji p = new Pasniedzeji("Ma", "Ka", "ma@ex.com", "+37121222324");
		Set<ConstraintViolation<Pasniedzeji>> result = validator.validate(p);
		assertEquals(2, result.size());
		assertEquals(Size.class, result.iterator().next().getConstraintDescriptor().getAnnotation().annotationType());
	}
	
	@Test
	void testPasnValidationNameSurnameTooLong() {
		Pasniedzeji p = new Pasniedzeji("Mamamamamamamamamamammamamamamama", "Kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk", "ma@ex.com", "+37121222324");
		Set<ConstraintViolation<Pasniedzeji>> result = validator.validate(p);
		assertEquals(2, result.size());
		assertEquals(Size.class, result.iterator().next().getConstraintDescriptor().getAnnotation().annotationType());
	}
	
	@Test
	void testPasnValidationNameSurnameFormat() {
		Pasniedzeji p = new Pasniedzeji("MaRta", "KalEjA", "ma@ex.com", "+37121222324");
		Set<ConstraintViolation<Pasniedzeji>> result = validator.validate(p);
		assertEquals(2, result.size());
		assertEquals(Pattern.class, result.iterator().next().getConstraintDescriptor().getAnnotation().annotationType());
	}
	
	@Test
	void testPasnValidationEmailFormat() {
		Pasniedzeji p = new Pasniedzeji("Marta", "Kalnina", "ma@", "+3fas7121222324");
		Set<ConstraintViolation<Pasniedzeji>> result = validator.validate(p);
		assertEquals(2, result.size());
		assertEquals(Pattern.class, result.iterator().next().getConstraintDescriptor().getAnnotation().annotationType());
	}
	
}
