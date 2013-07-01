package com.dephi.bosojowo;

import java.util.ArrayList;
import java.util.HashMap;
 
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Supports the ListActivity to displays the ListViews
 */
public class AdapterList extends BaseAdapter {
	public enum AIM{
		Category,
		Post,
		Detail,
	}
	
    private Activity _activity;
    private ArrayList<HashMap<String, String>> _data;
    private static LayoutInflater inflater=null;
    private AIM _aim;
 
    public AdapterList(Activity a, ArrayList<HashMap<String, String>> d, AIM aim) {
        _activity = a;
        _data=d;
        inflater = (LayoutInflater)_activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _aim = aim;
    }
 
    public int getCount() {
        return _data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        HashMap<String, String> current = new HashMap<String, String>();
        current = _data.get(position);
        
        switch (_aim) {
		case Category:
			vi = inflater.inflate(R.layout.list_category, null);
			TextView categoryName = (TextView)vi.findViewById(R.id.categoryName);

	        categoryName.setText(current.get("seekThis"));
			break;
		case Post:
			vi = inflater.inflate(R.layout.list_post, null);
			arrangeItems(vi, current);
			break;
		case Detail:
			vi = inflater.inflate(R.layout.list_detail, null);
			arrangeItems(vi, current);
			break;
		default:
			break;
		}
        return vi;
    }
    
    private static void arrangeItems(View vi, HashMap<String, String> current){
    	
    	ImageView img = (ImageView)vi.findViewById(R.id.picture);
		TextView postOne = (TextView)vi.findViewById(R.id.postOne);
		TextView postTwo = (TextView)vi.findViewById(R.id.postTwo);
		
		postOne.setText(current.get("postOne"));
		postTwo.setText(current.get("postTwo"));
		String picture = current.get("picture");

        if(picture.equals("dangu"))img.setImageResource(R.drawable.dangu); 
        else if(picture.equals("ontong"))img.setImageResource(R.drawable.ontong);   
        else if(picture.equals("hanacaraka"))img.setImageResource(R.drawable.hanan);
        else if(picture.equals("abimanyu"))img.setImageResource(R.drawable.abimanyu);
        else if(picture.equals("anoman"))img.setImageResource(R.drawable.anoman);
        else if(picture.equals("aswotomo"))img.setImageResource(R.drawable.aswotomo);
        else if(picture.equals("dursasana"))img.setImageResource(R.drawable.dursasana);
        else if(picture.equals("gatotkaca"))img.setImageResource(R.drawable.gatotkaca);
        else if(picture.equals("irawan"))img.setImageResource(R.drawable.irawan);
        else if(picture.equals("janaka"))img.setImageResource(R.drawable.janaka);
        else if(picture.equals("lesmana"))img.setImageResource(R.drawable.lesmana);
        else if(picture.equals("nakula"))img.setImageResource(R.drawable.nakula);
        else if(picture.equals("ontorejo"))img.setImageResource(R.drawable.antareja);
        else if(picture.equals("ontoseno"))img.setImageResource(R.drawable.antasena);
        else if(picture.equals("kartamarma"))img.setImageResource(R.drawable.kartamarma);
        else if(picture.equals("sadewa"))img.setImageResource(R.drawable.sadewa);
        else if(picture.equals("setyaka"))img.setImageResource(R.drawable.setyaka);
        else if(picture.equals("setyaki"))img.setImageResource(R.drawable.setyaki);
        else if(picture.equals("samba"))img.setImageResource(R.drawable.samba);
        else if(picture.equals("sengkuni"))img.setImageResource(R.drawable.sengkuni);
        else if(picture.equals("udawa"))img.setImageResource(R.drawable.udawa);
        else if(picture.equals("werkudara"))img.setImageResource(R.drawable.werkudara);
        else if(picture.equals("asem"))img.setImageResource(R.drawable.asem);
        else if(picture.equals("jambu"))img.setImageResource(R.drawable.jambu);
        else img.setImageResource(R.drawable.default_pic);
    }
}