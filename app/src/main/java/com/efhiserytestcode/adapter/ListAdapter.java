package com.efhiserytestcode.adapter;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.efhiserytestcode.R;
import com.efhiserytestcode.model.ListModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.efhiserytestcode.helper.DateHelper.getDateFormat;
import static com.efhiserytestcode.helper.NumberHelper.formatIdr;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private Activity mActivity;
    private List<ListModel> mList;

    public ListAdapter(Activity mActivity, List<ListModel> mList) {
        this.mActivity = mActivity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListModel item = mList.get(position);

        try {
            String komoditas = item.getKomoditas();
            String price = item.getPrice();
            String size = item.getSize();
            String dateParsed = item.getTglParsed();
            String provinsi = item.getAreaProvinsi();
            String kota = item.getAreaKota();

            holder.tvKomoditas.setText(komoditas);
            if (price.equals("")) {
                holder.tvPrice.setText("Harga: " + formatIdr(0));
            } else {
                holder.tvPrice.setText("Harga: " + formatIdr(Double.parseDouble(price)));
            }

            if (!dateParsed.equals("")) {
                dateParsed = getDateFormat(dateParsed);
            }

            holder.tvSize.setText("Ukuran: " + size);
            holder.tvTgl.setText(dateParsed);
            holder.tvLocation.setText(provinsi + ", " + kota);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvKomoditas, tvPrice, tvSize, tvLocation, tvTgl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvKomoditas = itemView.findViewById(R.id.tvKomoditas);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvTgl = itemView.findViewById(R.id.tvDateParsed);
        }
    }
}
