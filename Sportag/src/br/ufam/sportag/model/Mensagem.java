
package br.ufam.sportag.model;

import java.util.Date;

public class Mensagem
{
	private int id;
	private Date dataHora;
	private String conteudo;
	private Usuario usuario;
	
	public Usuario getUsuario()
	{
		return usuario;
	}
	
	public void setUsuario(Usuario usuario)
	{
		this.usuario = usuario;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public Date getDataHora()
	{
		return dataHora;
	}
	
	public void setDataHora(Date dataHora)
	{
		this.dataHora = dataHora;
	}
	
	public String getConteudo()
	{
		return conteudo;
	}
	
	public void setConteudo(String conteudo)
	{
		this.conteudo = conteudo;
	}
	
}
