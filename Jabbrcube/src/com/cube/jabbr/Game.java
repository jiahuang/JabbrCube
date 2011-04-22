package com.cube.jabbr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
	
	int correctPicks = 0, numFlashcardsLeft = 0;
	boolean wrong = false;
	
	int currentFlashcardIndex = 0;
	FlashCard[] flashcards;
	boolean questionCorrect = false;
	
	TextView timerText, cardsText;

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
		timerText = (TextView) findViewById(R.id.Timer);
		cardsText = (TextView) findViewById(R.id.CardsLeft);
 		initializeGame();
	}

	public void initializeGame() {
		// put this in a seperate thread
		
		try{
			getCardsFromWebsite();
		} catch(Exception e) {
			Log.e("jabbr", "Problem getting cards from website:" +e.toString());
		}
		currentFlashcardIndex = 0;
		try{
			Log.i("jabbr", "current: "+this.currentFlashcardIndex);
			
			loadCurrentFlashCard();
			} catch (Exception e) {
				Log.e("jabbr", "load current flashcard error:"+e.toString());
			}
		
	}

	public void getCardsFromWebsite() {

		BufferedReader in = null;
		try {
			/*HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet("http://jabbrcube.heroku.com/api/getdeck/1");
			//request.setURI(new URI("url here"));} catch (Exception e) {
//			Log.e("ErROR", "Problem loading deck:" + e.toString());
//			return false;
//		}
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = client.execute(get, responseHandler);*/
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet("http://jabbrcube.heroku.com/api/getdeck/1");
			httpGet.addHeader(BasicScheme.authenticate(
			 new UsernamePasswordCredentials("jialiya", "password"),
			 "UTF-8", false));
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = httpClient.execute(httpGet, responseHandler);
			Log.i("jabbr", "response: " +response);
			/*
			in = new BufferedReader
			(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			String page = sb.toString();
			 */

			buildDeck(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}


	public boolean buildDeck(String responseBody) {
		try {
			JSONObject deck = new JSONObject(responseBody);
			JSONArray flashcards = deck.getJSONArray("flashcards");
			Log.i("jabbr", "got flashcards array");
			this.flashcards = new FlashCard[flashcards.length()];
			for( int i = 0; i < flashcards.length(); i++) {
				JSONObject flashcard = flashcards.getJSONObject(i);
//				Log.i("jabbr", "got flashcard object");
				String image_url = flashcard.getString("image_url");
				//until the stuff goes to amazon s3, every link is broken. Get the last url
				image_url = image_url.substring(image_url.lastIndexOf("http"));
				Log.i("jabbr", "got flashcard image_url:"+image_url);
				String title = flashcard.getString("word");
				Log.i("jabbr", "got flashcard word: "+title);
				int flashcardID = flashcard.getInt("flashcard_id");
				Log.i("jabbr", "got flashcard id:"+flashcardID);
				String[] choices = new String[4];
				String[] translatedChoices = new String[4];
				for ( int si = 0; si < 3 ; si++) {
					choices[si] = flashcard.getString("wrong"+(si+1));
					translatedChoices[si] = flashcard.getString("wrong"+(si+1)+"_native");
					Log.i("jabbr", "got flashcard wrong"+(si+1)+":"+choices[si]);
				}
				choices[3] = flashcard.getString("answer");
				Log.i("jabbr", "got flashcard answer");
				if (choices[3] == null)
					choices[3] = "THE ANSWER";
				int correctChoice = 3;
				Log.i("jabbr","now to load: "+image_url);
				
				Drawable drawable;
				try {
					drawable= loadDrawable(image_url);
					Log.i("jabbr", "got flashcard drawable by loading:"+image_url);
				} catch (Exception e) {
					//drawable = getResources().getDrawable(R.drawable.no_image);
					drawable = loadDrawable("http://catsinsinks.com/images/cats/rotator.php");
					Log.i("jabbr", "CATS!!!! IN F*CKING SINKS Y'ALL.");
				}
				FlashCard flashCardObject = new FlashCard(drawable, title, choices, translatedChoices,
						correctChoice, flashcardID);
				flashCardObject.randomizeChoices();
				this.flashcards[i] = flashCardObject;
				Log.i("jabbr", "Got card #"+i);
			}
			numFlashcardsLeft = flashcards.length();
			updateCardText();

		} catch (Exception e) {
			Log.e("jabbr", "Problem loading deck:" + e.toString());
			return false;
		}
		

		return true;
	}

	public Drawable loadDrawable(String image_url) {


		URL url = null;
		InputStream inputStream = null;
		try {
			url = new URL(image_url);
			inputStream = (InputStream) url.getContent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Drawable drawable = Drawable.createFromStream(inputStream, "src");
		//Toast.makeText(this.getApplicationContext(), "url:"+url.toString(), Toast.LENGTH_SHORT).show();
		//tableLayout.setBackgroundDrawable(drawable);
		return drawable;
	}

	public void loadCurrentFlashCard() {
		this.wrong = false;
		Log.i("jabbr", ""+this.currentFlashcardIndex +"/"+ this.flashcards.length);
		//Toast.makeText(this.getApplicationContext(), "loading current flash:"+this.currentFlashcardIndex, Toast.LENGTH_SHORT).show();
		FlashCard flashcard = this.flashcards[this.currentFlashcardIndex];
		if (flashcard != null){
			tableLayout.setBackgroundDrawable(flashcard.drawable);
			for (int i = 0; i < 4; i++) {
				choices[i].setText(flashcard.choices[i]);
				Log.i("jabbr", "setting text for choice "+i+" to "+flashcard.choices[i]);
			}
			
			title.setText(flashcard.title);
			this.correctChoice = flashcard.correctChoice;
			Log.i("jabbr", "setting correct choice to " + flashcard.correctChoice);
			popup.setVisibility(View.INVISIBLE);
		}
		else
			Toast.makeText(this.getApplicationContext(), "NULL POINTER: index, length:"+this.currentFlashcardIndex+" "+  this.flashcards.length, Toast.LENGTH_LONG).show();
		// reset all buttons to be visible
		setAllVisible();
		//this.currentFlashcardIndex++;
	}
	
	private void setAllVisible(){
		for(int i =0; i<choices.length; i++){
			choices[i].setVisibility(View.VISIBLE);
		}
	}

	public void onChoiceClicked(View view) {
		Button button = (Button) view;
		this.questionCorrect = (choices[correctChoice] == button);
		
		if (this.questionCorrect) {
			if (!this.wrong){
				flashcards[currentFlashcardIndex].setCorrect();
				correctPicks++;
			}
			numFlashcardsLeft--;
			updateCardText();
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
			this.wrong = true;
			button.setVisibility(View.INVISIBLE);
			int i;
			for ( i = 0; i < choices.length; i++) {
				if (choices[i] == button) break;
			}
			popup.setText("Incorrect!\n" + button.getText().toString() + " = " + translatedChoices[i]);
			popup.setBackgroundColor(0xaaff0000);
			popup.setVisibility(View.VISIBLE);
			vibrator.vibrate(500);
		}
	}

	public void onPopupClicked(View v) {
		popup.setVisibility(View.INVISIBLE);
		if (this.questionCorrect && this.currentFlashcardIndex < (this.flashcards.length-1)) {
			this.currentFlashcardIndex++;
			loadCurrentFlashCard();
			Toast.makeText(this.getApplicationContext(), "index, length: "+this.currentFlashcardIndex +" "+ this.flashcards.length, Toast.LENGTH_SHORT).show();
		}
		else if (this.questionCorrect && this.currentFlashcardIndex >= (this.flashcards.length-1)){
			Toast.makeText(this.getApplicationContext(), "Picked "+correctPicks+" right on first try", Toast.LENGTH_SHORT).show();
		//TODO: go to stat page
		}

	}
	public void updateCardText() {
		cardsText.setText("Cards Left:\n"+numFlashcardsLeft);
	}
}