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

class MyAuthorityTest {
	private static MyAuthority authDefault;
	private static MyAuthority authGood;
	private static MyAuthority authNull;
	private static MyAuthority authNullVariable;
	
	private static Validator validator;
	
	@BeforeAll
	static void setup() {
		authDefault = new MyAuthority();
		authGood = new MyAuthority("USER");
		authNull = null;
		authNullVariable = new MyAuthority(null);
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	@Test
	void testAuthDefault() {
		assertEquals(null, authDefault.getTitle());
	}
	
	@Test
	void testAuthGood() {
		assertEquals("USER", authGood.getTitle());
	}
	
	@Test
	void testAuthNull() {
		assertThrows(NullPointerException.class, () -> {authNull.getAId();});
	}
	
	@Test
	void testAuthNullVariable() {
		assertEquals(null, authNullVariable.getTitle());
	}
	
	// ---------- VALIDATION ----------
	
	@Test
	void testAuthValidationTitleTooShort() {
		MyAuthority a = new MyAuthority("U");
		Set<ConstraintViolation<MyAuthority>> result = validator.validate(a);
		assertEquals(1, result.size());
		assertEquals(Size.class, result.iterator().next().getConstraintDescriptor().getAnnotation().annotationType());
	}
	
	@Test
	void testAuthValidationTitleTooLong() {
		MyAuthority a = new MyAuthority("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
		Set<ConstraintViolation<MyAuthority>> result = validator.validate(a);
		assertEquals(1, result.size());
		assertEquals(Size.class, result.iterator().next().getConstraintDescriptor().getAnnotation().annotationType());
	}
	
	@Test
	void testAuthValidationTitleFormat() {
		MyAuthority a = new MyAuthority("user");
		Set<ConstraintViolation<MyAuthority>> result = validator.validate(a);
		assertEquals(1, result.size());
		assertEquals(Pattern.class, result.iterator().next().getConstraintDescriptor().getAnnotation().annotationType());
	}
}
