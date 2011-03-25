package com.cube.jabbr;

import android.graphics.drawable.Drawable;

public class FlashCard {
	Drawable drawable;
	String title;
	String[] choices, translatedChoices;
	int correctChoice;
	int flashcardID;
	public FlashCard(Drawable drawable, String title, String[] choices, String[] translatedChoices,
			int correctChoice, int flashcardID) {
		super();
		this.drawable = drawable;
		this.title = title;
		this.choices = choices;
		this.translatedChoices = translatedChoices;
		this.correctChoice = correctChoice;
		this.flashcardID = flashcardID;
	}
	
}