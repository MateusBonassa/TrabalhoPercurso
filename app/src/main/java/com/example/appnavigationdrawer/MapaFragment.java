package com.example.appnavigationdrawer;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;


import com.example.appnavigationdrawer.models.Percurso;
import com.google.android.material.textfield.TextInputEditText;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.overlay.Marker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class MapaFragment extends Fragment {

    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private double latitudeInicio, longitudeInicio;
    private double latitudeFim, longitudeFim;
    private double latitudeInicioCorrida, longitudeInicioCorrida;
    private LocalDateTime dataInicioCorrida;
    private double contadorDistancia;
    private double contadorTempo;
    private String titulo;
    private boolean estado = true;

    private boolean corrida = false;
    public static IMapController mapController;
    private MapView map;
    private Button buttonFIPP, buttonZoom, buttonMarker;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextInputEditText tiTitulo;

    private Button playPauseButton;
    private Button stopButton;

    private String mParam1;
    private String mParam2;

    private View view;

    private List<Percurso> historicoPercursos;

    public MapaFragment() {
        // Required empty public constructor
    }


    public static MapaFragment newInstance(String param1, String param2) {
        MapaFragment fragment = new MapaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mapa, container, false);
        MinhaLocalizacaoListener.vincularCords(view);
        map = view.findViewById(R.id.mapaId);

        map.setTileSource(TileSourceFactory.MAPNIK);

        tiTitulo = view.findViewById(R.id.inputTitulo);
        titulo = tiTitulo.getText().toString();
        // necessário para API superior à 13
        Configuration.getInstance().load(view.getContext(), PreferenceManager.getDefaultSharedPreferences(view.getContext()));
        mapController = map.getController();
        mapController.setZoom(15.0);


        locationManager = (LocationManager) view.getContext().getSystemService(Context.LOCATION_SERVICE);
        LocationListener mLocListener = new MinhaLocalizacaoListener();

        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions


            return view;
        }

        playPauseButton = view.findViewById(R.id.playPauseButton);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              percorrerCaminho();
           }
        });

        stopButton = view.findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(corrida && !tiTitulo.getText().toString().isEmpty())
                    finalizar_corrida();

            }
        });

        // Altera a posição dos botões
        playPauseButton.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        ));



        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocListener);

        Location location = locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(MinhaLocalizacaoListener.latitude == 0.0){
            MinhaLocalizacaoListener.latitude = -22.121265;
            MinhaLocalizacaoListener.longitude = -51.383400;
        }
        else
            mLocListener.onLocationChanged(location);
       Log.i("cords",""+MinhaLocalizacaoListener.latitude+"   "+MinhaLocalizacaoListener.longitude);
        mapController.setCenter(new GeoPoint(MinhaLocalizacaoListener.latitude,MinhaLocalizacaoListener.longitude));
        MinhaLocalizacaoListener.pontoAtual = new Marker(map);
        MinhaLocalizacaoListener.pontoAtual.setPosition(new GeoPoint(MinhaLocalizacaoListener.latitude,MinhaLocalizacaoListener.longitude));
        MinhaLocalizacaoListener.pontoAtual.setTitle("Ponto Atual");
        MinhaLocalizacaoListener.pontoAtual.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(MinhaLocalizacaoListener.pontoAtual);
        return view;


    }

    private double calculaDeslocamento(double latInicio, double lonInicio, double latFim, double lonFim) {
        // Fórmula haversine para calcular a distância entre dois pontos geográficos
        double raioTerra = 6371000; // raio médio da Terra em metros
        double dLat = Math.toRadians(latFim - latInicio);
        double dLon = Math.toRadians(lonFim - lonInicio);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(latInicio)) * Math.cos(Math.toRadians(latFim)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return raioTerra * c; // Distância em metros
    }

    private double calculaTempo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        // Calcula a diferença em dias entre as datas de início e fim
        return ChronoUnit.SECONDS.between(dataInicio, dataFim);
    }

    private void percorrerCaminho() {

        if (!corrida) {
            dataInicioCorrida = LocalDateTime.now();
            latitudeInicioCorrida = MinhaLocalizacaoListener.latitude;
            longitudeInicioCorrida = MinhaLocalizacaoListener.longitude;
            corrida = true;
            contadorTempo = 0;
            contadorDistancia = 0;
        }
        if (estado) {
            playPauseButton.setText("pause");
            dataInicio = LocalDateTime.now();
            latitudeInicio = MinhaLocalizacaoListener.latitude;
            longitudeInicio = MinhaLocalizacaoListener.longitude;
        } else {
            playPauseButton.setText("play");
            dataFim = LocalDateTime.now();
            latitudeFim = MinhaLocalizacaoListener.latitude;
            longitudeFim = MinhaLocalizacaoListener.longitude;
            contadorDistancia += calculaDeslocamento(latitudeInicio, longitudeInicio, latitudeFim, longitudeFim);
            contadorTempo += calculaTempo(dataInicio, dataFim);
        }
        estado = !estado;
    }

    private void finalizar_corrida(){
        if(!estado)
        {
            dataFim = LocalDateTime.now();
            latitudeFim = MinhaLocalizacaoListener.latitude;
            longitudeFim = MinhaLocalizacaoListener.longitude;
            contadorDistancia += calculaDeslocamento(latitudeInicio, longitudeInicio, latitudeFim, longitudeFim);
            contadorTempo += calculaTempo(dataInicio, dataFim);
            estado= true;
            playPauseButton.setText("play");
        }
        tiTitulo = view.findViewById(R.id.inputTitulo);
        titulo = tiTitulo.getText().toString();
        Percurso percurso = new Percurso(latitudeFim, longitudeFim, latitudeInicioCorrida, longitudeInicioCorrida, dataInicioCorrida, contadorDistancia, contadorTempo, titulo);

        tiTitulo.setText("");
        corrida = false;

        String caminhoArquivo = "dados.txt";

        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), caminhoArquivo);

            // Cria o arquivo se não existir
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream outputStream = new FileOutputStream(file, true);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);


            writer.append(percurso.getTitulo() + "|" + percurso.getContadorTempo() + "|" +percurso.getContadorDistancia()+ "|" +percurso.getDataInicioCorrida());
            writer.append(System.lineSeparator());

            writer.close();
            outputStream.close();




        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}