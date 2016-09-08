#TextToSpeech_Android :notes:
Tudo que for digitado no "EditText", será lido usando a voz nativa do android.
 * Versão beta do Text To Speech. A tela é básica, só estou demonstrando como se faz para chamar uma API nativa do Android, para usar em qualquer aplicação.
 * Lembrando que algumas adaptações serão necessárias.

######Componentes Básicos
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
 <!-- Componente não implementado, por não ser necessário o uso-->
 <!--<TextView
        android:id="@+id/textViewStatusLocale"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textAppearance="?android:attr/textAppearanceMedium" />
  -->
 ``` 

 iii.

###### Print das telas 
  1. Telas básicas 

![TELA 1 - Seleção de idiomas]
(https://github.com/tuerepinto/TextToSpeech_Android/blob/master/Screenshot_20160720-172705.png)


![TELA 2 - Escrevendo no campo EditText onde será usando para leitura]  
(https://github.com/tuerepinto/TextToSpeech_Android/blob/master/Screenshot_20160720-172713.png)

![TELA 3 - E a ultima tela! clicando o Button, e a aplicação ler o texto.] (https://github.com/tuerepinto/TextToSpeech_Android/blob/master/Screenshot_20160720-172852.png)
