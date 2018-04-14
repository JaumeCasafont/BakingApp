package com.jcr.bakingapp.data.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

@Entity(tableName = "Recipes")
public class Recipe {
	private String image;
	private int servings;
	private String name;
	private List<Ingredient> ingredients;
	@NonNull
	@PrimaryKey
	private int id;
	private List<Step> steps;

	public Recipe(String image, int servings, String name, List<Ingredient> ingredients, @NonNull int id, List<Step> steps) {
		this.image = image;
		this.servings = servings;
		this.name = name;
		this.ingredients = ingredients;
		this.id = id;
		this.steps = steps;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setServings(int servings){
		this.servings = servings;
	}

	public int getServings(){
		return servings;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setIngredients(List<Ingredient> ingredients){
		this.ingredients = ingredients;
	}

	public List<Ingredient> getIngredients(){
		return ingredients;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setSteps(List<Step> steps){
		this.steps = steps;
	}

	public List<Step> getSteps(){
		return steps;
	}

	@Override
 	public String toString(){
		return 
			"Recipe{" +
			"image = '" + image + '\'' + 
			",servings = '" + servings + '\'' + 
			",name = '" + name + '\'' + 
			",ingredients = '" + ingredients + '\'' + 
			",id = '" + id + '\'' + 
			",steps = '" + steps + '\'' + 
			"}";
		}
}