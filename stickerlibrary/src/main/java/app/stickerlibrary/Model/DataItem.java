package app.stickerlibrary.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataItem{

	@SerializedName("stickers_count")
	private String stickersCount;

	@SerializedName("identifier")
	private String identifier;

	@SerializedName("size")
	private String size;

	@SerializedName("cat_img")
	private String catImg;

	@SerializedName("is_new")
	private boolean isNew;

	@SerializedName("name")
	private String name;

	@SerializedName("telegram")
	private String telegram;

	@SerializedName("stickers")
	private List<String> stickers;

	@SerializedName("title")
	private String title;

	@SerializedName("signal")
	private String signal;

	public String getStickersCount(){
		return stickersCount;
	}

	public String getIdentifier(){
		return identifier;
	}

	public String getSize(){
		return size;
	}

	public String getCatImg(){
		return catImg;
	}

	public boolean isIsNew(){
		return isNew;
	}

	public String getName(){
		return name;
	}

	public String getTelegram(){
		return telegram;
	}

	public List<String> getStickers(){
		return stickers;
	}

	public String getTitle(){
		return title;
	}

	public String getSignal(){
		return signal;
	}
}