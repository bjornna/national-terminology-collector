package no.dips.nationalterm.excel;

/**
 * Created by bna on 02.02.2016.
 */
public class AsciiDocGenerator {

    private Report report;
    private StringBuffer buffer = new StringBuffer();

    public AsciiDocGenerator(final Report report) {

        this.report = report;
    }

    private void addRegistryForms() {
        append("== Skjema").newLine();
        for (RegistryForm form : report.getRegistryForms()) {
            append("* ").append(form.getName()).newLine();
        }
    }

    private void addVariables() {
        append("== Variables").newLine();
        for (Variable v : report.getVariables()) {
            append("* ").append(v.getName()).newLine();
            if (v.getUsedByRegistry() == null || v.getUsedByRegistry().size() > 0) {
                append("** ").append(" Used by").newLine();
                for (RegistryForm f : v.getUsedByRegistry()) {
                    append("*** ").append(f.getName()).newLine();
                }
            }
            append("** ").append("Items").newLine();
            for (Item item : v.getItems()) {
                append("*** ").append(item.getName()).newLine();
                for (RegistryForm f : item.getUsedByRegistryForm()) {
                    append("**** ").append(f.getName()).newLine();
                }


            }
        }
    }

    public String generate() {
        addHeader();
        addVariables();
        addRegistryForms();


        return buffer.toString();
    }

    private void addHeader() {
        append("= Report").newLine()
        ;
    }

    private AsciiDocGenerator append(String s) {
        buffer.append(s);
        return this;
    }

    private AsciiDocGenerator newLine() {
        buffer.append("\n\n");
        return this;
    }
}
