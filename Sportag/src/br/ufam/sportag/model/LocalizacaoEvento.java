
package br.ufam.sportag.model;

public class LocalizacaoEvento
{
	private int id;
	private double latitude;
	private double longitude;
	private String endereco;
	private String nomeLocal;
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
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
	
	public String getEndereco()
	{
		return endereco;
	}
	
	public void setEndereco(String endereco)
	{
		this.endereco = endereco;
	}
	
	public String getNomeLocal()
	{
		return nomeLocal;
	}
	
	public void setNomeLocal(String nomeLocal)
	{
		this.nomeLocal = nomeLocal;
	}
	
}
