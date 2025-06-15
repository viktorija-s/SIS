package lv.sis.service;

import java.time.LocalDate;
import java.util.ArrayList;

import lv.sis.model.KursaDalibnieki;
import lv.sis.model.Sertifikati;
import lv.sis.model.enums.CertificateType;

public interface ICRUDSertifikatiService {
	public abstract void create(CertificateType tips, LocalDate izdosanasDatums, int regNr, boolean irParakstits, KursaDalibnieki dalibnieks) throws Exception;
	public abstract ArrayList<Sertifikati> retrieveAll() throws Exception;
	public abstract Sertifikati retrieveById(int id) throws Exception;
	public abstract void updateById(int id, CertificateType tips, LocalDate izdosanasDatums, int regNr, boolean irParakstits) throws Exception; 
	public abstract void deleteById(int id) throws Exception;
}
