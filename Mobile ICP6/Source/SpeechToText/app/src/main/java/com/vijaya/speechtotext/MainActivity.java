package com.vijaya.speechtotext;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech tts;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private TextView mVoiceInputTv;
    private ImageButton mSpeakBtn;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String PREFS = "prefs";
    private static final String NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(PREFS,0);// Extracting name and saving in editor
        editor = preferences.edit();

        mVoiceInputTv = (TextView) findViewById(R.id.voiceInput);
        mSpeakBtn = (ImageButton) findViewById(R.id.btnSpeak);
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() { // text to speech start listening
            public void onInit(int status) { // initiate it
                if (status == TextToSpeech.SUCCESS) { // if successful
                    int text = tts.setLanguage(Locale.US); // initiate text- the language is set to US
                    speak("Hi, What is your name"); // Now speak Hello,What is your name
                } }}); }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }
    private void speak(String text){ // Speaking code
        // Using TTS speak
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    // Add the user input voice to text as seen in output slide
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mVoiceInputTv.setText(result.get(0));
                    ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String inSpeech = res.get(0);
                    recognition(inSpeech);
                }
                break;
            }

        }
    }
    private void recognition(String text){ // Recognize speaking
        //if the speech contains this words,start speaking
        Log.e("Speech",""+text);
        String[] speech = text.split(" ");
        String name = speech[speech.length-1];
        editor.putString(NAME,name).apply(); // Put the name in Editor
        speak("My name is "+preferences.getString(NAME,null));
// Speak You name + name+ Please ask your questions
        if(text.contains("what medicine should I take")){ // user will ask this
            speak("I think you have Fever Please take this medicines");// speaker answer
        }
        if(text.contains("I'm not feeling good what I should do")){// user will ask this
            speak("I can understand.Please tell me your symptoms in short");// speaker answer
        }
        if(text.contains("Thank you my Medical Assistant")){// user will ask this
            speak("Thank you too Take care");//speaker answer
        }
        if(text.contains("what time is it")) {// Time asking
            SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");//dd/MM/yyyy
            Date now = new Date();
            String[] strDate = sdfDate.format(now).split(":");
            if (strDate[1].contains("00"))
                strDate[1] = "o'clock";
            speak("The time is " + sdfDate.format(now));
        }
    }
}