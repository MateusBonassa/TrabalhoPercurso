package com.example.appnavigationdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appnavigationdrawer.models.Percurso;

import org.w3c.dom.Text;

import java.util.List;

public class PercursoAdapter extends ArrayAdapter<Percurso> {
    private int resource;
    public PercursoAdapter(@NonNull Context context, int resource, @NonNull List<Percurso> objects) {
        super(context, resource, objects);
        this.resource=resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(this.resource,parent,false);
        TextView tvTitulo = convertView.findViewById(R.id.tvTitulo);
        TextView tvDistancia = convertView.findViewById(R.id.tvDistancia);
        TextView tvTempo = convertView.findViewById(R.id.tvTempo);
        TextView tvDtHora = convertView.findViewById(R.id.tvDtHora);
        Percurso percurso = getItem(position);
        tvTitulo.setText(String.format("%s",percurso.getTitulo()));
        tvDistancia.setText(String.format("%.2f",percurso.getContadorDistancia()));
        tvTempo.setText(String.format("%.2f",percurso.getContadorTempo()));
        tvDtHora.setText(String.format("%s",percurso.getDataInicioCorrida().toString().replace("T","\n")));
        return convertView;
    }
}
