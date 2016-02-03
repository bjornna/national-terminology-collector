package no.dips.nationalterm.excel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by bna on 02.02.2016.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)

public class RegistryForm {

    @XmlAttribute
    private String name;

    public RegistryForm() {

    }

    public RegistryForm(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistryForm that = (RegistryForm) o;

        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RegistryForm{" +
                "name='" + name + '\'' +
                '}';
    }
}
