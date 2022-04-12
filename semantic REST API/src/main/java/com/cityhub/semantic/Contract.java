package com.cityhub.semantic;

public class Contract {

	private String name;
	
	private int id;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Contract [name=").append(name).append(", id=").append(id).append("]");
		return builder.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
