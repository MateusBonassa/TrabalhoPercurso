package com.example.appnavigationdrawer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.appnavigationdrawer.models.Percurso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoricoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoricoFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listView;
    public HistoricoFragment() {
        // Required empty public constructor
    }


    public static HistoricoFragment newInstance(String param1, String param2) {
        HistoricoFragment fragment = new HistoricoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historico, container, false);
        List<Percurso> percursoList = new ArrayList<>();

        String caminhoArquivo = "dados.txt";
        listView = view.findViewById(R.id.listView);
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), caminhoArquivo);

            // Cria o arquivo se não existir
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String dados,titulo,dataInicioCorrida;
            double contadorTempo, contadorDistancia;
            while ((dados = bufferedReader.readLine()) != null) {
                Log.i("Leitura Arquivo", dados);
                String[] partes = dados.split("\\|");
                titulo = partes[0];
                 contadorTempo = Double.parseDouble(partes[1]);
                 contadorDistancia = Double.parseDouble(partes[2]);
                 dataInicioCorrida = partes[3];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
                LocalDateTime dataInicioCorridaLocalDateTime = LocalDateTime.parse(dataInicioCorrida, formatter);
                percursoList.add(new Percurso(dataInicioCorridaLocalDateTime,contadorDistancia,contadorTempo,titulo));

            }
            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

            PercursoAdapter percursoAdapter = new PercursoAdapter(view.getContext(),R.layout.item,percursoList);
            listView.setAdapter(percursoAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view ;
    }
}