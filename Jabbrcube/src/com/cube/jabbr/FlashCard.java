package com.cube.jabbr;

import android.graphics.Bitmap;

public class FlashCard {
	Bitmap bitmap;
	String title;
	String[] choices, translatedChoices;
	int correctChoice;
	public FlashCard(Bitmap bitmap, String title, String[] choices, String[] translatedChoices,
			int correctChoice) {
		super();
		this.bitmap = bitmap;
		this.title = title;
		this.choices = choices;
		this.translatedChoices = translatedChoices;
		this.correctChoice = correctChoice;
	}
	
}