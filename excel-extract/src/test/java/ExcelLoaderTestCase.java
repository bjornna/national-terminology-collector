/**
 * Created by bna on 02.02.2016.
 */

import no.dips.nationalterm.excel.ExcelLoader;
import no.dips.nationalterm.excel.Item;
import no.dips.nationalterm.excel.Report;
import no.dips.nationalterm.excel.Variable;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.fest.assertions.Assertions.*;

public class ExcelLoaderTestCase {

    private static XSSFWorkbook workbook = null;


    @BeforeClass

    public static void loadWorkBook() {

        try {
            workbook = new XSSFWorkbook(ExcelLoaderTestCase.class.getResourceAsStream("Kvalitetsregistre termer og koblinger.xlsx"));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Could not load resource: " + e.getMessage());
        }

    }

    @Test
    public void testLoadWorkBook() {
        assertThat(workbook).isNotNull();
    }

    @Test
    public void loadBook() {
        ExcelLoader loader = new ExcelLoader(workbook);
        Report variables = loader.listTabs();
        System.out.println(variables.toString()
        );
        writeReportToXml(variables);

    }

    @Test
    public void testThatPatLabItemMontebelloHasRegistryForm() {
        String varName = "01: OUS, Rikshospitalet, Montebello";
        ExcelLoader loader = new ExcelLoader(workbook);
        Report report = loader.listTabs();
        for (Variable v : report.getVariables()) {
            v.getItems().stream().filter(i -> varName.equalsIgnoreCase(i.getName())).forEach(i -> {
                assertThat(i.getUsedByRegistryForm()).isNotNull();
                assertThat(i.getUsedByRegistryForm().size()).isGreaterThan(0);
            });
        }
    }

    private void writeReportToXml(Report report) {
        try {
            JAXBContext context = JAXBContext.newInstance(Report.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(report, new FileOutputStream("report.xml"));
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
