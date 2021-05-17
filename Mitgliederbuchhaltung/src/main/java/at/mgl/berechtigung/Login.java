package at.mgl.berechtigung;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class Login  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject Credentials credentials;
    private User user = null;

    public void login() {
    	
    	System.out.println("test");
    	System.out.println("login " + credentials.getUsername() + " " + credentials.getPassword() + "xyz");
    	
    	if (credentials.getUsername().equals("Andreas") && credentials.getPassword().equals("Andreas") ) {
    		this.user = new User(credentials.getUsername(),credentials.getPassword());
    		System.out.println("Login erfolgreich");
    	}
    	
    }

    public void logout() {
        user = null;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    @Produces @LoggedIn User getCurrentUser() {
        if (user == null) {
            return null;
        }
        else {
            return user;
        }
    }

}
