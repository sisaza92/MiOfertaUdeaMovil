package udea.edu.co.miofertaudea.vista.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import udea.edu.co.miofertaudea.R;
import udea.edu.co.miofertaudea.modelo.dao.Implementations.MateriaOfertadaDaoImpl;
import udea.edu.co.miofertaudea.modelo.dao.Implementations.TandaDaoImpl;
import udea.edu.co.miofertaudea.modelo.dao.Interfaces.MateriaOfertadaDao;
import udea.edu.co.miofertaudea.modelo.dao.Interfaces.TandaDao;
import udea.edu.co.miofertaudea.modelo.dto.Estudiante;
import udea.edu.co.miofertaudea.modelo.dto.MateriaOfertada;
import udea.edu.co.miofertaudea.modelo.dto.Tanda;
import udea.edu.co.miofertaudea.service.ServiceImpl;
import udea.edu.co.miofertaudea.vista.adapter.MateriaOfertadaListAdapter;

public class Oferta_Ppal extends AppCompatActivity {

    private RecyclerView recyclerView;
    private IntentFilter filtro;
    private BroadcastReceiver receptor;

    private TextView mTVOfertaPPTanda;
    private TextView mTVOfertaPPImpedimentos;

    private Estudiante estudiante;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("REGISTRO -->","CLASE: Oferta_Ppal      METODO: onCreate");
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        setContentView(R.layout.activity_oferta_ppal);
        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mTVOfertaPPTanda = (TextView) findViewById(R.id.TVOfertaPPTanda);
        mTVOfertaPPImpedimentos = (TextView) findViewById(R.id.TVOfertaPPImpedimentos);

        // verificar llega null
        estudiante =(Estudiante) getIntent().getExtras().getSerializable("ESTUDIANTE");
        //Log.d("REGISTRO -->","CLASE: Oferta_Ppal      METODO: onCreate ----> en el intent" +
        //        " llego el estudiante: "+ estudiante.toString());


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //getTanda();


        getAllMateriasOfertadas();

    }

    @Override
    public void onResume() {
        super.onResume();
        receptor =  new TimelineReceiver();
        registerReceiver(receptor, filtro);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receptor);
    }

    /**
     * Metodo que Crea un IntentService para llamar al servicio que lista las materias.
     */
    private void getAllMateriasOfertadas(){

        filtro = new IntentFilter("udea.edu.co.miofertaudea.NUEVA_LISTA");

        MateriaOfertadaDao materiaOfertadaDao  = new MateriaOfertadaDaoImpl();
        List<MateriaOfertada> materiasOfertadas = materiaOfertadaDao.getAllMateriasOfertadas();
        if(materiasOfertadas.size()>0){
            //listaMaterias.setAdapter(new MateriaOfertadaListAdapter( this, (ArrayList<MateriaOfertada>) materiasOfertadas));
            recyclerView.setAdapter(new MateriaOfertadaListAdapter( this, (ArrayList<MateriaOfertada>) materiasOfertadas));
        }else{
            String idPrograma =  getIntent().getStringExtra("idPrograma");
            String idEstudiante = getIntent().getStringExtra("idEstudiante");
            Log.d("REGISTRO -->", "CLASE: Oferta_Ppal   METODO: getAllMateriasOfertadas");
            Log.d("IMPORTANTE -->", "CLASE: Oferta_Ppal   METODO: getAllMateriasOfertadas" +
                    " codigo del programa enviado es: " +idPrograma+ "  y el idEstudiante enviado es: "+idEstudiante);
            Intent listarMaterias = new Intent(Oferta_Ppal.this, ServiceImpl.class);
            listarMaterias.putExtra("accion", "listarMaterias");
            listarMaterias.putExtra("idPrograma",idPrograma);
            startService(listarMaterias);
        }
    }

    /**
     * Metodo que Crea un IntentService para llamar al servicio que obtine la informacion de la tanda del estudiante
     */
    private void getTanda(){

        filtro = new IntentFilter("udea.edu.co.miofertaudea.NUEVA_TANDA");
            Intent obtenerTanda = new Intent(Oferta_Ppal.this, ServiceImpl.class);
            obtenerTanda.putExtra("accion", "obtenerTanda");
            obtenerTanda.putExtra("cedulaEstudiante","101700");
            obtenerTanda.putExtra("semestre","20172");

            startService(obtenerTanda);

    }

    class TimelineReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("REGISTRO -->", "CLASE: TimelineReciver   METODO: onReceive");

            // direrenciar cual de los Broadcast esta llegando
            String BroadcastType = intent.getStringExtra("BroadcastType");

            switch (BroadcastType){
                case "Materias":
                    Log.d("REGISTRO -->", "CLASE: TimelineReciver   METODO: onReceive  -------> se resive el Broadcast" +
                            "con la Lista de Materias");
                    MateriaOfertadaDao materiaOfertadaDao  = new MateriaOfertadaDaoImpl();
                    List<MateriaOfertada> materiasOfertadas = materiaOfertadaDao.getAllMateriasOfertadas();
                    Log.d("BROADCAST RECIBIDO", "onReceived");
                    recyclerView.setAdapter(new MateriaOfertadaListAdapter( (Activity) context, (ArrayList<MateriaOfertada>) materiasOfertadas));
                    break;
                case "Tanda":
                    Log.d("REGISTRO -->", "CLASE: TimelineReciver   METODO: onReceive  -------> se resive el Broadcast" +
                            "con la Tanda");
                    TandaDao tandaDao = new TandaDaoImpl();
                    Tanda tanda = tandaDao.getTanda();
                    mTVOfertaPPTanda.setText(tanda.toString());

                    break;

                case "Impedimentos":
                    Log.d("REGISTRO -->", "CLASE: TimelineReciver   METODO: onReceive  -------> se resive el Broadcast" +
                            "con los Impedimentos");

                    break;

            }





        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Hola");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle("User");
                    isShow = false;
                }
            }
        });
    }
}
