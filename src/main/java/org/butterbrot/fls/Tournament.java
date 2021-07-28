package org.butterbrot.fls;

import org.springframework.util.StringUtils;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tournament")
public class Tournament implements Comparable<Tournament> {

    private int id;

    private String name;

    private int season;

    private String groupName;
    private int groupId;

    private Tournament() {
        // jaxb
    }

    public Tournament(int id, String name, int season, String groupName, int groupId) {
        this.id = id;
        this.name = name;
        this.season = season;
        this.groupName = groupName;
        this.groupId = groupId;
    }

    @XmlAttribute(name = "id")
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tournament that = (Tournament) o;

        if (id != that.id) return false;
        if (season != that.season) return false;
        if (groupId != that.groupId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return groupName != null ? groupName.equals(that.groupName) : that.groupName == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + season;
        result = 31 * result + (groupName != null ? groupName.hashCode() : 0);
        result = 31 * result + groupId;
        return result;
    }

    @Override
    public int compareTo(Tournament o) {
        if (o != null) {
            return Integer.compare(o.getId(), this.getId());
        }
        return -1;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName =  StringUtils.trimWhitespace(groupName);
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
