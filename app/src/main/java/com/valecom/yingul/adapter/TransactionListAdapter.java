package com.valecom.yingul.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.valecom.yingul.R;
import com.valecom.yingul.model.Yng_Transaction;

import java.util.ArrayList;

/**
 * Created by gonzalo on 12-06-18.
 */

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.ItemRowHolder> {
    private ArrayList<Yng_Transaction> dataList;
    private Context mContext;

    public TransactionListAdapter(Context context, ArrayList<Yng_Transaction> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public TransactionListAdapter.ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transaction_detail, parent, false);
        return new TransactionListAdapter.ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(TransactionListAdapter.ItemRowHolder holder, final int position) {
        final Yng_Transaction item = dataList.get(position);

        holder.textNumber.setText(item.getTransactionId().toString());
        holder.textDate.setText(item.getDay()+"/"+item.getMonth()+"/"+item.getYear()+"  "+item.getHour()+":"+item.getMinute()+":"+item.getSecond());
        holder.textType.setText(item.getType());
        holder.textCurrency.setText(item.getCurrency());
        holder.textAmount.setText(String.valueOf(item.getAmount()));
        holder.textDescription.setText(item.getDescription());

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        public TextView textNumber,textDate,textType,textCurrency,textAmount,textDescription;

        public ItemRowHolder(View itemView) {
            super(itemView);
            textNumber = (TextView) itemView.findViewById(R.id.textNumber);
            textDate = (TextView) itemView.findViewById(R.id.textDate);
            textType = (TextView) itemView.findViewById(R.id.textType);
            textCurrency = (TextView) itemView.findViewById(R.id.textCurrency);
            textAmount = (TextView) itemView.findViewById(R.id.textAmount);
            textDescription = (TextView) itemView.findViewById(R.id.textDescription);
        }
    }
}
