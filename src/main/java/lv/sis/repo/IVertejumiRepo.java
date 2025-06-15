package lv.sis.repo;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import lv.sis.model.Vertejumi;

public interface IVertejumiRepo extends CrudRepository<Vertejumi, Integer>{

	boolean existsByVertejumsAndDatums(float vertejumi, LocalDate datums);

	Vertejumi findByVertejumsAndDatums(float vertejumi, LocalDate datums);

}
