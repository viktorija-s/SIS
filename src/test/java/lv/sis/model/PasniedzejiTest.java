package lv.sis.model;

import org.junit.jupiter.api.BeforeAll;

import jakarta.validation.Validator;

class PasniedzejiTest {
	private static Pasniedzeji pasnDefault;
	private static Pasniedzeji pasnGood;
	private static Pasniedzeji pasnBad;
	private static Pasniedzeji pasnNull;
	private static Pasniedzeji pasnNullVariables;
	
	private static Validator validator;
	
	@BeforeAll
	static void setUp() {
		pasnDefault = new Pasniedzeji();
		pasnGood = new Pasniedzeji();
		pasnBad = new Pasniedzeji();
		pasnNull = null;
		pasnNullVariables = new Pasniedzeji(null, null, null, null);
	}
}
