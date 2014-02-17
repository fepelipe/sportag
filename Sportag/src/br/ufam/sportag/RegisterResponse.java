package br.ufam.sportag;

public interface RegisterResponse {
	void tokenReceived(String token);
	void registerSuccess(String userFirstName);
}
