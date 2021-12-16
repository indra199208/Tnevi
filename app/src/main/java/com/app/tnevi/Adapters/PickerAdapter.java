package com.app.tnevi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.tnevi.R;

import androidx.recyclerview.widget.RecyclerView;




import java.util.List;

public class PickerAdapter extends RecyclerView.Adapter<PickerAdapter.TextVH> {

    private Context context;
    public List<String> dataList;
    private RecyclerView recyclerView;

    public PickerAdapter(Context context, List<String> dataList, RecyclerView recyclerView) {
        this.context = context;
        this.dataList = dataList;
        this.recyclerView = recyclerView;
    }

    @Override
    public TextVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.rv_epoints, parent, false);
        return new TextVH(view);
    }

    @Override
    public void onBindViewHolder(TextVH holder, final int position) {
        holder.itemView.setTag(position);
        TextVH textVH = holder;
        textVH.tvEpointsvalue.setText("$"+dataList.get(position));

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void swapData(List<String> newData) {
        dataList = newData;
        notifyDataSetChanged();
    }

    class TextVH extends RecyclerView.ViewHolder {
        TextView tvEpointsvalue, tvEpoints;

        public TextVH(View itemView) {
            super(itemView);
            tvEpointsvalue = (TextView) itemView.findViewById(R.id.tvEpointsvalue);
        }
    }
}
