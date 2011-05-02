package com.cube.jabbr;

import java.util.Random;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class FlashCard {
	Drawable drawable;
	String title = "word title:"+Math.floor(Math.random()*1000);
	String[] choices= {"choice","choice2","choice3","choice4"}, 
		translatedChoices ={"translated choice1","translated choice2","translated choice3","translated choice4"};
	int correctChoice = 3;
	int flashcardID = 1;
	int userGotCorrect = 0;
	
	public FlashCard() {
	}
	
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
		//Log.i("jabbr", "before choices");
		//logChoices();
		for(int i = 0; i < choices.length; i++) {
			int j = random.nextInt(choices.length);
			//Log.i("jabbr","switching "+i+ " with "+j+":"+choices[i]+"<->"+choices[j]);
			
			String ctemp = choices[i];
			choices[i] = choices[j];
			choices[j] = ctemp;
			
			String tctemp = ""+translatedChoices[i];
			translatedChoices[i] = translatedChoices[j];
			translatedChoices[j] = tctemp;
			if (i == correctChoice)
				correctChoice = j;
			else if (j == correctChoice)
				correctChoice = i;
			//Log.i("jabbr", "i:"+choices[i] + " j:"+choices[j] + " temp:"+ctemp+ " repeat");
			//logChoices();
		}
		//Log.i("jabbr", "after choices");
		//logChoices();
	}
	
	public void logChoices() {
		String s = "";
		for (int i = 0; i < 4; i++) {
			s+= i+":"+ choices[i]+"\t";
		}
		Log.i("jabbr", s);
		Log.i("jabbr", "correct:"+correctChoice+":"+choices[correctChoice]);
	}
	
}