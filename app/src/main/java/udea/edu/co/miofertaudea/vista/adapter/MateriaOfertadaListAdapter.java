package udea.edu.co.miofertaudea.vista.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import udea.edu.co.miofertaudea.R;
import udea.edu.co.miofertaudea.modelo.dto.MateriaOfertada;
import udea.edu.co.miofertaudea.service.ServiceImpl;
import udea.edu.co.miofertaudea.vista.activity.GrupoActivity;

/**
 * Created by CristianCamilo on 10/10/2016.
 */

public class MateriaOfertadaListAdapter extends ArrayAdapter<MateriaOfertada> {

    private Activity activity;
    ArrayList<MateriaOfertada> materiaOfertadas;

    public MateriaOfertadaListAdapter(Activity activity, ArrayList<MateriaOfertada> listaCanciones) {

        super(activity, R.layout.item_materia);

        this.activity = activity;
        this.materiaOfertadas = listaCanciones;

    }

    static class ViewHolder {

        TextView codigoMateria;
        TextView nombreMateria;
        TextView grupoMateria;
        TextView creditos;
        TextView horario;

    }

    public int getCount() {
        return materiaOfertadas.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {

        View view = null;
        MateriaOfertada materiaOfertada = materiaOfertadas.get(position);
        LayoutInflater inflador = activity.getLayoutInflater();
        view = inflador.inflate(R.layout.item_materia, null);
        final ViewHolder viewHolder = new ViewHolder();

        // *** instanciamos a los recursos
        viewHolder.codigoMateria = (TextView) view.findViewById(R.id.codigoMateria);
        viewHolder.nombreMateria = (TextView) view.findViewById(R.id.nombreMateria);
        viewHolder.grupoMateria = (TextView) view.findViewById(R.id.grupoMateria);
        viewHolder.creditos = (TextView) view.findViewById(R.id.creditosMateria);
        viewHolder.horario = (TextView) view.findViewById(R.id.horarioMateria);

        // importante!!! establecemos el mensaje

        viewHolder.codigoMateria.setText(materiaOfertada.getCodigoMateria());
        viewHolder.nombreMateria.setText(materiaOfertada.getNombreMateria());
        viewHolder.grupoMateria.setText(materiaOfertada.getGrupo());
        viewHolder.creditos.setText(String.valueOf(materiaOfertada.getCreditos()));
        viewHolder.horario.setText(materiaOfertada.getHorario());

        view.setOnClickListener(getListener(position));
        return view;
    }

    private View.OnClickListener getListener(final int position){
        Log.d("REGISTRO -->","CLASE: MateriaOfertadaListAdapter, METODO: getListener");
        View.OnClickListener listener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent listarGruposMateria = new Intent(getContext(), GrupoActivity.class);
                String codigoMateria = materiaOfertadas.get(position).getCodigoMateria();
                String nombreMateria = materiaOfertadas.get(position).getNombreMateria();

                listarGruposMateria.putExtra("codigoMateria", codigoMateria);
                listarGruposMateria.putExtra("nombreMateria",nombreMateria);

                v.setOnClickListener(getListener(position));// TODO: mirar cual de los dos es el que funciona.
                Log.d("REGISTRO -->","ITEM "+position+" CLIQUEADO");
                Log.d("REGISTRO -->","se ha seleccionado la materia con nombre :"+nombreMateria+  " y codigoMateria: "
                        +codigoMateria+" para mostrar sus grupos");


                v.getContext().startActivity(listarGruposMateria);

            }
        };
        return listener;
    }

}
