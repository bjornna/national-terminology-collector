package no.dips.nationalterm.excel;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bna on 02.02.2016.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Variable implements UsedByForm {

    @XmlAttribute
    private String name;
    private List<Item> items = new ArrayList<>();
    @XmlElementWrapper(name = "usedByRegistryForm")
    private List<RegistryForm> usedByRegistry = new ArrayList<>();

    public Variable() {

    }

    public Variable(String name) {
        this.name = name;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<RegistryForm> getUsedByRegistry() {
        return usedByRegistry;
    }

    @Override
    public Variable addRegistryForm(RegistryForm form) {
        if (!usedByRegistry.contains(form)) {
            usedByRegistry.add(form);
        }
        return this;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                '}';
    }

    public Variable addItem(Item item) {
        items.add(item);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
