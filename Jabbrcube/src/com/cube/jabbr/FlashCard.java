package com.cube.jabbr;

import java.util.Random;

import android.graphics.drawable.Drawable;

public class FlashCard {
	Drawable drawable;
	String title;
	String[] choices, translatedChoices;
	int correctChoice;
	int flashcardID;
	int userGotCorrect = 0;
	
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
	
	public void setCorrect(){
		userGotCorrect = 1;
	}
	
	public void randomizeChoices() {
		Random random = new Random();
		for(int i = 0; i < this.choices.length; i++) {
			int j = random.nextInt(this.choices.length);
			String ctemp = choices[i];
			choices[i] = choices[j];
			choices[j] = ctemp;
			String tctemp = translatedChoices[i];
			translatedChoices[i] = translatedChoices[j];
			translatedChoices[j] = tctemp;
			if (i == correctChoice)
				correctChoice = j;
			if (j == correctChoice)
				correctChoice = j;
		}
	}
	
}