package com.example.note.guiavirtual.Equipos;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.note.guiavirtual.AppController;
import com.example.note.guiavirtual.R;
import com.example.note.guiavirtual.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConsultaEquipo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConsultaEquipo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultaEquipo extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText etID,etDescripcion, etMarca, etModelo,etSerie;
    Button btConsultar,btActualizar;
    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    public ConsultaEquipo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConsultaEquipo.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultaEquipo newInstance(String param1, String param2) {
        ConsultaEquipo fragment = new ConsultaEquipo();
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
        // Inflate the layout for this fragment
        View vista= inflater.inflate(R.layout.fragment_consulta_equipo, container, false);

        etID=(EditText) vista.findViewById(R.id.etID);
        etDescripcion=(EditText)vista.findViewById(R.id.etDescripcion);
        etMarca=(EditText)vista.findViewById(R.id.etMarca);
        etModelo=(EditText)vista.findViewById(R.id.etModelo);
        etSerie=(EditText)vista.findViewById(R.id.etSerie);
        btConsultar=(Button)vista.findViewById(R.id.btConsulta);
        btActualizar=(Button)vista.findViewById(R.id.btModificar);

        request= Volley.newRequestQueue(getContext());

        btConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarWebService();
            }
        });

        btActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarWebServiceActualizar();
            }
        });

        return vista;
    }

    private void cargarWebService() {
        progreso=new ProgressDialog(getContext());
        progreso.setMessage("Cargando datos...");
        progreso.show();
        String url="http://sigequip.esy.es/obtenerEquipoPorId.php?idEquipo="
               +etID.getText().toString();

        jsonObjectRequest= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);

    }
    /*
    private void cargarWebServiceActualizar() {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Cargando datos...");
        progreso.show();
        String url = "http://http://sigequip.esy.es/actualizarEquipo.php";


        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }*/

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getContext(),"Se produjo un error..."+error.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
        //Toast.makeText(getContext(),"Mensaje: "+response,Toast.LENGTH_LONG).show();

        Equipos miEquipo= new Equipos();

        JSONArray json=response.optJSONArray("Equipos");
        JSONObject jsonObject=null;

        try {

            jsonObject=json.getJSONObject(0);
            miEquipo.setID(jsonObject.optInt("ID"));
            miEquipo.setDescripcion(jsonObject.optString("Descripcion"));
            miEquipo.setMarca(jsonObject.optString("Marca"));
            miEquipo.setModelo(jsonObject.optString("Modelo"));
            miEquipo.setSerie(jsonObject.optString("Serie"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        etDescripcion.setText(miEquipo.getDescripcion());
        etMarca.setText(miEquipo.getMarca());
        etModelo.setText(miEquipo.getModelo());
        etSerie.setText(miEquipo.getSerie());

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void cargarWebServiceActualizar() {
        progreso=new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();

        String url="http://http://sigequip.esy.es/actualizarEquipo.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso.hide();

                if (response.trim().equalsIgnoreCase("actualiza")) {
                    Toast.makeText(getContext(), "Se ha Actualizado con exito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "No se ha Actualizado ", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                progreso.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String descripcion = etDescripcion.getText().toString();
                String marca = etMarca.getText().toString();
                String modelo = etModelo.getText().toString();
                String serie = etSerie.getText().toString();

                Map<String, String> parametros = new HashMap<>();
                parametros.put("descripcion", descripcion);
                parametros.put("marca", marca);
                parametros.put("modelo", modelo);
                parametros.put("serie", serie);

                return parametros;
            }
        };
        request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(stringRequest);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
