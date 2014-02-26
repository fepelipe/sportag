
package br.ufam.sportag.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable
{
	private int id_foursquare;
	private String nome;
	private String fotoPrefix;
	private String fotoSuffix;
	private double latitude;
	private double longitude;
	
//	private ArrayList<Evento> listaEventosConfirmados;
//	private ArrayList<Evento> listaEventosConvidado;
//	private ArrayList<Evento> listaEventosCheckin;
//
//	public ArrayList<Evento> getListaEventosConfirmados()
//	{
//		if(listaEventosConfirmados == null )
//			listaEventosConfirmados = new ArrayList<Evento>();
//		return listaEventosConfirmados;
//	}
//
//	public void setListaEventosConfirmados(ArrayList<Evento> listaEventosConfirmados)
//	{
//		this.listaEventosConfirmados = listaEventosConfirmados;
//	}
//
//	public ArrayList<Evento> getListaEventosConvidado()
//	{
//		if(listaEventosConvidado == null)
//			listaEventosConvidado = new ArrayList<Evento>();
//		return listaEventosConvidado;
//	}
//
//	public void setListaEventosConvidado(ArrayList<Evento> listaEventosConvidado)
//	{
//		this.listaEventosConvidado = listaEventosConvidado;
//	}
//
//	public ArrayList<Evento> getListaEventosCheckin()
//	{
//		if(listaEventosCheckin == null)
//			listaEventosCheckin = new ArrayList<Evento>();
//		return listaEventosCheckin;
//	}
//
//	public void setListaEventosCheckin(ArrayList<Evento> listaEventosCheckin)
//	{
//		this.listaEventosCheckin = listaEventosCheckin;
//	}

	public int getId_foursquare()
	{
		return id_foursquare;
	}
	
	public void setId_foursquare(int id_foursquare)
	{
		this.id_foursquare = id_foursquare;
	}
	
	public String getNome()
	{
		return nome;
	}
	
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	
	public String getFotoPrefix()
	{
		return fotoPrefix;
	}
	
	public void setFotoPrefix(String fotoPrefix)
	{
		this.fotoPrefix = fotoPrefix;
	}
	
	public String getFotoSuffix()
	{
		return fotoSuffix;
	}
	
	public void setFotoSuffix(String fotoSuffix)
	{
		this.fotoSuffix = fotoSuffix;
	}
	
	public double getLatitude()
	{
		return latitude;
	}
	
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}
	
	public double getLongitude()
	{
		return longitude;
	}
	
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
	
}
