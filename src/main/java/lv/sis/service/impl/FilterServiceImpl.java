package lv.sis.service.impl;

import lv.sis.model.*;
import lv.sis.repo.*;
import lv.sis.model.enums.CertificateType;
import lv.sis.service.IFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class FilterServiceImpl implements IFilterService {

	@Autowired
	private IKurssRepo kurssRepo;

	@Autowired
	private ISertifikatiRepo sertifikatiRepo;

    @Autowired IVertejumiRepo vertejumiRepo;
    
    @Autowired IPasniedzejiRepo pasniedzejiRepo;

    @Autowired
    private IKursaDalibniekiRepo kursaDalibniekiRepo;

    @Override
    public Page<Kurss> findByNosaukumsContainingIgnoreCase(String text) throws Exception {
        return new PageImpl<>(kurssRepo.findByNosaukumsContaining(text), Pageable.unpaged(), kurssRepo.findByNosaukumsContaining(text).size());
    }
    
    @Override
    public Page<Vertejumi> retrieveByMinMax(Integer min, Integer max, Pageable pageable) throws Exception {

        if (min == null && max == null) {
            throw new Exception("Jānorāda MIN vai MAX vērtējums");
        }

        int minValue = (min == null) ? 0 : min;
        int maxValue = (max == null) ? 10 : max;

        if (minValue < 0 || maxValue > 10) {
            throw new Exception("Vērtējumam jābūt robežās no 0 līdz 10");
        }

        if (minValue > maxValue) {
            throw new Exception("MIN vērtējums nedrīkst būt lielāks par MAX");
        }

        return vertejumiRepo.findByVertejumsBetween(minValue, maxValue, pageable);
    }
    
    @Override
    public Page<Pasniedzeji> retrieveByKurss(String nosaukums, Pageable pageable) throws Exception {
        
        if (nosaukums == null || nosaukums.trim().isEmpty()) {
            throw new Exception("Jānorāda kursa nosaukums");
        }
        
       
        return pasniedzejiRepo.findByKursaDatumi_Kurss_NosaukumsContainingIgnoreCase(nosaukums, pageable);
    }

	@Autowired
	private IKursaDatumiRepo kursaDatumiRepo;

	@Override
	public Page<KursaDatumi> findKursaDatumiBetweenDates(LocalDate from, LocalDate to) throws Exception {
		if (from == null || to == null || from.isAfter(to)) {
			throw new Exception("Nav norādīti pareizi datumi.");
		}
		return new PageImpl<>(
				kursaDatumiRepo.findBySakumaDatumsLessThanEqualAndBeiguDatumsGreaterThanEqual(to, from), 
				Pageable.unpaged(), 
				kursaDatumiRepo.findBySakumaDatumsLessThanEqualAndBeiguDatumsGreaterThanEqual(to, from).size()
				);
	}

	@Override
	public ArrayList<Sertifikati> findSertifikatiByDalibniekaVardsUzvards(String vards, String uzvards)
			throws Exception {
		if ((vards == null || vards.isBlank()) && (uzvards == null || uzvards.isBlank())) {
			throw new Exception("Name or surname must be provided");
		}
		return sertifikatiRepo.findByDalibnieks_VardsContainingIgnoreCaseAndDalibnieks_UzvardsContainingIgnoreCase(
				vards == null ? "" : vards.trim(), uzvards == null ? "" : uzvards.trim());
	}

	public ArrayList<Sertifikati> findByTipsContainingIgnoreCase(String text) throws Exception {
		CertificateType type = CertificateType.valueOf(text.toUpperCase());
		return sertifikatiRepo.findByTips(type);
	}

    @Override
    public Page<KursaDalibnieki> findDalibniekiByNameQuery(String q, Pageable pageable) {
        if (q == null || q.isBlank()) {
            return kursaDalibniekiRepo.findAll(pageable);
        }

        String trimmed = q.trim();

        if (!trimmed.matches("^[\\p{L} \\-]+$")) {
            return kursaDalibniekiRepo.findAll(pageable);
        }

        return kursaDalibniekiRepo
                .findByVardsContainingIgnoreCaseOrUzvardsContainingIgnoreCase(trimmed, trimmed, pageable);
    }

}
