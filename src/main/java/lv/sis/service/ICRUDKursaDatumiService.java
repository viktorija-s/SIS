package lv.sis.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lv.sis.model.KursaDatumi;
import lv.sis.model.Kurss;
import lv.sis.model.Pasniedzeji;


public interface ICRUDKursaDatumiService {
	
	//C - create
	public abstract void create(LocalDate sakumaDatums, LocalDate beiguDatums, 
			Kurss kurss, Pasniedzeji pasniedzejs) throws Exception;
					
	//U - update
	public abstract void updateById(int kursaDatId, KursaDatumi kursaDatumi) throws Exception;
			
	//D - delete
	public abstract void deleteById(int kursaDatId) throws Exception;

	Page<KursaDatumi> retrieveAll(Pageable pageable) throws Exception;

	Page<KursaDatumi> retrieveById(int kursaDatId) throws Exception;


}
