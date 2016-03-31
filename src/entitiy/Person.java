package entitiy;

import com.j256.ormlite.field.DatabaseField;

public class Person {
	
	@DatabaseField(generatedId = true)
	int id;
	
	@DatabaseField
	String username;
	
	@DatabaseField
	String password;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=").append(id);
		sb.append(", ").append("username=").append(username);
		return sb.toString();
	}

}
