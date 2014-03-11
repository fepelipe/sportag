package br.ufam.sportag.util;

import br.ufam.sportag.model.Usuario;

public interface RegisterResponse {
	void tokenReceived(String token);
	void registerSuccess(String userFirstName);
	void registerSuccess(Usuario usuario);
}
