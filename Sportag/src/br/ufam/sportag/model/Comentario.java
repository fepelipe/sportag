
package br.ufam.sportag.model;

import java.util.Date;

public class Comentario
{
	private int id;
	private Date dateTime;
	private String conteudo;
	private Evento evento;
	private Usuario usuario;
	
	public Date getDateTime()
	{
		return dateTime;
	}
	
	public void setDateTime(Date dateTime)
	{
		this.dateTime = dateTime;
	}
	
	public String getConteudo()
	{
		return conteudo;
	}
	
	public void setConteudo(String conteudo)
	{
		this.conteudo = conteudo;
	}
	
	public Usuario getUsuario()
	{
		return usuario;
	}
	
	public void setUsuario(Usuario usuario)
	{
		this.usuario = usuario;
	}
	
	public Evento getEvento()
	{
		return evento;
	}
	
	public void setEvento(Evento evento)
	{
		this.evento = evento;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
	
}
