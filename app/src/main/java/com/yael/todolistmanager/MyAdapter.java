package com.yael.todolistmanager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Collection;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nicole on 06/05/2017.
 */


public class MyAdapter extends RecyclerView.Adapter<View_Holder> {
    private ArrayList<ItemData> list;
    //Collections.emptyList();
    Context context;

    public MyAdapter(List<ItemData> list, Context context) {
        try {
            this.context = context;
            this.list = new ArrayList<ItemData>(list);
        }catch (Exception e){
            // print( e.toString());
            e.printStackTrace();
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from( parent.getContext()).inflate(R.layout.item_layout, parent, false);
        // create ViewHolder
        View_Holder viewHolder = new View_Holder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final View_Holder viewHolder, int position) {
        final int pos = position;
        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        final ItemData item = list.get( position);
        viewHolder.title.setText( item.getTitle());
        viewHolder.description.setText( item.getDescription());
        viewHolder.date_string.setText( item.getDate());

        callButtonUpdate( viewHolder, position);
        /*
        viewHolder.callBtn.setVisibility(View.GONE);
        viewHolder.callBtn.setClickable(false);
        String titleStr = item.getTitle().toLowerCase();
        if (titleStr.contains("call") || titleStr.contains("dial")) {
            viewHolder.callBtn.setVisibility(View.VISIBLE);
            viewHolder.callBtn.bringToFront();
            viewHolder.callBtn.setClickable(true);
            viewHolder.callBtn.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    taskCall( v, item.getDescription() );
                }
            });
        }
        */

        // define listener on the remove button for an item to delete it.
        viewHolder.removeBtn.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                remove( pos);
          }
        });

        viewHolder.editBtn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                promptEditItem( v, viewHolder, pos);
            }
        });

        // prevent unwanted situations
        viewHolder.cbSelect.setOnCheckedChangeListener(null);
        viewHolder.cbSelect.setChecked( item.isSelected());
        viewHolder.cbSelect.setTag(item);

        viewHolder.cbSelect.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                ItemData item = (ItemData) cb.getTag();
                item.setSelected(cb.isChecked());
                list.get(pos).setSelected(cb.isChecked());
                // Toast.makeText( v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void callButtonUpdate( final View_Holder viewHolder, int position){
        final ItemData item = getItem( position);
        viewHolder.callBtn.setVisibility(View.GONE);
        viewHolder.callBtn.setClickable(false);

        String titleStr = item.getTitle().toLowerCase();

        if (titleStr.equalsIgnoreCase("call") || titleStr.equalsIgnoreCase("dial")) {
            viewHolder.callBtn.setVisibility(View.VISIBLE);
            viewHolder.callBtn.bringToFront();
            viewHolder.callBtn.setClickable(true);
            viewHolder.callBtn.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    taskCall( v, item.getDescription() );
                }
            });
        }
    }


    /**
     * @return the number of elements the RecyclerView will display
     */
    @Override
    public int getItemCount() {
        return list.size();
    }


    /**
     * gets the message object by it's index in the list
     * @return Message - message object
     */
    public ItemData getItem( int position) {
        return this.list.get(position);
    }

    public void editItem( int position){
        notifyDataSetChanged();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public void insert( int position, ItemData data) {
        list.add( position, data);
    }

    /**
     * Insert a new item to the to do list on a predefined position
     */
    public void add(ItemData data) {
        list.add(data);
        int position = this.getItemCount();
        notifyItemInserted( position - 1);
    }


    /**
     * @return linked list of all the items in the To Do list.
     */
    public Collection<ItemData> getAllItems() {
        LinkedHashSet<ItemData> allItems = new LinkedHashSet<ItemData>(list);
        return allItems;
    }

    /**
     * remove a RecyclerView item containing a specified Data object
      */
    public void removeItem( ItemData data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    public void remove( int position){
        this.list.remove( position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }


    public void promptEditItem( View v, final View_Holder viewHolder, final int pos) {
        final ItemData item = getItem(pos);
        Context con = v.getContext();

        //get prompt_edit_item.xml_item.xml view
        LayoutInflater li = LayoutInflater.from(con);
        View promptsView = li.inflate(R.layout.prompt_edit_item, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( con);
        //set prompt_edit_item.xml_item.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setTitle("Edit Task");
        final EditText titleInput = (EditText) promptsView.findViewById(R.id.edit_title);
        final EditText descInput = (EditText) promptsView.findViewById(R.id.edit_description);
        // set input fields to the original values so user could edit
        titleInput.setText(item.getTitle());
        descInput.setText(item.getDescription());

        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("Done",
                new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int id) {
                        String newTitle = titleInput.getText().toString();
                        String newDesc = descInput.getText().toString();
                        item.setTitle( newTitle);
                        viewHolder.title.setText( newTitle);
                        item.setDescription( newDesc);
                        viewHolder.description.setText( newDesc);
                        callButtonUpdate( viewHolder, pos);
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void animate(RecyclerView.ViewHolder viewHolder) {
        final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.anticipate_overshoot_interpolator);
        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }


    private void taskCall( View view, final String phone) {
        Context con = view.getContext();
        String phone_str = phone.replaceAll("[^0-9|\\+]", "");
        Intent intent = new Intent(Intent.ACTION_DIAL); // Intent.ACTION_DIAL
        intent.setData( Uri.fromParts("tel", phone_str, null) );
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            con.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText( con, "Could not find an activity to place the call.", Toast.LENGTH_SHORT).show();
        }
    }
}