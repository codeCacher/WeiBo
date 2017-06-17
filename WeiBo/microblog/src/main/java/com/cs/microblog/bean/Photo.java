package com.cs.microblog.bean;

import android.os.Parcel;
import android.os.Parcelable;


public class Photo implements Parcelable {
	private String imageId;
	private String imagePath;
	private String imageSize;
	private String imageName;
	private boolean isSelected;

	public Photo() {
		imageId = "";
		imagePath = "";
		imageSize = "";
		imageName = "";
		isSelected = false;
	}

	protected Photo(Parcel in) {
		imageId = in.readString();
		imagePath = in.readString();
		imageSize = in.readString();
		imageName = in.readString();
		isSelected = in.readByte() != 0;
	}

	public static final Creator<Photo> CREATOR = new Creator<Photo>() {
		@Override
		public Photo createFromParcel(Parcel in) {
			return new Photo(in);
		}

		@Override
		public Photo[] newArray(int size) {
			return new Photo[size];
		}
	};

	public long getLongImageSize(){
		long size = 0;
		try {
			size = Long.parseLong(imageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return size;
		}
		return size;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getImageSize() {
		return imageSize;
	}

	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(imageId);
		dest.writeString(imagePath);
		dest.writeString(imageSize);
		dest.writeString(imageName);
		dest.writeByte((byte) (isSelected ? 1 : 0));
	}
}
