package lv.sis.service;

import lv.sis.model.enums.CertificateType;

public interface IPDFCreatorService {
	
	void createCertificateAsPDF(int kdId, int kId) throws Exception;

}
