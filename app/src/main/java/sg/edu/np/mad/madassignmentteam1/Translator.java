 package sg.edu.np.mad.madassignmentteam1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

 public class Translator extends AppCompatActivity {
     //translator object to configure the source and target languages
private Translator translator;
//translator options to set source and target languages
private TranslatorOptions translatorOptions;
private EditText sourceLanguage;
private TextView destinationLanguage;
private MaterialButton translateButton,targetButton,sourceButton;
private Button backBtn;
private String destinationLanguageTitle = "Chinese";
private ArrayList<ModelLanguage>languageList;
private String sourceLanguageCode = "en";
private String sourceLanguageTitle = "English";
private String destinationLanguageCode = "zh";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);
        sourceLanguage = findViewById(R.id.sourceText);
        destinationLanguage = findViewById(R.id.destinationText);
        translateButton = findViewById(R.id.materialButton);
        targetButton = findViewById(R.id.targetButton);
        sourceButton = findViewById(R.id.sourceButton);
        backBtn = findViewById(R.id.back);
        //Translation using ML Kit: The code utilizes the ML Kit Translate API (com.google.mlkit.nl.translate.Translator) to perform text translation.
        // It creates a translator object, sets the source and target languages, and initiates the translation process when the translate button is clicked.

        //Localization: The code retrieves a list of available languages using TranslateLanguage.getAllLanguages() and displays them in their respective language titles.
        // It uses the Locale class to obtain the display language for each language code.

        loadAvailableLanguages();
        //user clicks back
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Translator.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //user can select source language from list
        sourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseSourceLanguage();
            }
        });

        //user can select language they want it to translate to
        targetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDestinationLanguage();
            }
        });
        //handle translation button after user clicks translate
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();

            }
        });
    }

     private void validateData() {
        String sourceLanguageText = sourceLanguage.getText().toString();
        //if EditText is empty
        if(sourceLanguageText.isEmpty()){
            Toast.makeText(this, "Enter text to translate...", Toast.LENGTH_SHORT).show();
            sourceLanguage.setError("Please enter a text to translate");
            sourceLanguage.requestFocus();
        }
        else{
            Translate();
        }
     }

     private void Translate() {
        translatorOptions = new TranslatorOptions.Builder()
                 .setSourceLanguage(sourceLanguageCode)
                 .setTargetLanguage(destinationLanguageCode)
                 .build();
         com.google.mlkit.nl.translate.Translator translator1 = Translation.getClient(translatorOptions);
         //initialise DownloadConditions with options to requireWifi
         DownloadConditions conditions = new DownloadConditions.Builder()
                 .requireWifi()
                 .build();
         //start downloading translation model if required(will download 1st time)
         translator1.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void unused) {
                 //start translation process
                 translator1.translate(sourceLanguage.getText().toString()).addOnSuccessListener(new OnSuccessListener<String>() {
                     @Override
                     public void onSuccess(String translatedText) {
                         //successfully translated
                            destinationLanguage.setText(translatedText);
                            destinationLanguage.invalidate();

                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         //failed to translate
                         Toast.makeText(Translator.this, "Enter text to translate due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 });

             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                //failed to translate model
                 Toast.makeText(Translator.this, "Failed to ready model due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
             }
         });

     }

     private void chooseSourceLanguage(){
        //initialise popup menu,put it into source button to choose source language from list
        PopupMenu popupMenu = new PopupMenu(this,sourceButton);

        //from languageList to display language titles
        for(int i=0;i<languageList.size();i++){
            //keep adding titles in popup menu item: 1 param is groupId, param2 is itemId, param3 is order and param4 is title
            popupMenu.getMenu().add(Menu.NONE, i, i, languageList.get(i).languageTitle);
        }
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int position = item.getItemId();
                //get code and title of the language selected
                sourceLanguageCode = languageList.get(position).languageCode;
                sourceLanguageTitle = languageList.get(position).languageTitle;
                //set the selected language to sourceButton as text and sourceLanguage as hint
                sourceButton.setText(sourceLanguageTitle);
                sourceLanguage.setHint("Enter " + sourceLanguageTitle);

                return false;
            }
        });
    }
    private void chooseDestinationLanguage(){

        //initialise popup menu,put it into source button to choose source language from list
        PopupMenu popupMenu = new PopupMenu(this,targetButton);
        //from languageList to display language titles
        for(int i=0;i<languageList.size();i++){
            //keep adding titles in popup menu item: 1 param is groupId, param2 is itemId, param3 is order and param4 is title
            popupMenu.getMenu().add(Menu.NONE, i, i, languageList.get(i).languageTitle);
        }
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int position = item.getItemId();
                //get code and title of the language selected
                destinationLanguageCode = languageList.get(position).languageCode;
                destinationLanguageTitle = languageList.get(position).languageTitle;
                //set the selected language to targetButton as text
                targetButton.setText(destinationLanguageTitle);


                return false;
            }
        });
    }
     private void loadAvailableLanguages() {
        //initialise language array list
        languageList = new ArrayList<>();
        //get list of all language codes e.g en,zh
         List<String>languageCodeList = TranslateLanguage.getAllLanguages();
         //make list containing both language code and language title
         for(String languageCode:languageCodeList){
             String languageTitle = new Locale(languageCode).getDisplayLanguage();//en->english
             //Get ModelLanguage model and add into list
             ModelLanguage modelLanguage = new ModelLanguage(languageCode,languageTitle);
             languageList.add(modelLanguage);
         }
     }
 }