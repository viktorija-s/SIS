package lv.sis.controller;

import lv.sis.model.KursaDatumi;
import lv.sis.model.Kurss;
import lv.sis.model.Pasniedzeji;
import lv.sis.repo.IKurssRepo;
import lv.sis.repo.IPasniedzejiRepo;
import lv.sis.service.ICRUDKursaDatumiService;
import lv.sis.service.IFilterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = KursaDatumiCRUDController.class)
@SuppressWarnings({"removal", "deprecation"})
@AutoConfigureMockMvc(addFilters = false)
class KursaDatumiCRUDControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "testModelLayer")
    private CommandLineRunner testModelLayer;

    @MockBean
    private ICRUDKursaDatumiService kursaDatumiService;

    @MockBean
    private IPasniedzejiRepo pasniedzejiRepo;

    @MockBean
    private IKurssRepo kurssRepo;

    @MockBean
    private IFilterService filterService;

    @Test
    void getAddPage_setsDropdownLists() throws Exception {
        when(kurssRepo.findAll()).thenReturn(List.of(new Kurss()));
        when(pasniedzejiRepo.findAll()).thenReturn(List.of(new Pasniedzeji()));

        mockMvc.perform(get("/kursaDatumi/CRUD/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("kursa-datumi-add-page"))
                .andExpect(model().attributeExists("kursaDatumi"))
                .andExpect(model().attributeExists("kurssList"))
                .andExpect(model().attributeExists("pasniedzejiList"));
    }

    @Test
    void postAdd_whenServiceThrows_returnsAddPageAndKeepsDropdownLists() throws Exception {
        when(kurssRepo.findAll()).thenReturn(List.of(new Kurss()));
        when(pasniedzejiRepo.findAll()).thenReturn(List.of(new Pasniedzeji()));

        doThrow(new Exception("Ievades parametri nav pareizi"))
                .when(kursaDatumiService)
                .create(any(), any(), any(), any());

        mockMvc.perform(post("/kursaDatumi/CRUD/add")
                        .param("sakumaDatums", LocalDate.now().plusDays(1).toString())
                        .param("beiguDatums", LocalDate.now().plusDays(2).toString())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("kursa-datumi-add-page"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attributeExists("kurssList"))
                .andExpect(model().attributeExists("pasniedzejiList"));
    }

    @Test
    void getUpdatePage_setsDropdownLists() throws Exception {
        KursaDatumi kd = new KursaDatumi();
        kd.setSakumaDatums(LocalDate.now().plusDays(1));
        kd.setBeiguDatums(LocalDate.now().plusDays(2));

        Page<KursaDatumi> page = new PageImpl<>(List.of(kd));
        when(kursaDatumiService.retrieveById(1)).thenReturn(page);

        when(kurssRepo.findAll()).thenReturn(List.of(new Kurss()));
        when(pasniedzejiRepo.findAll()).thenReturn(List.of(new Pasniedzeji()));

        mockMvc.perform(get("/kursaDatumi/CRUD/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("kursa-datumi-update-page"))
                .andExpect(model().attributeExists("kursaDatumi"))
                .andExpect(model().attributeExists("kurssList"))
                .andExpect(model().attributeExists("pasniedzejiList"));
    }

    @Test
    void postUpdate_whenServiceThrows_returnsUpdatePageAndKeepsDropdownLists() throws Exception {
        KursaDatumi existing = new KursaDatumi();
        existing.setSakumaDatums(LocalDate.now().plusDays(5));
        existing.setBeiguDatums(LocalDate.now().plusDays(10));
        existing.setKurss(new Kurss());
        existing.setPasniedzejs(new Pasniedzeji());

        when(kursaDatumiService.retrieveById(1)).thenReturn(new PageImpl<>(List.of(existing)));

        when(kurssRepo.findAll()).thenReturn(List.of(new Kurss()));
        when(pasniedzejiRepo.findAll()).thenReturn(List.of(new Pasniedzeji()));

        doThrow(new Exception("Kļūda"))
                .when(kursaDatumiService)
                .updateById(eq(1), any(KursaDatumi.class));

        mockMvc.perform(post("/kursaDatumi/CRUD/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("kursa-datumi-update-page"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attributeExists("kurssList"))
                .andExpect(model().attributeExists("pasniedzejiList"));
    }
}
