
package br.ufam.sportag.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Chat implements Serializable
{
	private int id;
	private Usuario usuarioRemetente;
	private Usuario usuarioDestinatario;
	private ArrayList<Mensagem> listaMensagem;
	
	public ArrayList<Mensagem> getListaMensagem()
	{
		if(listaMensagem == null)
			listaMensagem = new ArrayList<Mensagem>();
		return listaMensagem;
	}

	public void setListaMensagem(ArrayList<Mensagem> listaMensagem)
	{
		this.listaMensagem = listaMensagem;
	}

	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public Usuario getUsuarioRemetente()
	{
		return usuarioRemetente;
	}
	
	public void setUsuarioRemetente(Usuario usuarioRemetente)
	{
		this.usuarioRemetente = usuarioRemetente;
	}
	
	public Usuario getUsuarioDestinatario()
	{
		return usuarioDestinatario;
	}
	
	public void setUsuarioDestinatario(Usuario usuarioDestinatario)
	{
		this.usuarioDestinatario = usuarioDestinatario;
	}
	
}
