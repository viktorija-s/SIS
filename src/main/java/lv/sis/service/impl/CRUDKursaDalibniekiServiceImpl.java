package lv.sis.service.impl;

import java.util.List;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import lv.sis.model.KursaDalibnieki;
import lv.sis.repo.IKursaDalibniekiRepo;
import lv.sis.service.ICRUDKursaDalibniekiService;

@Service
public class CRUDKursaDalibniekiServiceImpl implements ICRUDKursaDalibniekiService {

	@Autowired
	private IKursaDalibniekiRepo kursaDalibniekiRepo;

	@Override
	public void create(String vards, String uzvards, String epasts, String telefonaNr, String personasId,
			String pilseta, String valsts, String ielasNosaukumsNumurs, int dzivoklaNr, String pastaIndekss)
			throws Exception {
		if (vards == null || uzvards == null || epasts == null || telefonaNr == null || personasId == null
				|| pilseta == null || valsts == null || ielasNosaukumsNumurs == null || dzivoklaNr < 0
				|| pastaIndekss == null) {
			throw new Exception("Ievades parametri nav pareizi");
		}

		if (kursaDalibniekiRepo.existsByVardsAndUzvards(vards, uzvards)) {
			KursaDalibnieki existingKursaDalibnieki = kursaDalibniekiRepo.findByVardsAndUzvards(vards, uzvards);
		} else {
			KursaDalibnieki newKursaDalibnieki = new KursaDalibnieki(vards, uzvards, epasts, telefonaNr, personasId,
					pilseta, valsts, ielasNosaukumsNumurs, dzivoklaNr, pastaIndekss);
			kursaDalibniekiRepo.save(newKursaDalibnieki);
		}
	}

	@Override
	@Transactional
	public void importCourseParticipants(MultipartFile file) throws Exception {
		try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
			XSSFSheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);

				if (row != null) {
					String personasId = formatter.formatCellValue(row.getCell(4));
					KursaDalibnieki participant = kursaDalibniekiRepo.findByPersonasId(personasId) // ja persona jau ir
																									// datubaze, tad
																									// tiek atgriezts
																									// esosais objekts
																									// un atjaunoti
																									// parejie lauki /
																									// ja persona netiek
																									// atrasta, tad
																									// atgriezas
																									// Optional.empty()
							.orElse(new KursaDalibnieki()); // ja datubaze nekas netika atrasts (Optional ir tukss), tad
															// tiek izveidots jauns KursaDalibnieki objekts.
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
				}
			}
		}
	}

	@Override
	public ArrayList<KursaDalibnieki> retrieveAll() throws Exception {
		if (kursaDalibniekiRepo.count() == 0) {
			throw new Exception("Tabulā nav neviena ieraksta");
		}
		ArrayList<KursaDalibnieki> allKursaDalibnieki = (ArrayList<KursaDalibnieki>) kursaDalibniekiRepo.findAll();
		return allKursaDalibnieki;
	}

	@Override
	public KursaDalibnieki retrieveById(int kdid) throws Exception {
		if (kdid < 0) {
			throw new Exception("Id nevar būt negatīvs");
		}

		if (!kursaDalibniekiRepo.existsById(kdid)) {
			throw new Exception("Kursa dalībnieks ar tādu id neeksistē");
		}

		KursaDalibnieki retrievedKursaDalibnieki = kursaDalibniekiRepo.findById(kdid).get();
		return retrievedKursaDalibnieki;
	}

	@Override
	public void updateById(int kdid, String vards, String uzvards, String epasts, String telefonaNr, String personasId,
			String pilseta, String valsts, String ielasNosaukumsNumurs, int dzivoklaNr, String pastaIndekss)
			throws Exception {
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
	}

	@Override
	public void deleteById(int kdid) throws Exception {
		if (kdid < 0) {
			throw new Exception("Id nevar būt negatīvs");
		}

		if (!kursaDalibniekiRepo.existsById(kdid)) {
			throw new Exception("Kursa dalībnieks ar tādu id neeksistē");
		}

		kursaDalibniekiRepo.deleteById(kdid);
		;

	}

}
