package com.jcr.bakingapp.data.models;

public class Step {
	private String videoURL;
	private String description;
	private int id;
	private String shortDescription;
	private String thumbnailURL;

	public Step(String videoURL, String description, int id, String shortDescription, String thumbnailURL) {
		this.videoURL = videoURL;
		this.description = description;
		this.id = id;
		this.shortDescription = shortDescription;
		this.thumbnailURL = thumbnailURL;
	}

	public void setVideoURL(String videoURL){
		this.videoURL = videoURL;
	}

	public String getVideoURL(){
		return videoURL;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setShortDescription(String shortDescription){
		this.shortDescription = shortDescription;
	}

	public String getShortDescription(){
		return shortDescription;
	}

	public void setThumbnailURL(String thumbnailURL){
		this.thumbnailURL = thumbnailURL;
	}

	public String getThumbnailURL(){
		return thumbnailURL;
	}

	@Override
 	public String toString(){
		return 
			"Step{" +
			"videoURL = '" + videoURL + '\'' + 
			",description = '" + description + '\'' + 
			",id = '" + id + '\'' + 
			",shortDescription = '" + shortDescription + '\'' + 
			",thumbnailURL = '" + thumbnailURL + '\'' + 
			"}";
		}
}
