
package br.ufam.sportag.model;

import java.io.Serializable;
import java.util.Date;

public class Evento implements Serializable
{
	private int id;
	private String nome;
	private boolean visivel;
	private Esporte esporte;
	private Date dataHora;

	private LocalizacaoEvento localizacaoEvento;
	private Usuario criador;
	
//	private ArrayList<Mensagem> listaComentarios;
	
//	public ArrayList<Mensagem> getListaMensagens()
//	{
//		if(listaComentarios == null)
//			listaComentarios = new ArrayList<Mensagem>();
//		return listaComentarios;
//	}

//	public void setListaMensagens(ArrayList<Mensagem> listaMensagens)
//	{
//		this.listaComentarios = listaMensagens;
//	}

	public Date getDataHora()
	{
		return dataHora;
	}

	public void setDataHora(Date dataHora)
	{
		this.dataHora = dataHora;
	}
	
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
	
	public boolean isVisivel()
	{
		return visivel;
	}
	
	public void setVisivel(boolean visivel)
	{
		this.visivel = visivel;
	}
	
	public LocalizacaoEvento getLocalizacaoEvento()
	{
		return localizacaoEvento;
	}
	
	public void setLocalizacaoEvento(LocalizacaoEvento localizacaoEvento)
	{
		this.localizacaoEvento = localizacaoEvento;
	}
	
	public Usuario getCriador()
	{
		return criador;
	}
	
	public void setCriador(Usuario criador)
	{
		this.criador = criador;
	}
	
	public Esporte getEsporte()
	{
		return esporte;
	}
	
	public void setEsporte(Esporte esporte)
	{
		this.esporte = esporte;
	}
	
}
