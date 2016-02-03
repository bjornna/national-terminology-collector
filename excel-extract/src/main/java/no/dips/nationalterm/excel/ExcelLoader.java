package no.dips.nationalterm.excel;

import org.apache.poi.ss.formula.functions.Intercept;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by bna on 02.02.2016.
 */
public class ExcelLoader {

    private XSSFWorkbook workbook;
    private String[] SHEETS_TO_SKIP = {"Forside", "Totals"};
    private List<Variable> variables = new ArrayList<>();
    private org.slf4j.Logger log = LoggerFactory.getLogger(ExcelLoader.class);
    private Variable currentVariable = null;
    private Map<String, RegistryForm> registryFormMap = new HashMap<>();

    public ExcelLoader(final XSSFWorkbook workbook) {

        this.workbook = workbook;
    }

    public Report listTabs() {
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            XSSFSheet sheet = workbook.getSheetAt(i);
            String name = sheet.getSheetName();
            if (skipSheet(name)) {
                log.info("SKIPPING sheet: " + name);
            } else {
                workonSheet(sheet);
            }

        }
        Report report = new Report();
        report.setVariables(variables);
        report.setRegistryForms(registryFormMap.values());
        return report;
    }

    private void workonSheet(XSSFSheet sheet) {
        XSSFRow firstRow = sheet.getRow(sheet.getFirstRowNum());
        boolean collectingRegistryForm = false;
        Map<Integer, RegistryForm> registryColIndex = new HashMap<>();
        for (Iterator<Cell> cit = firstRow.cellIterator(); cit.hasNext(); ) {
            Cell cel = cit.next();
            if (cel.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                String name = cel.getStringCellValue();
                if (name.equalsIgnoreCase("NPR")) {
                    collectingRegistryForm = true;

                }
                if (collectingRegistryForm) {
                    RegistryForm form = new RegistryForm(name);
                    registryColIndex.put(cel.getColumnIndex(), form);
                    if (registryFormMap.containsKey(name)) {
                        log.info("Skipping:" + name + " - already exist");
                    } else {
                        log.info("Adding:" + name);
                        registryFormMap.put(name, form);

                    }
                }
            }
        }


        for (Iterator rit = sheet.rowIterator(); rit.hasNext(); ) {
            XSSFRow row = (XSSFRow) rit.next();
            // log.info(row.getLastCellNum() + "");
            ResourceType type = rowType(row);
            String nameOfRow = getNameOfRow(row);
            if (nameOfRow.equalsIgnoreCase("Patologilaboratorium")) {
                log.info("jjj");
            }
            log.info("Row type is:" + type + " " + nameOfRow);
            if (type.equals(ResourceType.VARIABLE)) {
                if (currentVariable != null) {
                    variables.add(currentVariable);
                }
                currentVariable = new Variable(getNameOfRow(row));
                log.info(" New variable:" + currentVariable);
                addRegistryFormToRow(row, registryColIndex, currentVariable);
            } else if (type.equals(ResourceType.ITEM)) {
                if (currentVariable != null) {
                    Item item = new Item(getNameOfRow(row));
                    addRegistryFormToRow(row, registryColIndex, item);
                    log.info("  Adding item: " + item);
                    currentVariable.addItem(item);
                }
            }


        }
        // sheet is finished - adding current variable and clear
        if (currentVariable != null) {
            variables.add(currentVariable);
            currentVariable = null;
        }
    }

    private void addRegistryFormToRow(XSSFRow row, Map<Integer, RegistryForm> registryColIndex, UsedByForm item) {
        for (Iterator<Cell> cit = row.cellIterator(); cit.hasNext(); ) {
            Cell cell = cit.next();
            if (!cellIsEmpty(cell)) {
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    String value = cell.getStringCellValue();
                    int coldIndex = cell.getColumnIndex();
                    RegistryForm name = registryColIndex.get(coldIndex);
                    if (name != null) {
                        item.addRegistryForm(name);
                    }
                }
            }
        }
    }

    private boolean cellIsEmpty(Cell c) {
        return c == null || c.getCellType() == Cell.CELL_TYPE_BLANK;
    }

    private String getNameOfRow(XSSFRow row) {
        XSSFCell cel = row.getCell(row.getFirstCellNum());
        if (cel.getCellType() == XSSFCell.CELL_TYPE_STRING) {
            return cel.getStringCellValue();
        } else {
            log.warn("Cell is not of type string");
            return "Unknown..";
        }
    }

    private boolean skipSheet(String name) {
        for (String s : SHEETS_TO_SKIP) {
            if (s.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    private ResourceType rowType(XSSFRow row) {
        if (row.getRowNum() < 6) {
            return ResourceType.UNKNOWN;
        }
        if (!cellIsEmpty(row.getCell(1))) {
            // must be variable ??
            return ResourceType.VARIABLE;
        }
        if (!cellIsEmpty(row.getCell(3))) {
            // must be item
            return ResourceType.ITEM;
        }
        for (Iterator<Cell> cit = row.cellIterator(); cit.hasNext(); ) {
            Cell c = cit.next();
            if (c.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                String content = c.getStringCellValue();
                if (content.contains("#")) {
                    return ResourceType.VARIABLE;
                } else if (content.contains("x")) {
                    return ResourceType.ITEM;
                }
            }
        }
        return ResourceType.UNKNOWN;
    }
}
