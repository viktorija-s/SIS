package lv.sis.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import lv.sis.model.Pasniedzeji;
import lv.sis.service.ICRUDPasniedzejiService;

public class PasniedzejiControllerTest {
	@Mock
	private ICRUDPasniedzejiService pasnService;
	
	@InjectMocks
	private PasniedzejiCRUDController pasnController;
	
	@Autowired
	private MockMvc mockMVC;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMVC = MockMvcBuilders.standaloneSetup(pasnController).build();
	}
	
	@Test
	void testGetAllController() throws Exception {
		Pasniedzeji p1 = new Pasniedzeji("Marta", "Lemberga", "lemberga@marta.com", "+37124345324");
		Pasniedzeji p2 = new Pasniedzeji("Vija", "Egle", "egle@vija.com", "+37125678976");
		ArrayList<Pasniedzeji> allPasn = new ArrayList<>(Arrays.asList(p1, p2));
		Page<Pasniedzeji> pasn = new PageImpl<>(allPasn);
		
		when((pasnService.retrieveAll(any(Pageable.class)))).thenReturn(pasn);
		
		mockMVC.perform(get("/pasniedzeji/CRUD/show/all"))
	        .andExpect(status().isOk())
	        .andExpect(view().name("pasniedzeji-all-page"))   
	        .andExpect(model().attributeExists("package"));
	}
	
	@Test
	void postInsertController() throws Exception {
		Pasniedzeji p = new Pasniedzeji("Janis", "Ozols", "ozols@example.com", "+37127438590");
		
		mockMVC.perform(post("/pasniedzeji/CRUD/add")
		        .param("vards", "Janis")
		        .param("uzvards", "Ozols")
		        .param("epasts", "ozols@example.com")
		        .param("telefonaNr", "+37127438590"))
		    .andExpect(status().is3xxRedirection())
		    .andExpect(redirectedUrl("/pasniedzeji/CRUD/show/all"));
	}
	
	
}
