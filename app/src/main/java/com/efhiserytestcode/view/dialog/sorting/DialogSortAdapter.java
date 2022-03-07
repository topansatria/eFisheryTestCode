package com.efhiserytestcode.view.dialog.sorting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efhiserytestcode.R;
import com.efhiserytestcode.model.MenuModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DialogSortAdapter extends RecyclerView.Adapter<DialogSortAdapter.ViewHolder> {

    private Context context;
    private static ClickListener clickListener;

    private ArrayList<MenuModel> mList;

    public DialogSortAdapter(Context context, ArrayList<MenuModel> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_sorting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        final MenuModel item = mList.get(position);
        holder.tvTitle.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle;

        private ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        DialogSortAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}
