package nl.fontys.kwetter.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@Data
@Entity
public class Kwetter implements Serializable, Comparable<Kwetter> {

    @Id
    @Type(type = "uuid-char")
    @Column(name = "uuid", updatable = false, nullable = false, unique = true, columnDefinition = "varchar(64)")
    private UUID uuid = UUID.randomUUID();

    @Size(max = 140)
    @NotNull
    private String text;

    private int reports;

    private int hearts;

    @ElementCollection
    private Set<String> tags;

    @ElementCollection
    private Set<User> mentions;

    @JsonIgnoreProperties({"createdKwetters", "reportedKwetters", "heartedKwetters"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pla_fk_n_userId")
    private User owner;

    @NotNull
    private Date dateTime;

    public Kwetter() {
        this.dateTime = new Date();
        this.text = "";
        this.tags = new HashSet<>();
        this.mentions = new HashSet<>();
    }

    public Kwetter(Kwetter toBeClonedKwetter) {
        this.uuid = toBeClonedKwetter.getUuid();
        this.text = toBeClonedKwetter.getText();
        this.reports = toBeClonedKwetter.getReports();
        this.hearts = toBeClonedKwetter.getHearts();
        this.tags = toBeClonedKwetter.getTags();
        this.mentions = toBeClonedKwetter.getMentions();
        this.owner = toBeClonedKwetter.getOwner();
        this.dateTime = toBeClonedKwetter.getDateTime();
    }

    public Kwetter(String text, User owner, Date dateTime) {
        this(text, new HashSet<>(), new HashSet<>(), owner, dateTime);
    }

    public Kwetter(String text, Set<String> tags, Set<User> mentions, User owner, Date dateTime) {
        this.text = text;
        this.owner = owner;
        this.dateTime = dateTime;

        if (tags != null) {
            this.tags = tags;
        } else {
            this.tags = new HashSet<>();
        }

        if (mentions != null) {
            this.mentions = mentions;
        } else {
            this.mentions = new HashSet<>();
        }

        this.reports = 0;
        this.hearts = 0;
        owner.addCreatedKwetter(this);
    }

    public void report() {
        reports += 1;
    }

    public void heart() {
        hearts += 1;
    }

    public void removeReport() {
        reports -= 1;
    }

    public void removeHeart() {
        hearts -= 1;
    }

    public void removeOwner() {
        owner = null;
    }

    @Override
    public int compareTo(Kwetter kwetter) {
        return getDateTime().compareTo(kwetter.getDateTime());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Kwetter)) {
            return false;
        }
        Kwetter kwetter = (Kwetter) o;
        return uuid == kwetter.getUuid();
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
