package lv.sis.service.impl;

import lv.sis.model.Kurss;
import lv.sis.model.Pasniedzeji;
import lv.sis.model.Vertejumi;
import lv.sis.repo.IKurssRepo;
import lv.sis.repo.IPasniedzejiRepo;
import lv.sis.repo.IVertejumiRepo;
import lv.sis.service.IFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;

@Service
public class FilterServiceImpl implements IFilterService {

    private static final Logger logger = LoggerFactory.getLogger(FilterServiceImpl.class);

    @Autowired
    private IKurssRepo kurssRepo;

    @Autowired
    IVertejumiRepo vertejumiRepo;

    @Autowired
    IPasniedzejiRepo pasniedzejiRepo;

    @Override
    public ArrayList<Kurss> findByNosaukumsContainingIgnoreCase(String text) throws Exception {
        logger.info("Searching courses by name containing text: {}", text);

        ArrayList<Kurss> result = kurssRepo.findByNosaukumsContaining(text);

        logger.debug("Found {} courses matching the search text", result.size());
        return result;
    }

    @Override
    public Page<Vertejumi> retrieveByMinMax(Integer min, Integer max, Pageable pageable) throws Exception {
        logger.info("Filtering grades by min={} and max={}", min, max);

        if (min == null && max == null) {
            logger.error("Both minimum and maximum grade values are missing");
            throw new Exception("Jānorāda MIN vai MAX vērtējums");
        }

        int minValue = (min == null) ? 0 : min;
        int maxValue = (max == null) ? 10 : max;

        logger.debug("Resolved grade range: {} - {}", minValue, maxValue);

        if (minValue < 0 || maxValue > 10) {
            logger.error("Grade range out of bounds: {} - {}", minValue, maxValue);
            throw new Exception("Vērtējumam jābūt robežās no 0 līdz 10");
        }

        if (minValue > maxValue) {
            logger.error("Invalid grade range: min > max ({} > {})", minValue, maxValue);
            throw new Exception("MIN vērtējums nedrīkst būt lielāks par MAX");
        }

        Page<Vertejumi> result =
                vertejumiRepo.findByVertejumsBetween(minValue, maxValue, pageable);

        logger.debug("Filtered grades count: {}", result.getTotalElements());
        return result;
    }

    @Override
    public Page<Pasniedzeji> retrieveByKurss(String nosaukums, Pageable pageable) throws Exception {
        logger.info("Searching lecturers by course name: {}", nosaukums);

        if (nosaukums == null || nosaukums.trim().isEmpty()) {
            logger.error("Course name is null or empty");
            throw new Exception("Jānorāda kursa nosaukums");
        }

        Page<Pasniedzeji> result =
                pasniedzejiRepo.findByKursaDatumi_Kurss_NosaukumsContainingIgnoreCase(nosaukums, pageable);

        logger.debug("Found {} lecturers for course search", result.getTotalElements());
        return result;
    }
}
