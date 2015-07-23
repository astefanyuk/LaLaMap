package com.mariko.lalamap;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by AStefaniuk on 23.07.2015.
 */
public class TextSpeaker implements TextToSpeech.OnInitListener {

    public static class TextSpeakerEvent {
        public String text;

        public TextSpeakerEvent(String text) {
            this.text = text;
        }
    }

    private TextToSpeech tts;
    private boolean ready;

    public TextSpeaker(Context context) {
        tts = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.US);
            ready = true;
        }
    }

    public void speak(String text) {

        if (ready) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void destroy() {
        tts.shutdown();
    }
}
