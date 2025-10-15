package lv.sis.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lv.sis.model.KursaDalibnieki;
import lv.sis.model.KursaDatumi;
import lv.sis.model.Vertejumi;

public interface ICRUDVertejumiService {
	
	//C - create
	public abstract void create(float vertejumi, LocalDate datums, KursaDalibnieki kursaDalibnieki, KursaDatumi kursaDatumi) throws Exception;
			
	//R - retrieve by id
	public abstract Vertejumi retrieveById(int vid) throws Exception;
		
	//U - update
	public abstract void updateById(int vid, float vertejums) throws Exception;
		
	//D - delete
	public abstract void deleteById(int vid) throws Exception;

	public abstract Page<Vertejumi> retrieveAll(Pageable pageable) throws Exception;

}
