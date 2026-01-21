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

public class MyUserTest {
	private static MyUser userDefault;
	private static MyUser userGood;
	private static MyUser userNull;
	private static MyUser userNullVariables;
	
	private static Validator validator;
	
	@BeforeAll
	static void setup() {
		userDefault = new MyUser();
		userGood = new MyUser("user123", "somepass", new MyAuthority("USER"));
		userNull = null;
		userNullVariables = new MyUser(null, null, null);
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	@Test
	void testUserDefault() {
		assertEquals(null, userDefault.getUsername());
		assertEquals(null, userDefault.getPassword());
		assertEquals(null, userDefault.getAuthority());
	}
	
	@Test
	void testUserGood() {
		assertEquals("user123", userGood.getUsername());
		assertEquals("somepass", userGood.getPassword());
		assertEquals("USER", userGood.getAuthority().getTitle());
	}
	
	@Test
	void testUserNull() {
		assertThrows(NullPointerException.class, () -> {userNull.getUId();});
	}
	
	@Test
	void testUserNullvariables() {
		assertEquals(null, userNullVariables.getUsername());
		assertEquals(null, userNullVariables.getPassword());
		assertEquals(null, userNullVariables.getAuthority());
	}
	
	// ---------- VALIDATION ----------
	
	@Test
	void testUserInvalidUsername() {
		MyUser u = new MyUser("isere_!9@", "password", new MyAuthority("USER"));
		Set<ConstraintViolation<MyUser>> result = validator.validate(u);
		assertEquals(1, result.size());
		assertEquals(Pattern.class, result.iterator().next().getConstraintDescriptor().getAnnotation().annotationType());
	}
	
	@Test
	void testUserUsernameTooLong() {
		MyUser u = new MyUser("iserehfkjdsfdlkhnfdkcnxdszxk", "password", new MyAuthority("USER"));
		Set<ConstraintViolation<MyUser>> result = validator.validate(u);
		assertEquals(1, result.size());
		assertEquals(Size.class, result.iterator().next().getConstraintDescriptor().getAnnotation().annotationType());
	}
	
}
