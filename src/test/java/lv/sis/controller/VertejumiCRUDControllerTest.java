package lv.sis.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import lv.sis.model.KursaDalibnieki;
import lv.sis.model.KursaDatumi;
import lv.sis.model.Vertejumi;
import lv.sis.repo.IKursaDalibniekiRepo;
import lv.sis.repo.IKursaDatumiRepo;
import lv.sis.service.ICRUDVertejumiService;
import lv.sis.service.IFilterService;

class VertejumiCRUDControllerTest {

	@Mock
	private ICRUDVertejumiService vertejumiServiss;

	@Mock
	private IKursaDalibniekiRepo kursaDalibniekiRepo;

	@Mock
	private IKursaDatumiRepo kursaDatumiRepo;

	@Mock
	private IFilterService filterService;

	@InjectMocks
	private VertejumiCRUDController vertejumiController;

	@Autowired
	private MockMvc mockMVC;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMVC = MockMvcBuilders.standaloneSetup(vertejumiController).build();
	}

	@Test
	void testShowAll_NoFilter() {
		try {
			Pageable pageable = PageRequest.of(0, 3);
			Page<Vertejumi> page = new PageImpl<>(new ArrayList<>(), pageable, 0);

			when(vertejumiServiss.retrieveAll(any(Pageable.class))).thenReturn(page);

			mockMVC.perform(get("/vertejumi/CRUD/show/all")).andExpect(status().isOk())
					.andExpect(view().name("vertejumi-all-page")).andExpect(model().attributeExists("package"));

		} catch (Exception e) {
			fail(e);
		}
	}

	@Test
	void testShowAll_WithFilter() {
		try {
			Pageable pageable = PageRequest.of(0, 3);
			Page<Vertejumi> page = new PageImpl<>(new ArrayList<>(), pageable, 0);

			when(filterService.retrieveByMinMax(eq(1), eq(10), any(Pageable.class))).thenReturn(page);

			mockMVC.perform(get("/vertejumi/CRUD/show/all").param("min", "1").param("max", "10"))
					.andExpect(status().isOk()).andExpect(view().name("vertejumi-all-page"))
					.andExpect(model().attributeExists("package")).andExpect(model().attribute("min", 1))
					.andExpect(model().attribute("max", 10));

		} catch (Exception e) {
			fail(e);
		}
	}

	@Test
	void testShowById() {
		try {
			int id = 5;
			Page<Vertejumi> page = new PageImpl<>(new ArrayList<>());

			when(vertejumiServiss.retrieveById(id)).thenReturn(page);

			mockMVC.perform(get("/vertejumi/CRUD/show/all/{id}", id)).andExpect(status().isOk())
					.andExpect(view().name("vertejumi-all-page")).andExpect(model().attributeExists("package"));

		} catch (Exception e) {
			fail(e);
		}
	}

	@Test
	void testRemoveGet() {
		try {
			int id = 7;
			Vertejumi v = new Vertejumi(8.0f, LocalDate.now(), null, null);

			Page<Vertejumi> page = new PageImpl<>(List.of(v));
			when(vertejumiServiss.retrieveById(id)).thenReturn(page);

			mockMVC.perform(get("/vertejumi/CRUD/remove/{id}", id)).andExpect(status().isOk())
					.andExpect(view().name("vertejumi-delete-confirm")).andExpect(model().attributeExists("vertejumi"));

		} catch (Exception e) {
			fail(e);
		}
	}

	@Test
	void testRemovePost() {
		try {
			int id = 7;

			mockMVC.perform(post("/vertejumi/CRUD/remove/{id}", id)).andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/vertejumi/CRUD/show/all"));

			verify(vertejumiServiss, times(1)).deleteById(id);

		} catch (Exception e) {
			fail(e);
		}
	}

	@Test
	void testAddGet() {
		try {
			when(kursaDalibniekiRepo.findAll()).thenReturn(new ArrayList<KursaDalibnieki>());
			when(kursaDatumiRepo.findAll()).thenReturn(new ArrayList<KursaDatumi>());

			mockMVC.perform(get("/vertejumi/CRUD/add")).andExpect(status().isOk())
					.andExpect(view().name("vertejumi-add-page")).andExpect(model().attributeExists("vertejumi"))
					.andExpect(model().attributeExists("kursaDalibniekiList"))
					.andExpect(model().attributeExists("kursaDatumiList"));

		} catch (Exception e) {
			fail(e);
		}
	}

	@Test
	void testUpdateGet() {
		try {
			int id = 3;
			Vertejumi v = new Vertejumi(7.0f, LocalDate.now(), null, null);
			Page<Vertejumi> page = new PageImpl<>(List.of(v));

			when(vertejumiServiss.retrieveById(id)).thenReturn(page);
			when(kursaDalibniekiRepo.findAll()).thenReturn(new ArrayList<KursaDalibnieki>());
			when(kursaDatumiRepo.findAll()).thenReturn(new ArrayList<KursaDatumi>());

			mockMVC.perform(get("/vertejumi/CRUD/update/{id}", id)).andExpect(status().isOk())
					.andExpect(view().name("vertejumi-update-page")).andExpect(model().attributeExists("vertejumi"))
					.andExpect(model().attributeExists("kursaDalibniekiList"))
					.andExpect(model().attributeExists("kursaDatumiList"));

		} catch (Exception e) {
			fail(e);
		}
	}

	@Test
	void testUpdatePost() {
		try {
			int id = 3;

			mockMVC.perform(post("/vertejumi/CRUD/update/{id}", id).param("vertejums", "9.0"))
					.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/vertejumi/CRUD/show/all"));

			verify(vertejumiServiss, times(1)).updateById(eq(id), eq(9.0f));

		} catch (Exception e) {
			fail(e);
		}
	}
}
