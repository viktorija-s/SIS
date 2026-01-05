package lv.sis.service;

import java.util.ArrayList;

import jakarta.transaction.Transactional;
import lv.sis.model.Kurss;
import lv.sis.model.enums.Limeni;
import org.springframework.web.multipart.MultipartFile;

public interface ICRUDKurssService {
	//C - create
		public abstract void create(String nosaukums, int stundas, Limeni limenis)throws Exception;

    @Transactional
    void importCourses(MultipartFile file) throws Exception;

    //R - retrieve all
		public abstract ArrayList<Kurss> retrieveAll() throws Exception;
				
		//R - retrieve by id
		public abstract Kurss retrieveById(int kdid) throws Exception;
				
		//U - update
		public abstract void updateById(int kdid, String nosaukums,int stundas, Limeni limenis) throws Exception;
				
		//D - delete
		public abstract void deleteById(int kdid) throws Exception; 
}
