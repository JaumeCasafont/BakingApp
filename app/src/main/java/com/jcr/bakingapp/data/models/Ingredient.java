package com.jcr.bakingapp.data.models;

public class Ingredient {
	private float quantity;
	private String measure;
	private String ingredient;

	public Ingredient(float quantity, String measure, String ingredient) {
		this.quantity = quantity;
		this.measure = measure;
		this.ingredient = ingredient;
	}

	public void setQuantity(int quantity){
		this.quantity = quantity;
	}

	public float getQuantity(){
		return quantity;
	}

	public void setMeasure(String measure){
		this.measure = measure;
	}

	public String getMeasure(){
		return measure;
	}

	public void setIngredient(String ingredient){
		this.ingredient = ingredient;
	}

	public String getIngredient(){
		return ingredient;
	}

	@Override
 	public String toString(){
		return ingredient + " (" + quantity + " " + measure + ")";
		}
}
