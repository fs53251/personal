package progi.projekt.backend.model.classes.Klijent;

public class LoginKlijent{
	private String username;
	private String password;
	public LoginKlijent(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public LoginKlijent() {
		
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
}