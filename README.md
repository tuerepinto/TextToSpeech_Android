#TextToSpeech_Android :notes:
Tudo que for digitado no "EditText", será lido usando a voz nativa do android.
 * Versão beta do Text To Speech. A tela é básica, só estou demonstrando como se faz para chamar uma API nativa do Android, para usar em qualquer aplicação.
 * Lembrando que algumas adaptações serão necessárias.

#### Componentes Básicos :finnadie:
  i. EditText - Componente que será responsável, por receber os textos informados pelo usuário.

```xml
    <EditText
        android:id="@+id/editTextTexto"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:hint="Digite o texto aqui"/>
```
 
 ii. TextView - Componente não finalizado a implementação, pois não tinha necessidade.

 ```xml
 <!--Componente não implementado, por não ser necessário o uso-->
 <!--<TextView
        android:id="@+id/textViewStatusLocale"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textAppearance="?android:attr/textAppearanceMedium" />
  -->
 ``` 

iii. Spinner - Componente com mesmo comportamento de um ComboBox, sua finalidade é a seleção de idioma, que a API irá usa para lê o texto. Obs:. Em alguns casos e versão do Android, a opção de mudança de idioma não será possível de ser utilizada.

  ```xml
      <Spinner
        android:id="@+id/spinnerLanguage"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        />
  ```
iiii. Button - Componente responsável pela ação final, "Leitura do texto digitado no EditText".

```xml
    <Button
        android:id="@+id/buttonFale"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Fala Comigo"
        android:onClick="onClickSpeech" />
```

#### Código JAVA

* Como estou usando só uma tela, foi feito um código salsichão "Toda implementação do componetes eu uma única classe".

```java
package br.com.tuere.textmyspeech;

// importes usados na implementação de cada componente.
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

  //implementação básica Android para link classe com o layout
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

    // implementação do Spinner
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

    // ação ao clicar no button
    public void onClickSpeech(View view) {
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, INTENT_TEXT_SPEECH_CODE);
    }

    // verificado de região
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

    // verificador de região para a fala
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

```


#### Print das telas :godmode:
  1. Telas básicas 

![TELA 1 - Seleção de idiomas]
(https://github.com/tuerepinto/TextToSpeech_Android/blob/master/Screenshot_20160720-172705.png)


![TELA 2 - Escrevendo no campo EditText onde será usando para leitura]  
(https://github.com/tuerepinto/TextToSpeech_Android/blob/master/Screenshot_20160720-172713.png)

![TELA 3 - E a ultima tela! clicando o Button, e a aplicação ler o texto.] (https://github.com/tuerepinto/TextToSpeech_Android/blob/master/Screenshot_20160720-172852.png)
