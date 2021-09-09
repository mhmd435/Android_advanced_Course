package com.example.androidadvancedcourse.Models.cryptolistmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class DataItem implements Parcelable {

	@SerializedName("id")
	private int id;

	@SerializedName("name")
	private String name;

	@SerializedName("symbol")
	private String symbol;

	@SerializedName("lastUpdated")
	private String lastUpdated;

	@SerializedName("cmc_rank")
	private int cmcRank;

	@SerializedName("marketPairCount")
	private int numMarketPairs;

	@SerializedName("circulatingSupply")
	private double circulatingSupply;

	@SerializedName("totalSupply")
	private Number totalSupply;

	@SerializedName("max_supply")
	private double maxSupply;

	@SerializedName("ath")
	private double ath;

	@SerializedName("atl")
	private double atl;

	@SerializedName("high24h")
	private double high24h;

	@SerializedName("low24h")
	private double low24h;

	@SerializedName("isActive")
	private int isActive;

	@SerializedName("tags")
	private List<String> tags;

	@SerializedName("dateAdded")
	private String dateAdded;

	@SerializedName("quotes")
	private List<ListUSD> listQuote;

	@SerializedName("slug")
	private String slug;

	protected DataItem(Parcel in) {
		id = in.readInt();
		name = in.readString();
		symbol = in.readString();
		lastUpdated = in.readString();
		cmcRank = in.readInt();
		numMarketPairs = in.readInt();
		circulatingSupply = in.readDouble();
		totalSupply = in.readDouble();
		maxSupply = in.readDouble();
		ath = in.readDouble();
		atl = in.readDouble();
		high24h = in.readDouble();
		low24h = in.readDouble();
		isActive = in.readInt();
		tags = in.createStringArrayList();
		dateAdded = in.readString();
		slug = in.readString();
	}

	public static final Creator<DataItem> CREATOR = new Creator<DataItem>() {
		@Override
		public DataItem createFromParcel(Parcel in) {
			return new DataItem(in);
		}

		@Override
		public DataItem[] newArray(int size) {
			return new DataItem[size];
		}
	};

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public int getCmcRank() {
		return cmcRank;
	}

	public int getNumMarketPairs() {
		return numMarketPairs;
	}

	public double getCirculatingSupply() {
		return circulatingSupply;
	}

	public Number getTotalSupply() {
		return totalSupply;
	}

	public double getMaxSupply() {
		return maxSupply;
	}

	public double getAth() {
		return ath;
	}

	public double getAtl() {
		return atl;
	}

	public double getHigh24h() {
		return high24h;
	}

	public double getLow24h() {
		return low24h;
	}

	public int getIsActive() {
		return isActive;
	}

	public List<String> getTags() {
		return tags;
	}

	public String getDateAdded() {
		return dateAdded;
	}

	public List<ListUSD> getListQuote() {
		return listQuote;
	}

	public String getSlug() {
		return slug;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(symbol);
		dest.writeString(lastUpdated);
		dest.writeInt(cmcRank);
		dest.writeInt(numMarketPairs);
		dest.writeDouble(circulatingSupply);
		dest.writeInt((Integer) totalSupply);
		dest.writeDouble(maxSupply);
		dest.writeDouble(ath);
		dest.writeDouble(atl);
		dest.writeDouble(high24h);
		dest.writeDouble(low24h);
		dest.writeInt(isActive);
		dest.writeStringList(tags);
		dest.writeString(dateAdded);
		dest.writeString(slug);
	}
}