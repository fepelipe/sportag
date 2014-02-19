
package br.ufam.sportag.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class AdapterListView<T> extends BaseAdapter
{
	private LayoutInflater mInflater;
	private ArrayList<T> itens;
	private int idListView;
	
	public AdapterListView(Context context, int idItemListView,
			ArrayList<T> source)
	{
		this.itens = source;
		this.idListView = idItemListView;
		mInflater = LayoutInflater.from(context);
	}
	
	public int getCount()
	{
		return itens.size();
	}
	
	public T getItem(int position)
	{
		return itens.get(position);
	}
	
	public long getItemId(int position)
	{
		return position;
	}
	
	public View getView(int position, View view, ViewGroup parent)
	{
		T item = itens.get(position);
		view = mInflater.inflate(idListView, null);
		setItemContentLayout(view, item);
		return view;
	}
	
	public abstract void setItemContentLayout(View view, T item);
}