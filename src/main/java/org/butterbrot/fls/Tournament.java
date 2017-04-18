package org.butterbrot.fls;

import org.springframework.util.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tournament")
public class Tournament {

    private int id;

    private String name;

    private int season;

    private Tournament() {
        // jaxb
    }

    public Tournament(int id, String name, int season) {
        this.id = id;
        this.name = name;
        this.season = season;
    }

    @XmlAttribute(name="id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = StringUtils.trimWhitespace(name);
    }

    @XmlElement(name = "season")
    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Tournament that = (Tournament) o;

        if (id != that.id)
            return false;
        if (season != that.season)
            return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + season;
        return result;
    }
}
