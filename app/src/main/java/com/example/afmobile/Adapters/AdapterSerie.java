package com.example.afmobile.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afmobile.AddNovaSerie;
import com.example.afmobile.MainActivity;
import com.example.afmobile.Models.ModeloSerie;
import com.example.afmobile.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdapterSerie extends RecyclerView.Adapter<AdapterSerie.MyViewHolder> {

    private List<ModeloSerie> serieList;
    private MainActivity activity;

    private FirebaseFirestore firestore;

    public AdapterSerie(MainActivity mainActivity , List<ModeloSerie> serieList){
        this.serieList = serieList;
        activity = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.cada_serie , parent , false);
        firestore = FirebaseFirestore.getInstance();

        return new MyViewHolder(view);
    }

    public void deleteSerie(int position){
        ModeloSerie serieModel = serieList.get(position);
        firestore.collection("serie").document(serieModel.SerieId).delete();
        serieList.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext(){
        return activity;
    }
    public void editSerie(int position){
        ModeloSerie serieModel = serieList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("nome" , serieModel.getNome());
        bundle.putString("diaDaSemana" , serieModel.getDiaDaSemana());
        bundle.putString("plataforma" , serieModel.getPlataforma());
        bundle.putString("temporada" , serieModel.getTemporada());
        bundle.putString("ultimoEpisodioAssistido" , serieModel.getUltimoEpisodioAssistido());
        bundle.putString("id" , serieModel.SerieId);

        AddNovaSerie addNovaSerie = new AddNovaSerie();
        addNovaSerie.setArguments(bundle);
        addNovaSerie.show(activity.getSupportFragmentManager() , addNovaSerie.getTag());
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ModeloSerie serieModel = serieList.get(position);
        holder.mCheckBox.setText(serieModel.getNome());

        holder.mDueDateTv.setText("Data: " + serieModel.getDiaDaSemana());

        holder.mPlataformaTv.setText("Plataforma: " + serieModel.getPlataforma());

        holder.mTemporadaTv.setText("Temporada: " + serieModel.getTemporada());

        holder.mUltimoEpiTv.setText("Último Episódio: " + serieModel.getUltimoEpisodioAssistido());

        holder.mCheckBox.setChecked(toBoolean(serieModel.getStatus()));

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("serie").document(serieModel.SerieId).update("status" , 1);
                }else{
                    firestore.collection("serie").document(serieModel.SerieId).update("status" , 0);
                }
            }
        });

    }

    private boolean toBoolean(int status){
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return serieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mDueDateTv;
        TextView mPlataformaTv;
        TextView mTemporadaTv;
        TextView mUltimoEpiTv;
        CheckBox mCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mDueDateTv = itemView.findViewById(R.id.due_date_tv);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
            mPlataformaTv = itemView.findViewById(R.id.plataforma_tv);
            mTemporadaTv = itemView.findViewById(R.id.temporada_tv);
            mUltimoEpiTv = itemView.findViewById(R.id.ultimo_episodio_tv);

        }
    }
}
