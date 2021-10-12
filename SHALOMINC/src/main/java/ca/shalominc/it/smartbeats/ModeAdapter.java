package ca.shalominc.it.smartbeats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ModeAdapter extends ArrayAdapter<ModeItem> {

    public ModeAdapter(Context context, ArrayList<ModeItem> modeList){
        super(context,0,modeList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.mode_spinner, parent,false
            );
        }

        ImageView imageViewMode =convertView.findViewById(R.id.mode_image);
        TextView textViewName = convertView.findViewById(R.id.shalom_modeTV);

        ModeItem currentItem = getItem(position);

        if(currentItem != null) {
            imageViewMode.setImageResource(currentItem.getModeImage());
            textViewName.setText(currentItem.getModeName());
        }

        return convertView;
    }
}
