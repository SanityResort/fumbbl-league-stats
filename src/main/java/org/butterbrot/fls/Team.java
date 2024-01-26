package org.butterbrot.fls;

import org.springframework.util.StringUtils;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name="team")
public class Team {

    private int id;
    private String name;
    private String coachName;
    private String groupName;

    @XmlAttribute(name="id")
    public int getId() {
        return id;
    }

    @XmlElement(name="name")
    public String getName() {
        return name;
    }

    @XmlElement(name="coach")
    public String getCoachName() {
        return coachName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name =  StringUtils.trimWhitespace(name);
    }

    public void setGroupName(String groupName) {
        this.groupName =  StringUtils.trimWhitespace(groupName);
    }

    @SuppressWarnings("unused")
    private Team() {
        // jaxb
    }

    public Team(int id, String name, String coachName, String groupName) {
        this.id = id;
        this.name = name;
        this.coachName = coachName;
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        if (id != team.id) return false;
        if (!Objects.equals(name, team.name)) return false;
        if (!Objects.equals(coachName, team.coachName)) return false;
        return Objects.equals(groupName, team.groupName);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (coachName != null ? coachName.hashCode() : 0);
        result = 31 * result + (groupName != null ? groupName.hashCode() : 0);
        return result;
    }
}
