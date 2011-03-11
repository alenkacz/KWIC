package GeneratorChallenge;

import java.util.Date;

import javax.persistence.*;

@Entity 
@Table(name = "Person")
public class Person implements java.io.Serializable {
	private String name; 
	private Date born;
	
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
}
