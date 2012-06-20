package de.myreality.dev.littlewars.components;

public class UpdateFile {

	private long size, sum;
	private String path;
	
	public UpdateFile(String path, long size, long sum) {
		this.path = path;
		this.size = size;
		this.sum = sum;
	}

	public Long getSize() {
		return size;
	}

	public Long getSum() {
		return sum;
	}

	public String getPath() {
		return path;
	}
}
