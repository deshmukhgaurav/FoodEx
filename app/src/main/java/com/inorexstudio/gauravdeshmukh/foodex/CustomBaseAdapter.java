package com.inorexstudio.gauravdeshmukh.foodex;

/**
 * Created by gauravdeshmukh on 3/23/16.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomBaseAdapter extends BaseAdapter {
    private static ArrayList<SearchResults> searchArrayList;

    private LayoutInflater mInflater;

    public CustomBaseAdapter(Context context, ArrayList<SearchResults> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_card, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.name);
            holder.txtCityState = (TextView) convertView
                    .findViewById(R.id.cityState);
            holder.txtCuisine = (TextView) convertView.findViewById(R.id.phone);
            holder.txtRating = (TextView) convertView.findViewById(R.id.rating);
            holder.icon = (ImageView) convertView.findViewById(R.id.imageView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(searchArrayList.get(position).getName());
        holder.txtCityState.setText(searchArrayList.get(position)
                .getCityState());
        holder.txtCuisine.setText("Cuisine > " + searchArrayList.get(position).getCuisine());
        if(searchArrayList.get(position).getRating()!=null) {
            holder.txtRating.setText("" + searchArrayList.get(position).getRating());
        }
        else{
            holder.txtRating.setText("-");
        }
        switch (searchArrayList.get(position).getTopCuisine()){
            case "Cafe":
                holder.icon.setImageResource(R.drawable.ic_cafe);
                break;
            case "American":
                holder.icon.setImageResource(R.drawable.ic_american);
                break;
            case "Pizza":
                holder.icon.setImageResource(R.drawable.ic_pizza);
                break;
            case "Sandwiches":
                holder.icon.setImageResource(R.drawable.ic_sandwich);
                break;
            case "Pub Food":
                holder.icon.setImageResource(R.drawable.ic_beer);
                break;
            case "Mexican":
                holder.icon.setImageResource(R.drawable.ic_mexican);
                break;
            case "Grill":
                holder.icon.setImageResource(R.drawable.ic_barbecue);
                break;
            case "Asian":
                holder.icon.setImageResource(R.drawable.ic_asian);
                break;
            case "Italian":
                holder.icon.setImageResource(R.drawable.ic_pizza);
                break;
            case "Burgers":
                holder.icon.setImageResource(R.drawable.ic_american);
                break;
            case "Bakery":
                holder.icon.setImageResource(R.drawable.ic_bakery);
                break;
            case "Subs":
                holder.icon.setImageResource(R.drawable.ic_sandwich);
                break;
            case "Coffee":
                holder.icon.setImageResource(R.drawable.ic_cafe);
                break;
            case "Fast Food":
                holder.icon.setImageResource(R.drawable.ic_american);
                break;
            case "Chinese":
                holder.icon.setImageResource(R.drawable.ic_chinese);
                break;
            case "Ice Cream":
                holder.icon.setImageResource(R.drawable.ic_ice_cream);
                break;
            case "Barbecue":
                holder.icon.setImageResource(R.drawable.ic_barbecue);
                break;
            case "Smoothies":
                holder.icon.setImageResource(R.drawable.ic_smoothies);
                break;
            case "Sushi":
                holder.icon.setImageResource(R.drawable.ic_sushi);
                break;
            case "Greek":
                holder.icon.setImageResource(R.drawable.ic_greek);
                break;
            case "French":
                holder.icon.setImageResource(R.drawable.ic_french);
                break;
            case "Donuts":
                holder.icon.setImageResource(R.drawable.ic_donut);
                break;
            default:
                holder.icon.setImageResource(R.drawable.ic_fork_knife);
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        TextView txtName;
        TextView txtCityState;
        TextView txtCuisine;
        TextView txtRating;
        ImageView icon;
    }
}