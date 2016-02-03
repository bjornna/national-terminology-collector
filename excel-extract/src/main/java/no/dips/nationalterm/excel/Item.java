package no.dips.nationalterm.excel;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bna on 02.02.2016.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class Item implements UsedByForm {
    @XmlAttribute
    private String name;
    @XmlElementWrapper(name = "usedBy")

    private List<RegistryForm> usedByRegistryForm = new ArrayList<>();

    public Item() {

    }

    public Item(String name) {
        this.name = name;
    }

    public List<RegistryForm> getUsedByRegistryForm() {
        return usedByRegistryForm;
    }

    @Override
    public Item addRegistryForm(RegistryForm form) {
        if (!usedByRegistryForm.contains(form)) {
            usedByRegistryForm.add(form);
        }
        return this;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
