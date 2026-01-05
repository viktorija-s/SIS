package lv.sis.service.impl;

import java.util.ArrayList;

import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.sis.model.Kurss;
import lv.sis.model.enums.Limeni;
import lv.sis.repo.IKurssRepo;
import lv.sis.repo.ISertifikatiRepo;
import lv.sis.service.ICRUDKurssService;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ICRUDKurssServiceImpl implements ICRUDKurssService{
	@Autowired
    IKurssRepo kurssRepo;

	@Autowired
	ISertifikatiRepo sertRepo;

	@Override
	public void create(String nosaukums, int stundas, Limeni limenis) throws Exception {
		if (nosaukums == null || stundas<0 || limenis == null) {
			throw new Exception("Dati nav pareizi");
		}
		if (kurssRepo.existsByNosaukums(nosaukums)) {
			throw new Exception("Tads kurss jau eksistē");
		}
		
		Kurss newKurss = new Kurss(nosaukums, stundas, limenis);
		kurssRepo.save(newKurss);
	}

    @Transactional
    @Override
    public void importCourses(MultipartFile file) throws Exception {

        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null) continue;

                String nosaukums = formatter.formatCellValue(row.getCell(0));
                int stundas = (int) row.getCell(1).getNumericCellValue();
                Limeni limenis = Limeni.valueOf(
                        formatter.formatCellValue(row.getCell(2))
                );

                create(nosaukums, stundas, limenis);
            }
        }
    }


    @Override
	public ArrayList<Kurss> retrieveAll() throws Exception {
			if (kurssRepo.count() == 0) {
			throw new Exception("Tabula ir tukša");
		}
		
		return (ArrayList<Kurss>)kurssRepo.findAll();
	
	}

	@Override
	public Kurss retrieveById(int kdid) throws Exception {
		if (kdid < 0) {
			throw new Exception("ID nav pareizs");
		}
		if (!kurssRepo.existsById(kdid)) {
			throw new Exception("Kurss ar tadu id neeksistē");
		}
		
		return kurssRepo.findById(kdid).get();
	}

	@Override
	public void updateById(int kdid, String nosaukums, int stundas, Limeni limenis) throws Exception {
		if (kdid < 0) {
			throw new Exception("ID nav pareizs");
		}
		if (!kurssRepo.existsById(kdid)) {
			throw new Exception("Kurss ar tadu id neeksistē");
		}
		
		Kurss selectedKurss = kurssRepo.findById(kdid).get();
		
		selectedKurss.setNosaukums(nosaukums);
		selectedKurss.setStundas(stundas);
		selectedKurss.setLimenis(limenis);
		
		kurssRepo.save(selectedKurss);
	}

	@Override
    public void deleteById(int kid) throws Exception {
        if (kid < 0) {
            throw new Exception("Id nevar būt negatīvs");
        }
        if (!kurssRepo.existsById(kid)) {
            throw new Exception("Kurss ar tādu ID neeksistē");
        }
        kurssRepo.deleteById(kid);
    }

}
