package lv.sis.service.impl;

import java.awt.Color;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.Image;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.BaseFont;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.sis.model.KursaDalibnieki;
import lv.sis.model.MacibuRezultati;
import lv.sis.model.Vertejumi;
import lv.sis.model.enums.CertificateType;
import lv.sis.repo.IKursaDalibniekiRepo;
import lv.sis.repo.IKurssRepo;
import lv.sis.repo.IVertejumiRepo;
import lv.sis.service.IPDFCreatorService;

@Service
public class PDFCreatorImpl implements IPDFCreatorService {

    @Autowired
    private IKursaDalibniekiRepo kursaDalibniekiRepo;

    @Autowired
    private IKurssRepo kurssRepo;

    @Autowired
    private IVertejumiRepo vertejumiRepo;

    private static final String FONT_PATH = "src\\main\\resources\\fonts\\DejaVuSans.ttf";


    private Font getUnicodeFont(float size, boolean bold, Color color) {
        try {
            BaseFont baseFont = BaseFont.createFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            int style;
            if (bold==true) {
                style = Font.BOLD;
            } else {
                style = Font.NORMAL;
            }

            return new Font(baseFont, size, style, color);
        } catch (Exception e) {
            throw new RuntimeException("Neizdevās ielādēt fontu: " + FONT_PATH, e);
        }
    }

    private Paragraph createCenteredParagraph(String text, float fontSize) {
        Paragraph paragraph = new Paragraph(text, getUnicodeFont(fontSize, false, new Color(8, 20, 64)));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        return paragraph;
    }

    @Override
    public void createCertificateAsPDF(int kdId, int kId) throws Exception {

        if (!kursaDalibniekiRepo.existsById(kdId)) {
            throw new Exception("Kursa dalībnieks ar id: " + kdId + " neeksistē");
        }

        if (!kurssRepo.existsById(kId)) {
            throw new Exception("Kurss ar id: " + kId + " neeksistē");
        }

        Vertejumi vertejumiFromDB = vertejumiRepo.findByKursaDalibnieki_KdidAndKursaDatumi_Kurss_Kid(kdId, kId);

        if (vertejumiFromDB == null) {
            throw new Exception("Kursa dalībniekam ar id: " + kdId + " neeksistē vērtējuma kursā ar id: " + kId);
        } else {

            ArrayList<KursaDalibnieki> dalibnieki = kursaDalibniekiRepo.findByKdid(kdId);

            for (KursaDalibnieki k : dalibnieki) {
                if (k.getSertifikati() != null) {
                    boolean hasFull = k.getSertifikati().stream().anyMatch(s -> s.getTips() == CertificateType.Full);

                    String kursaDalibniekaVardsUnUzvards = vertejumiFromDB.getKursaDalibnieki().getVards() + " "
                            + vertejumiFromDB.getKursaDalibnieki().getUzvards();

                    String kursaNosaukums = vertejumiFromDB.getKursaDatumi().getKurss().getNosaukums();

                    float vertejums = vertejumiFromDB.getVertejums();

                    Random random = new Random();
                    int certificateNo = random.nextInt(100000, 999999);

                    int stundas = vertejumiFromDB.getKursaDatumi().getKurss().getStundas();

                    LocalDate date = LocalDate.now();

                    Document document = new Document();

                    PdfWriter writer = PdfWriter.getInstance(document,
                            new FileOutputStream(certificateNo + "_" + kursaDalibniekaVardsUnUzvards + ".pdf"));

                    document.open();

                    Image logo = Image.getInstanceFromClasspath("tdlSchool.png");
                    logo.scaleToFit(300, 150);
                    logo.setAbsolutePosition(0, 750);
                    writer.getDirectContent().addImage(logo);

                    document.add(new Paragraph("\n\n\n\n"));

                    document.add(createCenteredParagraph("TestDevLab Skola", 11.8f));
                    document.add(createCenteredParagraph("CERTIFICATE", 21.9f));
                    document.add(createCenteredParagraph("No. " + certificateNo, 11.8f));

                    Paragraph vardsUnUzvards = new Paragraph(kursaDalibniekaVardsUnUzvards,
                            getUnicodeFont(23.8f, true, new Color(52, 79, 179)));
                    vardsUnUzvards.setAlignment(Element.ALIGN_CENTER);
                    document.add(vardsUnUzvards);

                    if (hasFull) {

                        document.add(createCenteredParagraph("\nhas mastered non-formal education program", 11.8f));
                        document.add(createCenteredParagraph(kursaNosaukums, 18.1f));
                        document.add(createCenteredParagraph(stundas + " hours", 11.8f));

                        document.add(new Paragraph(
                                "\nResults the participant has achieved after completing the program " + kursaNosaukums + ":",
                                getUnicodeFont(11.8f, false, new Color(8, 20, 64))));

                        for (MacibuRezultati mr : vertejumiFromDB.getKursaDatumi().getKurss().getMacibuRezultati()) {
                            String macibuRezultats = mr.getMacibuRezultats();
                            Paragraph macRez = new Paragraph(macibuRezultats,
                                    getUnicodeFont(10, false, new Color(8, 20, 64)));
                            macRez.setIndentationLeft(30f);
                            document.add(macRez);
                        }

                        document.add(new Paragraph("\nAssessment: " + vertejums + " out of 10",
                                getUnicodeFont(16.3f, false, new Color(8, 20, 64))));

                        document.add(new Paragraph("\n\n"));

                    } else {

                        document.add(createCenteredParagraph("\nhas participated in a non-formal educational program", 11.8f));
                        document.add(createCenteredParagraph(kursaNosaukums, 18.1f));
                        document.add(createCenteredParagraph(stundas + " hours", 11.8f));
                        document.add(new Paragraph("\n\n"));
                    }

                    document.add(new Paragraph(
                            "__________________________________\n<Name Surname>\nHead of educational institution",
                            getUnicodeFont(10, false, new Color(8, 20, 64))));

                    document.add(new Paragraph(date.format(DateTimeFormatter.ofPattern("\ndd.MM.yyyy")),
                            getUnicodeFont(10, false, new Color(8, 20, 64))));

                    Paragraph regNr = new Paragraph("Registration No. 3380802314",
                            getUnicodeFont(10, false, new Color(8, 20, 64)));
                    regNr.setAlignment(Element.ALIGN_RIGHT);
                    document.add(regNr);

                    Image apaksa = Image.getInstanceFromClasspath("tdl_apaksa.png");
                    float pageWidth = document.getPageSize().getWidth();
                    apaksa.scaleToFit(pageWidth, 100f);
                    apaksa.setAbsolutePosition(0, 0);
                    writer.getDirectContent().addImage(apaksa);

                    document.close();
                }
            }

        }
    }

}
