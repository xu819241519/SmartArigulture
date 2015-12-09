package com.nfschina.aiot.entity;

import android.os.Parcel;
import android.os.Parcelable;


public class GreenHouse implements Parcelable {
	
	private String id;
	private String name;
	//private String crops;//所种的农作物
	private int alarminfoNum;
	
	
	public GreenHouse(){}
	public GreenHouse(String id, String name, int alarminfoNum) {
		this.alarminfoNum = alarminfoNum;
		this.id = id;
		this.name = name;
	}
	public GreenHouse(String id, String name,User user) {
		this.id = id;
		this.name = name;
	}
	public GreenHouse(String id, String name) {
		this.id = id;
		this.name = name;
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
//	public String getCrops() {
//		return crops;
//	}
//	public void setCrops(String crops) {
//		this.crops = crops;
//	}
	public int getAlarminfoNum() {
		return alarminfoNum;
	}
	public void setAlarminfoNum(int alarminfoNum) {
		this.alarminfoNum = alarminfoNum;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(id);
		out.writeString(name);
		out.writeInt(alarminfoNum);
	}
	public static final Parcelable.Creator<GreenHouse> CREATOR = new Creator<GreenHouse>() {
		
		@Override
		public GreenHouse[] newArray(int size) {
			return new GreenHouse[size];
		}
		
		@Override
		public GreenHouse createFromParcel(Parcel in) {
			return new GreenHouse(in);
		}
	};
	public GreenHouse(Parcel in){
		id = in.readString();
		name = in.readString();
		alarminfoNum = in.readInt();
	}
}
