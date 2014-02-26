
package br.ufam.sportag.model;

import java.io.Serializable;

public class Esporte implements Serializable
{
	private int id;
	private String nome;
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getNome()
	{
		return nome;
	}
	
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	
}
