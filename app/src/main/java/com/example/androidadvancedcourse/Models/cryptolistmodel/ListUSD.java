package com.example.androidadvancedcourse.Models.cryptolistmodel;

import com.google.gson.annotations.SerializedName;

public class ListUSD {

	@SerializedName("price")
	private double price;

	@SerializedName("volume24h")
	private Number volume24h;

	@SerializedName("volume7d")
	private double volume7d;

	@SerializedName("volume30d")
	private double volume30d;

	@SerializedName("marketCap")
	private Number marketCap;

	@SerializedName("percentChange1h")
	private double percentChange1h;

	@SerializedName("lastUpdated")
	private String lastUpdated;

	@SerializedName("percentChange24h")
	private double percentChange24h;

	@SerializedName("percentChange7d")
	private double percentChange7d;

	@SerializedName("percentChange30d")
	private double percentChange30d;

	@SerializedName("percentChange60d")
	private double percentChange60d;

	@SerializedName("percentChange90d")
	private double percentChange90d;

	@SerializedName("fullyDilluttedMarketCap")
	private double fullyDilluttedMarketCap;

	@SerializedName("dominance")
	private double dominance;

	@SerializedName("turnover")
	private double turnover;

	public double getPrice() {
		return price;
	}

	public Number getVolume24h() {
		return volume24h;
	}

	public double getVolume7d() {
		return volume7d;
	}

	public double getVolume30d() {
		return volume30d;
	}

	public Number getMarketCap() {
		return marketCap;
	}

	public double getPercentChange1h() {
		return percentChange1h;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public double getPercentChange24h() {
		return percentChange24h;
	}

	public double getPercentChange7d() {
		return percentChange7d;
	}

	public double getPercentChange30d() {
		return percentChange30d;
	}

	public double getPercentChange60d() {
		return percentChange60d;
	}

	public double getPercentChange90d() {
		return percentChange90d;
	}

	public double getFullyDilluttedMarketCap() {
		return fullyDilluttedMarketCap;
	}

	public double getDominance() {
		return dominance;
	}

	public double getTurnover() {
		return turnover;
	}
}