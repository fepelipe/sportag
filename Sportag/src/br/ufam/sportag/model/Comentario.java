
package br.ufam.sportag.model;

import java.io.Serializable;
import java.util.Date;

public class Comentario implements Serializable
{
	private int id;
	private Date dataHora;
	private String conteudo;
	private Evento evento;
	private Usuario usuario;
	
	public Date getDataHora()
	{
		return dataHora;
	}
	
	public void setDataHora(Date dateTime)
	{
		this.dataHora = dateTime;
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
