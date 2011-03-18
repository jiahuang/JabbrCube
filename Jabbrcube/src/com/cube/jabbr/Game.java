package com.cube.jabbr;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Game extends Activity {
	/** Called when the activity is first created. */
	Button[] choices = new Button[4];
	String[] translatedChoices = {"yello", "zup dawg", "das transltation", "Golden Gate"};
	TableLayout tableLayout;
	TextView title;
	Button popup;
	int correctChoice = 3;

	int numFlashCards = 10;
	FlashCard[] flashcards;

	Vibrator vibrator;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		choices[0] = (Button) findViewById(R.id.Button0);
		choices[1] = (Button) findViewById(R.id.Button1);
		choices[2] = (Button) findViewById(R.id.Button2);
		choices[3] = (Button) findViewById(R.id.Button3);
		tableLayout = (TableLayout) findViewById(R.id.buttonLayout);
		title = (TextView) findViewById(R.id.Title);
		popup = (Button) findViewById(R.id.Popup);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	}

	public void loadCard(FlashCard flashcard) {
		tableLayout.setBackgroundDrawable(new BitmapDrawable(flashcard.bitmap));
		for (int i = 0; i < 4; i++) {
			choices[i].setText(flashcard.choices[i]);
		}
		title.setText(flashcard.title);
		this.correctChoice = flashcard.correctChoice;
		popup.setVisibility(View.INVISIBLE);
	}

	public void onChoiceClicked(View view) {
		Button button = (Button) view;
		if (choices[correctChoice] == button) {
			view.performHapticFeedback( HapticFeedbackConstants.VIRTUAL_KEY,
					HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING );


			popup.setText("Correct!\n" + title.getText().toString() + " = " + button.getText());
			popup.setBackgroundColor(0xaa00ff00);
			popup.setVisibility(View.VISIBLE);
		} else {
			Log.i("jabbrCube","Touched a choice in the game! "+button.getText().toString());
			/*int dot = 20;      // Length of a Morse Code "dot" in milliseconds
			int dash = 50;     // Length of a Morse Code "dash" in milliseconds
			int short_gap = 20;    // Length of Gap Between dots/dashes
			int medium_gap = 50;   // Length of Gap Between Letters
			int long_gap = 100;    // Length of Gap Between Words
			long[] pattern = {
			    0,  // Start immediately
			    dot, short_gap, dot, short_gap, dot,    // s
			    medium_gap,
			    dash, short_gap, dash, short_gap, dash, // o
			    medium_gap,
			    dot, short_gap, dot, short_gap, dot,    // s
			    long_gap
			};
			vibrator.vibrate(pattern, -1);*/
			button.setVisibility(View.INVISIBLE);
			int i;
			for ( i = 0; i < choices.length; i++) {
				if (choices[i] == button) break;
			}
			popup.setText("Incorrect!\n" + button.getText().toString() + " = " + translatedChoices[i]);
			popup.setBackgroundColor(0xaaff0000);
			popup.setVisibility(View.VISIBLE);
			Toast.makeText(Game.this, button.getText(), Toast.LENGTH_SHORT).show();
			vibrator.vibrate(500);
		}
	}
	
	public void onPopupClicked(View v) {
		popup.setVisibility(View.INVISIBLE);
		
		
	}
}