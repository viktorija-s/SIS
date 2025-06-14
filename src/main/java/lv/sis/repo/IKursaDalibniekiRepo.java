package lv.sis.repo;

import java.util.ArrayList;

import lv.sis.model.KursaDalibnieki;

public interface IKursaDalibniekiRepo {

	int count();

	ArrayList<KursaDalibnieki> findAll();

	boolean existsById(int kdid);

	Object findById(int kdid);

	void deleteById(int kdid);

}
