package br.com.tuere.textmyspeech;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by tuere on 20/07/2016.
 */
public class MainActivity extends Activity implements TextToSpeech.OnInitListener {

    private static final int INTENT_TEXT_SPEECH_CODE = 9;

    private TextToSpeech textSpeech = null;
    private Spinner spinnerLanguage;
    private TextView textViewStatusLocale;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinnerLanguage = (Spinner) findViewById(R.id.spinnerLanguage);
        textViewStatusLocale = (TextView) findViewById(R.id.textViewStatusLocale);

        List<SpinnerDataObject<Locale>> locales = getLocales();
        ArrayAdapter<SpinnerDataObject<Locale>> adapter = new ArrayAdapter<SpinnerDataObject<Locale>>(this, android.R.layout.simple_list_item_1, locales);
        spinnerLanguage.setAdapter(adapter);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<SpinnerDataObject<Locale>> getLocales() {
        List<SpinnerDataObject<Locale>> locales = new ArrayList<SpinnerDataObject<Locale>>();
        Locale localeBR = new Locale("pt", "br");
        locales.add(new SpinnerDataObject("Brasil", localeBR));
        locales.add(new SpinnerDataObject("Canadá", Locale.CANADA));
        locales.add(new SpinnerDataObject("EUA", Locale.US));
        Locale localeES = new Locale("es", "ES");
        locales.add(new SpinnerDataObject("Espanha", localeES));
        return locales;

    }

    public void onClickSpeech(View view) {
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, INTENT_TEXT_SPEECH_CODE);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            SpinnerDataObject<Locale> registroSelecionado = (SpinnerDataObject<Locale>) spinnerLanguage.getSelectedItem();
            Locale localeSelected = registroSelecionado.getValue();


            int available = textSpeech.isLanguageAvailable(localeSelected);
            atualizarStatusDisponibilidadeLocaleSelecionado(available);
            textSpeech.setLanguage(localeSelected);

            String textoQueSeraFalado = ((EditText) findViewById(R.id.editTextTexto)).getText().toString();

            textSpeech.speak(textoQueSeraFalado, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void atualizarStatusDisponibilidadeLocaleSelecionado(int available) {
        switch (available) {

            case TextToSpeech.LANG_AVAILABLE:
                textViewStatusLocale.setText("Locale suportada, mas não por país ou variante!");
                break;

            case TextToSpeech.LANG_COUNTRY_AVAILABLE:
                textViewStatusLocale.setText("Locale suportada pela Localidade, mas não por país ou variante!");
                break;

            case TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE:
                textViewStatusLocale.setText("Locale suportada !");
                break;

            case TextToSpeech.LANG_MISSING_DATA:
                textViewStatusLocale.setText("Locale com dados faltando !");
                break;

            case TextToSpeech.LANG_NOT_SUPPORTED:
                textViewStatusLocale.setText("Locale nao suportada !");
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_TEXT_SPEECH_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                Log.i("", "success, create the TTS instance");
                textSpeech = new TextToSpeech(this, this);
            } else {
                Log.i("", "missing data, install it");
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }

    }


    private class SpinnerDataObject<T> {

        private String label;
        private T value;


        public SpinnerDataObject(String label, T value) {
            super();
            this.label = label;
            this.value = value;
        }


        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        private MainActivity getOuterType() {
            return MainActivity.this;
        }

        @Override
        public String toString() {
            return label;
        }

    }

}
