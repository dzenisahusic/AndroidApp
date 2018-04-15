package com.comtrade.edit2014sales;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.comtrade.edit2014salesDzenisa.R;

public class ListViewCustomAdapter extends BaseAdapter
{
	public List<String >title;
	public List<String >description;
	public Activity context;
	public LayoutInflater inflater;

	public ListViewCustomAdapter(Activity context,List<String > title, List<String > description) {
		super();

		this.context = context;
		this.title = title;
		this.description = description;

	    this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return title.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static class ViewHolder
	{
		
		TextView txtViewTitle;
		TextView txtViewDescription;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.racunlistitem, null);

			
			holder.txtViewTitle = (TextView) convertView.findViewById(R.id.artikal_t);
			holder.txtViewDescription = (TextView) convertView.findViewById(R.id.artikal_t2);

			convertView.setTag(holder);
		}
		else
			holder=(ViewHolder)convertView.getTag();

		
		holder.txtViewTitle.setText(title.get(position));
		holder.txtViewDescription.setText(description.get(position));

		return convertView;
	}

	

}
