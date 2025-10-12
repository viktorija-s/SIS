package lv.sis.repo;

import lv.sis.model.MyUser;
import org.springframework.data.repository.CrudRepository;

public interface IMyUserRepo extends CrudRepository<MyUser, Integer> {

	boolean existsByUsername(String username);

	MyUser findByUsername(String username);
}
