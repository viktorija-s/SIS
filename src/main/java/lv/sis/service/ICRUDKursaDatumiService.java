package lv.sis.service;

import java.util.ArrayList;

import lv.sis.model.KursaDatumi;

public interface ICRUDKursaDatumiService {

	ArrayList<KursaDatumi> retrieveAll();

	KursaDatumi retrieveById(Integer id);

	void deleteById(int id);

}
