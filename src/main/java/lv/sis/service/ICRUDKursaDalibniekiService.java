package lv.sis.service;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.web.multipart.MultipartFile;

import lv.sis.model.KursaDalibnieki;

public interface ICRUDKursaDalibniekiService {
	
	//C - create
	public abstract void create(String vards, String uzvards, String epasts, String telefonaNr, String personasId, String pilseta, String valsts, String ielasNosaukumsNumurs, int dzivoklaNr, String pastaIndekss) throws Exception;
			
	//R - retrieve by id
	public abstract Page<KursaDalibnieki> retrieveById(int kdid) throws Exception;
			
	//U - update
	public abstract void updateById(int kdid, String vards, String uzvards, String epasts, String telefonaNr, String personasId, String pilseta, String valsts, String ielasNosaukumsNumurs, int dzivoklaNr, String pastaIndekss) throws Exception;
			
	//D - delete
	public abstract void deleteById(int kdid) throws Exception;


	public abstract Page<KursaDalibnieki> retrieveAll(Pageable pageable) throws Exception;

	void importCourseParticipants(MultipartFile file) throws Exception;

	public abstract ArrayList<KursaDalibnieki> retrieveAll() throws Exception;

}
