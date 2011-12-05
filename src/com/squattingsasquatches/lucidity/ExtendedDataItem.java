package com.squattingsasquatches.lucidity;

public class ExtendedDataItem extends DataItem {
	
	private String itemInfo1;
	private String itemInfo2;

	public ExtendedDataItem(int id, String name) {
		this(id, name, "", "");
	}
	
	public ExtendedDataItem(int id, String name, String itemInfo1, String itemInfo2) {
		super(id, name);
		this.setItemInfo1(itemInfo1);
		this.setItemInfo2(itemInfo2);
	}

	public String getItemInfo1() {
		return itemInfo1;
	}

	public void setItemInfo1(String itemInfo1) {
		this.itemInfo1 = itemInfo1;
	}

	public String getItemInfo2() {
		return itemInfo2;
	}

	public void setItemInfo2(String itemInfo2) {
		this.itemInfo2 = itemInfo2;
	}
}
