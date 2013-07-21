package com.dephi.bosojowo;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Supports the ListActivity to displays the ListViews
 */
public class AdapterList extends BaseAdapter {
	public enum AIM {
		Category, Post, Detail,
	}

	private Activity mActivity;
	private ArrayList<HashMap<String, String>> mData;
	private LayoutInflater mInflater = null;
	private AIM mAim;

	public AdapterList(Activity activity, ArrayList<HashMap<String, String>> data, AIM aim) {
		mActivity = activity;
		mData = data;
		mInflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mAim = aim;
	}

	public int getCount() {
		return mData.size();
	}

	public Object getItem(int position) {
		return position;
	}
	
	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public int getItemDBid(int position) {
		String id = mData.get(position).get("_id");
		return Integer.parseInt(id);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		HashMap<String, String> current = new HashMap<String, String>();
		current = mData.get(position);

		switch (mAim) {
		case Category:
			vi = mInflater.inflate(R.layout.list_category, null);
			TextView categoryName = (TextView) vi
					.findViewById(R.id.categoryName);

			categoryName.setText(current.get("seekThis"));
			break;
		case Post:
			vi = mInflater.inflate(R.layout.list_post, null);
			Utilities.arrangeItems(vi, current);
			break;
		case Detail:
			vi = mInflater.inflate(R.layout.list_detail, null);
			Utilities.arrangeItems(vi, current);
			break;
		default:
			break;
		}
		return vi;
	}

	// Filter Class
	public void filter(ArrayList<HashMap<String, String>> resultDB) {
		mData.clear();
		Utilities.copyToNewList(resultDB, mData);
		mAim = AIM.Post;
		notifyDataSetChanged();
	}
	
	public void refresh(int IDtoRefresh){
		mData.clear();
		mData = new DatabaseHelper(mActivity).getPostsBySubCat(IDtoRefresh);
		notifyDataSetChanged();
	}
}