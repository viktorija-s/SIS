package lv.sis.repo;

import lv.sis.model.MyAuthority;
import org.springframework.data.repository.CrudRepository;

public interface IMyAuthorityRepo  extends CrudRepository<MyAuthority, Integer>  {

	MyAuthority findByTitle(String string);
}
