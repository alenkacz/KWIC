package GeneratorChallenge;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "Person")
public class Person implements java.io.Serializable {

    private Long id;
    private String name;
    private Date born;
    private Gender gender;

    private enum Gender {

        Male, Female
    };

    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "born", nullable = false)
    @Temporal(TemporalType.DATE)
    public Date getBorn() {
        return this.born;
    }

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Enumerated
    public Gender getGender() {
        return this.gender;
    }
}
