package lv.sis.service.impl;

import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.transaction.Transactional;
import lv.sis.model.KursaDalibnieki;
import lv.sis.repo.IKursaDalibniekiRepo;
import lv.sis.service.ICRUDKursaDalibniekiService;

@Service
public class CRUDKursaDalibniekiServiceImpl implements ICRUDKursaDalibniekiService {
	
	private static final Logger logger = LoggerFactory.getLogger(CRUDKursaDalibniekiServiceImpl.class);

	@Autowired
	private IKursaDalibniekiRepo kursaDalibniekiRepo;

	@Override
	public void create(String vards, String uzvards, String epasts, String telefonaNr, String personasId,
			String pilseta, String valsts, String ielasNosaukumsNumurs, int dzivoklaNr, String pastaIndekss)
			throws Exception {
		
		logger.info("Creating participant: {} {}", vards, uzvards);
		
		if (vards == null || uzvards == null || epasts == null || telefonaNr == null || personasId == null
				|| pilseta == null || valsts == null || ielasNosaukumsNumurs == null || dzivoklaNr < 0
				|| pastaIndekss == null) {
			logger.warn("Invalid input parameters for participant creation: vards={}, uzvards={}, personasId={}", vards, uzvards, personasId);
			throw new Exception("Ievades parametri nav pareizi");
		}


		if (kursaDalibniekiRepo.existsByPersonasId(personasId)) {
			logger.warn("Participant with personasId={} already exists", personasId);
	        throw new Exception("Kursa dalībnieks ar tādu personasId jau eksistē: " + personasId);
	    }

		if (kursaDalibniekiRepo.existsByVardsAndUzvards(vards, uzvards)) {
			logger.info("Participant {} {} already exists, skipping creation", vards, uzvards);
			KursaDalibnieki existingKursaDalibnieki = kursaDalibniekiRepo.findByVardsAndUzvards(vards, uzvards);
		} else {
			KursaDalibnieki newKursaDalibnieki = new KursaDalibnieki(vards, uzvards, epasts, telefonaNr, personasId,
					pilseta, valsts, ielasNosaukumsNumurs, dzivoklaNr, pastaIndekss);
			kursaDalibniekiRepo.save(newKursaDalibnieki);
			logger.info("Participant created successfully: {} {}, personasId={}", vards, uzvards, personasId);
		}
	}

	@Transactional
	public void importCourseParticipants(MultipartFile file) throws Exception {
		logger.info("Importing course participants from file={}", file.getOriginalFilename());
		
		try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);

				if (row != null) {
					String personasId = formatter.formatCellValue(row.getCell(4));
					KursaDalibnieki participant = kursaDalibniekiRepo.findByPersonasId(personasId)
							.orElse(new KursaDalibnieki());
					participant.setPersonasId(personasId);
					participant.setVards(formatter.formatCellValue(row.getCell(0)));
					participant.setUzvards(formatter.formatCellValue(row.getCell(1)));
					participant.setEpasts(formatter.formatCellValue(row.getCell(2)));
					participant.setTelefonaNr(formatter.formatCellValue(row.getCell(3)));
					participant.setPilseta(formatter.formatCellValue(row.getCell(5)));
					participant.setValsts(formatter.formatCellValue(row.getCell(6)));
					participant.setIelasNosaukumsNumurs(formatter.formatCellValue(row.getCell(7)));

					Cell dzivoklaNrCell = row.getCell(8);
					if (dzivoklaNrCell != null && dzivoklaNrCell.getCellType() == CellType.NUMERIC) {
						participant.setDzivoklaNr((int) dzivoklaNrCell.getNumericCellValue());
					} else {
						participant.setDzivoklaNr(null);
					}

					String pastaIndekss = formatter.formatCellValue(row.getCell(9));
					if (pastaIndekss.isEmpty()) {
						participant.setPastaIndekss(null);
					} else {
						participant.setPastaIndekss(pastaIndekss);
					}

					kursaDalibniekiRepo.save(participant);
					logger.debug("Saved participant: {} {}, personasId={}", participant.getVards(), participant.getUzvards(), participant.getPersonasId());
				}
			}
		}
	}

	@Override
	public Page<KursaDalibnieki> retrieveAll(Pageable pageable) throws Exception {
		logger.info("Retrieving all participants with pagination");
		
		if (kursaDalibniekiRepo.count() == 0) {
			logger.warn("No participants found in the database");
			throw new Exception("Tabulā nav neviena ieraksta");
		}
		return kursaDalibniekiRepo.findAll(pageable);
	}

	@Override
	public KursaDalibnieki retrieveById(int kdid) throws Exception {
		logger.info("Retrieving participant by id={}", kdid);
		
		if (kdid < 0) {
			logger.warn("Invalid participant id provided: {}", kdid);
			throw new Exception("Id nevar būt negatīvs");
		}

		if (!kursaDalibniekiRepo.existsById(kdid)) {
			logger.warn("Kursa dalībnieks with id={} does not exist", kdid);
			throw new Exception("Kursa dalībnieks ar tādu id neeksistē");
		}

		KursaDalibnieki retrievedKursaDalibnieki = kursaDalibniekiRepo.findById(kdid).get();
		return retrievedKursaDalibnieki;
	}

	@Override
	public void updateById(int kdid, String vards, String uzvards, String epasts, String telefonaNr, String personasId,
			String pilseta, String valsts, String ielasNosaukumsNumurs, int dzivoklaNr, String pastaIndekss)
			throws Exception {
		
		logger.info("Updating participant with id={}", kdid);
		KursaDalibnieki retrievedKursaDalibnieki = retrieveById(kdid);

		if (retrievedKursaDalibnieki.getVards() != vards) {
			retrievedKursaDalibnieki.setVards(vards);
		}

		if (retrievedKursaDalibnieki.getUzvards() != uzvards) {
			retrievedKursaDalibnieki.setUzvards(uzvards);
		}

		if (retrievedKursaDalibnieki.getEpasts() != epasts) {
			retrievedKursaDalibnieki.setEpasts(epasts);
		}

		if (retrievedKursaDalibnieki.getTelefonaNr() != telefonaNr) {
			retrievedKursaDalibnieki.setTelefonaNr(telefonaNr);
		}

		if (retrievedKursaDalibnieki.getPersonasId() != personasId) {
			retrievedKursaDalibnieki.setPersonasId(personasId);
		}

		if (retrievedKursaDalibnieki.getPilseta() != pilseta) {
			retrievedKursaDalibnieki.setPilseta(pilseta);
		}

		if (retrievedKursaDalibnieki.getValsts() != valsts) {
			retrievedKursaDalibnieki.setValsts(valsts);
		}

		if (retrievedKursaDalibnieki.getIelasNosaukumsNumurs() != ielasNosaukumsNumurs) {
			retrievedKursaDalibnieki.setIelasNosaukumsNumurs(ielasNosaukumsNumurs);
		}

		if (retrievedKursaDalibnieki.getDzivoklaNr() != dzivoklaNr) {
			retrievedKursaDalibnieki.setDzivoklaNr(dzivoklaNr);
		}

		if (retrievedKursaDalibnieki.getPastaIndekss() != pastaIndekss) {
			retrievedKursaDalibnieki.setPastaIndekss(pastaIndekss);
		}

		kursaDalibniekiRepo.save(retrievedKursaDalibnieki);
		logger.info("Participant updated successfully: id={}", kdid);
	}

	@Override
	public void deleteById(int kdid) throws Exception {
		logger.info("Deleting participant with id={}", kdid);
		
		if (kdid < 0) {
			logger.warn("Invalid participant id for deletion: {}", kdid);
			throw new Exception("Id nevar būt negatīvs");
		}

		if (!kursaDalibniekiRepo.existsById(kdid)) {
			logger.warn("Attempted to delete non-existent participant id={}", kdid);
			throw new Exception("Kursa dalībnieks ar tādu id neeksistē");
		}

		kursaDalibniekiRepo.deleteById(kdid);
		logger.info("Participant deleted successfully: id={}", kdid);

	}
	
	@Override
	public ArrayList<KursaDalibnieki> retrieveAll() throws Exception {
		logger.info("Retrieving all participants without pagination");
		
		if(kursaDalibniekiRepo.count()==0) {
			logger.warn("No participants found in the database");
			throw new Exception("Tabulā nav neviena ieraksta");
		}
		ArrayList<KursaDalibnieki> allKursaDalibnieki = (ArrayList<KursaDalibnieki>) kursaDalibniekiRepo.findAll();
		logger.debug("Total participants retrieved: {}", allKursaDalibnieki.size());
		return allKursaDalibnieki;
	}

}
