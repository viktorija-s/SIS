package lv.sis.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.transaction.Transactional;
import lv.sis.model.Kurss;
import lv.sis.model.enums.Limeni;
import org.springframework.web.multipart.MultipartFile;

public interface ICRUDKurssService {

    public abstract void create(String nosaukums, int stundas, Limeni limenis) throws Exception;

    public abstract Page<Kurss> retrieveAll(Pageable pageable) throws Exception;

    public abstract Kurss retrieveById(int kdid) throws Exception;

    public abstract void updateById(int kdid, String nosaukums, int stundas, Limeni limenis) throws Exception;

    public abstract void deleteById(int kdid) throws Exception;

    @Transactional
    void importCourses(MultipartFile file) throws Exception;
}
