/**
 * Created by bna on 02.02.2016.
 */

import no.dips.nationalterm.excel.AsciiDocGenerator;
import no.dips.nationalterm.excel.CsvGenerator;
import no.dips.nationalterm.excel.RegistryForm;
import no.dips.nationalterm.excel.Report;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.fest.assertions.Assertions.*;

public class AsciidocGeneratorTestCase {

    private Report report = null;

    @Before
    public void before() {
        try {
            JAXBContext context = JAXBContext.newInstance(Report.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            report = (Report) unmarshaller.unmarshal(new File("report.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGenerateCsv() {
        CsvGenerator generator = new CsvGenerator(report);
        String content = generator.generate();
        try {
            FileWriter writer = new FileWriter(new File("report.csv"));
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGEnerateAsciido() {
        AsciiDocGenerator generator = new AsciiDocGenerator(report);
        String ascii = generator.generate();
        try {
            FileWriter writer = new FileWriter(new File("report.adoc"));
            writer.write(ascii);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
