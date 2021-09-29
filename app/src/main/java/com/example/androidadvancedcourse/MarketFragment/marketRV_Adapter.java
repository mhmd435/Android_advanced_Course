package com.example.androidadvancedcourse.MarketFragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.androidadvancedcourse.Models.cryptolistmodel.DataItem;
import com.example.androidadvancedcourse.R;
import com.example.androidadvancedcourse.databinding.MarketfragRvItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class marketRV_Adapter extends RecyclerView.Adapter<marketRV_Adapter.MarketRV_Holder> {


    ArrayList<DataItem> dataItems;
    LayoutInflater layoutInflater;

    public marketRV_Adapter(ArrayList<DataItem> dataItems) {
        this.dataItems = dataItems;
    }

    @NonNull
    @NotNull
    @Override
    public MarketRV_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        MarketfragRvItemBinding marketfragRvItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.marketfrag_rv_item,parent,false);
        return new MarketRV_Holder(marketfragRvItemBinding);
    }

    @Override
    public void onBindViewHolder(MarketRV_Holder holder, @SuppressLint("RecyclerView") int position) {
        holder.bind(dataItems.get(position),position);

        // set onclick for RecyclerView Items

        holder.marketfragRvItemBinding.marketRVCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("model", dataItems.get(position));

//                Navigation.findNavController(v).navigate(R.id.action_marketFragment_to_cryptoDetailFragment,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }


    public void updateData(ArrayList<DataItem> newdata) {
        dataItems = newdata;
        notifyDataSetChanged();

    }

    static class MarketRV_Holder extends RecyclerView.ViewHolder {
        MarketfragRvItemBinding marketfragRvItemBinding;

        public MarketRV_Holder(MarketfragRvItemBinding marketfragRvItemBinding) {
            super(marketfragRvItemBinding.getRoot());
            this.marketfragRvItemBinding = marketfragRvItemBinding;
        }

        public void bind(DataItem dataItem, int position){

            loadCoinlogo(dataItem);
            loadChart(dataItem);
            SetColorText(dataItem);
            marketfragRvItemBinding.marketCoinNumber.setText(String.valueOf(position + 1));
            marketfragRvItemBinding.marketCoinName.setText(dataItem.getName());
            marketfragRvItemBinding.marketCoinSymbol.setText(dataItem.getSymbol());
            SetDecimalsForPrice(dataItem);
            //set + or - before precent change
            if (dataItem.getListQuote().get(0).getPercentChange24h() > 0){
                marketfragRvItemBinding.MarketUpDownIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24);
                marketfragRvItemBinding.marketCoinChange.setText(String.format("%.2f",dataItem.getListQuote().get(0).getPercentChange24h()) + "%");
            }else if (dataItem.getListQuote().get(0).getPercentChange24h() < 0){
                marketfragRvItemBinding.MarketUpDownIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24);
                marketfragRvItemBinding.marketCoinChange.setText(String.format("%.2f",dataItem.getListQuote().get(0).getPercentChange24h()) + "%");
            }else {
                marketfragRvItemBinding.marketCoinChange.setText(String.format("%.2f",dataItem.getListQuote().get(0).getPercentChange24h()) + "%");
            }
            marketfragRvItemBinding.executePendingBindings();
        }

        private void loadCoinlogo(DataItem dataItem) {
            Glide.with(marketfragRvItemBinding.getRoot().getContext())
                    .load("https://s2.coinmarketcap.com/static/img/coins/32x32/" + dataItem.getId() + ".png")
                    .thumbnail(Glide.with(marketfragRvItemBinding.getRoot().getContext()).load(R.drawable.loading))
                    .into(marketfragRvItemBinding.coinlogo);
        }

        private void loadChart(DataItem dataItem) {

            Glide.with(marketfragRvItemBinding.getRoot().getContext())
                    .load("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/" + dataItem.getId() + ".png")
                    .into(marketfragRvItemBinding.chartImage);
        }

        //set diffrent decimals for diffrent price
        private void SetDecimalsForPrice(DataItem dataItem) {
            if (dataItem.getListQuote().get(0).getPrice() < 1){
                marketfragRvItemBinding.marketCoinPrice.setText("$" + String.format("%.6f",dataItem.getListQuote().get(0).getPrice()));
            }else if (dataItem.getListQuote().get(0).getPrice() < 10){
                marketfragRvItemBinding.marketCoinPrice.setText("$" + String.format("%.4f",dataItem.getListQuote().get(0).getPrice()));
            }else {
                marketfragRvItemBinding.marketCoinPrice.setText("$" + String.format("%.2f",dataItem.getListQuote().get(0).getPrice()));
            }
        }

        //set Color Green and Red for price and chart
        private void SetColorText(DataItem dataItem){
            int greenColor = Color.parseColor("#FF00FF40");
            int redColor = Color.parseColor("#FFFF0000");
            int whiteColor = Color.parseColor("#FFFFFF");
            if (dataItem.getListQuote().get(0).getPercentChange24h() < 0){
                marketfragRvItemBinding.chartImage.setColorFilter(redColor);
                marketfragRvItemBinding.marketCoinChange.setTextColor(Color.RED);
            }else if (dataItem.getListQuote().get(0).getPercentChange24h() > 0){
                marketfragRvItemBinding.chartImage.setColorFilter(greenColor);
                marketfragRvItemBinding.marketCoinChange.setTextColor(Color.GREEN);
            }else {
                marketfragRvItemBinding.chartImage.setColorFilter(whiteColor);
                marketfragRvItemBinding.marketCoinChange.setTextColor(Color.WHITE);
            }
        }
    }
}
