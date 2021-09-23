package com.example.androidadvancedcourse.HomeFragment.adapters;

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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.androidadvancedcourse.Models.cryptolistmodel.DataItem;
import com.example.androidadvancedcourse.R;
import com.example.androidadvancedcourse.databinding.GainloseRvItemBinding;

import java.util.ArrayList;

public class GainLoseRvAdapter extends RecyclerView.Adapter<GainLoseRvAdapter.GainLoseRvHolder>{

    ArrayList<DataItem> dataItems;
    LayoutInflater layoutInflater;

    public GainLoseRvAdapter(ArrayList<DataItem> dataItems) {
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public GainLoseRvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        GainloseRvItemBinding gainloseRvItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.gainlose_rv_item,parent,false);
        return new GainLoseRvHolder(gainloseRvItemBinding);
    }

    @Override
    public void onBindViewHolder(GainLoseRvHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bind(dataItems.get(position));

        // set onclick for RecyclerView Items
        holder.gainloseRvItemBinding.GainLoseRVCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("model", dataItems.get(position));
//
//                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_cryptoDetailFragment,bundle);
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

    static class GainLoseRvHolder extends RecyclerView.ViewHolder {

        GainloseRvItemBinding gainloseRvItemBinding;

        public GainLoseRvHolder(GainloseRvItemBinding gainloseRvItemBinding) {
            super(gainloseRvItemBinding.getRoot());
            this.gainloseRvItemBinding = gainloseRvItemBinding;
        }

        public void bind(DataItem dataItem){

            loadCoinlogo(dataItem);
            loadChart(dataItem);
            SetColorText(dataItem);
            gainloseRvItemBinding.GLCoinName.setText(dataItem.getName());
            gainloseRvItemBinding.GLcoinSymbol.setText(dataItem.getSymbol());
            SetDecimalsForPrice(dataItem);
            //set + or - before precent change
            if (dataItem.getListQuote().get(0).getPercentChange24h() > 0){
                gainloseRvItemBinding.UpDownIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24);
                gainloseRvItemBinding.GLcoinChange.setText(String.format("%.2f",dataItem.getListQuote().get(0).getPercentChange24h()) + "%");
            }else if (dataItem.getListQuote().get(0).getPercentChange24h() < 0){
                gainloseRvItemBinding.UpDownIcon.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24);
                gainloseRvItemBinding.GLcoinChange.setText(String.format("%.2f",dataItem.getListQuote().get(0).getPercentChange24h()) + "%");
            }else {
                gainloseRvItemBinding.GLcoinChange.setText(String.format("%.2f",dataItem.getListQuote().get(0).getPercentChange24h()) + "%");
            }
            gainloseRvItemBinding.executePendingBindings();
        }

        private void loadCoinlogo(DataItem dataItem) {
            Glide.with(gainloseRvItemBinding.getRoot().getContext())
                    .load("https://s2.coinmarketcap.com/static/img/coins/32x32/" + dataItem.getId() + ".png")
                    .thumbnail(Glide.with(gainloseRvItemBinding.getRoot().getContext()).load(R.drawable.loading))
                    .into(gainloseRvItemBinding.gainLoseCoinlogo);
        }

        private void loadChart(DataItem dataItem) {
            Glide.with(gainloseRvItemBinding.getRoot().getContext())
                    .load("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/" + dataItem.getId() + ".png")
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(gainloseRvItemBinding.imageView);
        }

        //set diffrent decimals for diffrent price
        private void SetDecimalsForPrice(DataItem dataItem) {
            if (dataItem.getListQuote().get(0).getPrice() < 1){
                gainloseRvItemBinding.GLcoinPrice.setText("$" + String.format("%.6f",dataItem.getListQuote().get(0).getPrice()));
            }else if (dataItem.getListQuote().get(0).getPrice() < 10){
                gainloseRvItemBinding.GLcoinPrice.setText("$" + String.format("%.4f",dataItem.getListQuote().get(0).getPrice()));
            }else {
                gainloseRvItemBinding.GLcoinPrice.setText("$" + String.format("%.2f",dataItem.getListQuote().get(0).getPrice()));
            }
        }

        //set Color Green and Red for price
        private void SetColorText(DataItem dataItem){
            int greenColor = Color.parseColor("#FF00FF40");
            int redColor = Color.parseColor("#FFFF0000");
            int whiteColor = Color.parseColor("#FFFFFF");
            if (dataItem.getListQuote().get(0).getPercentChange24h() < 0){
                gainloseRvItemBinding.imageView.setColorFilter(redColor);
                gainloseRvItemBinding.GLcoinChange.setTextColor(Color.RED);
            }else if (dataItem.getListQuote().get(0).getPercentChange24h() > 0){
                gainloseRvItemBinding.imageView.setColorFilter(greenColor);
                gainloseRvItemBinding.GLcoinChange.setTextColor(Color.GREEN);
            }else {
                gainloseRvItemBinding.imageView.setColorFilter(whiteColor);
                gainloseRvItemBinding.GLcoinChange.setTextColor(Color.WHITE);
            }
        }
    }
}
