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

	public String getStickersCount() {
		return stickersCount;
	}

	public void setStickersCount(String stickersCount) {
		this.stickersCount = stickersCount;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCatImg() {
		return catImg;
	}

	public void setCatImg(String catImg) {
		this.catImg = catImg;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean aNew) {
		isNew = aNew;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelegram() {
		return telegram;
	}

	public void setTelegram(String telegram) {
		this.telegram = telegram;
	}

	public List<String> getStickers() {
		return stickers;
	}

	public void setStickers(List<String> stickers) {
		this.stickers = stickers;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSignal() {
		return signal;
	}

	public void setSignal(String signal) {
		this.signal = signal;
	}
}