package no.dips.nationalterm.excel;

/**
 * Created by bna on 03.02.2016.
 */
public class CsvGenerator {

    private Report report;
    private StringBuffer buffer = new StringBuffer();

    public CsvGenerator(final Report report) {

        this.report = report;
    }

    public String generate() {
        appendCell("Registry").appendCell("Item").appendCell("Variable").newLine();
        for (Variable v : report.getVariables()) {

            for (Item i : v.getItems()) {
                for (RegistryForm rf : i.getUsedByRegistryForm()) {
                    appendCell(rf.getName()).appendCell(i.getName()).appendCell(v.getName()).newLine();
                }
            }
        }
        return buffer.toString();
    }

    private CsvGenerator appendCell(String s) {
        return append(s + ";");
    }

    private CsvGenerator append(String s) {
        buffer.append(s);
        return this;
    }

    private CsvGenerator newLine() {
        buffer.append("\n");
        return this;
    }
}
