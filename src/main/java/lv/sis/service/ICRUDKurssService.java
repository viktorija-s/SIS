package lv.sis.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lv.sis.model.Kurss;
import lv.sis.model.enums.Limeni;

public interface ICRUDKurssService {
	//C - create
		public abstract void create(String nosaukums, int stundas, Limeni limenis)throws Exception;
		//R - retrieve all
		public abstract Page<Kurss> retrieveAll(Pageable pageable) throws Exception;
				
		//R - retrieve by id
		public abstract Page<Kurss> retrieveById(int kdid) throws Exception;
				
		//U - update
		public abstract void updateById(int kdid, String nosaukums,int stundas, Limeni limenis) throws Exception;
				
		//D - delete
		public abstract void deleteById(int kdid) throws Exception;
}
