package com.yael.todolistmanager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.CheckBox;

/**
 * Created by Nicole on 06/05/2017.
 */

//The adapters View Holder
public class View_Holder extends RecyclerView.ViewHolder {

    public CardView cv;
    public TextView title;
    public TextView description;
    public CheckBox cbSelect;
    public ImageButton removeBtn;
    public ImageButton callBtn;
    public ImageButton editBtn;
    public TextView date_string;

    View_Holder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
        cbSelect = (CheckBox) itemView.findViewById(R.id.item_comp);
        removeBtn = (ImageButton) itemView.findViewById(R.id.remove_Btn);
        editBtn = (ImageButton) itemView.findViewById(R.id.edit_Btn);
        callBtn = (ImageButton) itemView.findViewById(R.id.call_Btn);
        date_string = (TextView) itemView.findViewById(R.id.date_string);
    }

}

