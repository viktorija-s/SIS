package lv.sis.service;

import java.util.ArrayList;

import lv.sis.model.Pasniedzeji;

public interface ICRUDPasniedzejiService {


//C - create
	public abstract void create(String vards, String uzvards, String epasts, String telefonaNr)throws Exception;
	//R - retrieve all
	public abstract ArrayList<Pasniedzeji> retrieveAll() throws Exception;
			
	//R - retrieve by id
	public abstract Pasniedzeji retrieveById(int kdid) throws Exception;
			
	//U - update
	public abstract void updateById(int kdid, String vards, String uzvards, String epasts, String telefonaNr) throws Exception;
			
	//D - delete
	public abstract void deleteById(int kdid) throws Exception;
}