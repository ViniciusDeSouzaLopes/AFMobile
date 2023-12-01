package com.example.afmobile;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddNovaSerie extends BottomSheetDialogFragment {

    public static final String TAG = "AddNovaSerie";

    private EditText editarEpisodio;
    private EditText editarSerie;
    private EditText editarDiaSemana;
    private EditText editarPlataforma;
    private EditText editarTemporada;
    private Button salvarButton;
    private FirebaseFirestore firestore;
    private Context context;
    private String id = "";
    private ImageButton sppechBtn;
    private static final int RECOGNIZER_CODE = 1;

    public static AddNovaSerie newInstance(){
        return new AddNovaSerie();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_nova_serie , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editarEpisodio = view.findViewById(R.id.ultimo_episodio_edittext);
        editarDiaSemana = view.findViewById(R.id.dia_semana_edittext);
        editarPlataforma = view.findViewById(R.id.plataforma_edittext);
        editarTemporada = view.findViewById(R.id.temporada_edittext);
        editarSerie = view.findViewById(R.id.nome_edittext);
        salvarButton = view.findViewById(R.id.save_btn);

        firestore = FirebaseFirestore.getInstance();

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String serie = bundle.getString("nome");
            String ultimoEpisodioAssistido = bundle.getString("ultimoEpisodioAssistido");
            String diaDaSemana = bundle.getString("diaDaSemana");
            String plataforma = bundle.getString("plataforma");
            String temporada = bundle.getString("temporada");
            id = bundle.getString("id");


            editarSerie.setText(serie);
            editarEpisodio.setText(ultimoEpisodioAssistido);
            editarDiaSemana.setText(diaDaSemana);
            editarPlataforma.setText(plataforma);
            editarTemporada.setText(temporada);

            if (serie.length() > 0){
                salvarButton.setEnabled(false);
                salvarButton.setBackgroundColor(Color.GRAY);
            }
        }

        editarSerie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    salvarButton.setEnabled(false);
                    salvarButton.setBackgroundColor(Color.GRAY);
                }else{
                    salvarButton.setEnabled(true);
                    salvarButton.setBackgroundColor(getResources().getColor(R.color.green_blue));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        boolean finalIsUpdate = isUpdate;
        salvarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = editarSerie.getText().toString();
                String episodio = editarEpisodio.getText().toString();
                String dia = editarDiaSemana.getText().toString();
                String plat = editarPlataforma.getText().toString();
                String temp = editarTemporada.getText().toString();

                if (finalIsUpdate){
                    firestore.collection("serie").document(id).update("nome" , nome , "ultimoEpisodioAssistido" , episodio,
                            "diaDaSemana", dia, "plataforma", plat, "temporada", temp);
                    Toast.makeText(context, "Serie Atualizada", Toast.LENGTH_SHORT).show();
                } else{
                    if (nome.isEmpty()) {
                        Toast.makeText(context, "Há algum campo de texto vazio. ", Toast.LENGTH_SHORT).show();
                    } else {

                        Map<String, Object> serieMap = new HashMap<>();

                        serieMap.put("nome", nome);
                        serieMap.put("ultimoEpisodioAssistido", episodio);
                        serieMap.put("diaDaSemana", dia);
                        serieMap.put("plataforma", plat);
                        serieMap.put("temporada", temp);
                        serieMap.put("status", 0);
                        serieMap.put("tempo", FieldValue.serverTimestamp());

                        firestore.collection("serie").add(serieMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> serie) {
                                if (serie.isSuccessful()) {
                                    Toast.makeText(context, "Salvo com Sucesso!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, serie.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECOGNIZER_CODE && resultCode == RESULT_OK) {
            ArrayList<String> serieText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            editarSerie.setText(serieText.get(0).toString());
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof  OnDialogClose){
            ((OnDialogClose)activity).onDialogClose(dialog);
        }
    }
}