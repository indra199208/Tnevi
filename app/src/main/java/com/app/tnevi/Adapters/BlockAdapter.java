package com.app.tnevi.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tnevi.R;
import com.app.tnevi.Utils.DatabaseHelper;

import com.app.tnevi.model.BlockModel;

import java.util.ArrayList;

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.viewHolder> {

    Context context;
    Activity activity;
    ArrayList<BlockModel> arrayList;
    DatabaseHelper database_helper;

    public BlockAdapter(Context context, Activity activity, ArrayList<BlockModel> arrayList) {
        this.context = context;
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public BlockAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_seat, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockAdapter.viewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvBlockname.setText(arrayList.get(position).getBlockname());
        holder.tvNumberseats.setText(arrayList.get(position).getNumberofseats());
        holder.tvPriceperseat.setText(arrayList.get(position).getSeatprice());
        holder.tvNumberofrows.setText(arrayList.get(position).getNumberofrows());

        database_helper = new DatabaseHelper(context);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                //deleting note
                database_helper.delete(arrayList.get(position).getId());
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                //display edit dialog
                showDialog(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView tvBlockname, tvNumberofrows, tvNumberseats, tvPriceperseat;
        ImageView btnEdit, btnDelete;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvBlockname = itemView.findViewById(R.id.tvBlockname);
            tvNumberofrows = itemView.findViewById(R.id.tvNumberofrows);
            tvNumberseats = itemView.findViewById(R.id.tvNumberseats);
            tvPriceperseat = itemView.findViewById(R.id.tvPriceperseat);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }


    public void showDialog(final int pos) {
        final EditText etBlockname, etNumberofrows, etNoseats, etSeatprice;
        Button submit;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dailouge);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        etBlockname = (EditText) dialog.findViewById(R.id.etBlockname);
        etNumberofrows = (EditText) dialog.findViewById(R.id.etNumberofrows);
        etNoseats = (EditText) dialog.findViewById(R.id.etNoseats);
        etSeatprice = (EditText) dialog.findViewById(R.id.etSeatprice);

        submit = (Button) dialog.findViewById(R.id.submit);

        etBlockname.setText(arrayList.get(pos).getBlockname());
        etNumberofrows.setText(arrayList.get(pos).getNumberofrows());
        etNoseats.setText(arrayList.get(pos).getNumberofseats());
        etSeatprice.setText(arrayList.get(pos).getSeatprice());


        submit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                if (etBlockname.getText().toString().isEmpty()) {
                    etBlockname.setError("Please Enter Block name");
                }else if(etNumberofrows.getText().toString().isEmpty()) {
                    etNumberofrows.setError("Please Enter Rows");
                }else if (etNoseats.getText().toString().isEmpty()){
                    etNoseats.setError("Please Enter Seats");
                }else if (etSeatprice.getText().toString().isEmpty()){
                    etSeatprice.setError("Please enter seats");
                } else {
                    //updating note
                    database_helper.updateNote(etBlockname.getText().toString(), etNumberofrows.getText().toString(), etNoseats.getText().toString(), etSeatprice.getText().toString(), arrayList.get(pos).getId());
                    arrayList.get(pos).setBlockname(etBlockname.getText().toString());
                    arrayList.get(pos).setNumberofrows(etNumberofrows.getText().toString());
                    arrayList.get(pos).setNumberofseats(etNoseats.getText().toString());
                    arrayList.get(pos).setSeatprice(etSeatprice.getText().toString());
                    dialog.cancel();
                    //notify list
                    notifyDataSetChanged();
                }
            }
        });
    }
}
