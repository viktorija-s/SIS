package lv.sis.repo;

import java.sql.Date;

import org.springframework.data.repository.CrudRepository;

import lv.sis.model.Vertejumi;

public interface IVertejumiRepo extends CrudRepository<Vertejumi, Integer>{

	boolean existsByVertejumsAndDatums(float vertejumi, Date datums);

	Vertejumi findByVertejumsAndDatums(float vertejumi, Date datums);

}
