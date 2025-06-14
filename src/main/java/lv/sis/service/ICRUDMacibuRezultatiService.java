package lv.sis.service;

import java.util.ArrayList;

import lv.sis.model.Kurss;
import lv.sis.model.MacibuRezultati;

public interface ICRUDMacibuRezultatiService {
	
	//C - create
	public abstract void create(int macibuRezultats, Kurss kurss) throws Exception;
		
	//R - retrieve all
	public abstract ArrayList<MacibuRezultati> retrieveAll() throws Exception;
		
	//R - retrieve by id
	public abstract MacibuRezultati retrieveById(int mrid) throws Exception;
		
	//U - update
	public abstract void updateById(int mrid, int macibuRezultats, Kurss kurss) throws Exception;
		
	//D - delete
	public abstract void deleteById(int mrid) throws Exception;

}
