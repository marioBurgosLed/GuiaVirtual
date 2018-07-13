package com.example.note.guiavirtual.Equipos;




import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.note.guiavirtual.R;
import com.example.note.guiavirtual.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

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
public class ConsultaEquipo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText etID,etDescripcion, etMarca, etModelo,etSerie;
    TextView txtPrueba;
    Button btConsultar,btActualizar,btEliminar;
    ProgressDialog pDialog;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;

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
        btEliminar=(Button)vista.findViewById(R.id.btEliminar);
        txtPrueba=(TextView)vista.findViewById(R.id.txtPrueba);

        request= Volley.newRequestQueue(getContext());

        btConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarWebService();
            }
        });

        btEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webServiceEliminar();
            }
        });

        btActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webServiceActualizar();
            }
        });

        return vista;
    }

    ///////////////////////////////////////////////////////////////


    private void cargarWebService() {
        pDialog=new ProgressDialog(getContext());
        pDialog.setMessage("Cargando datos...");
        pDialog.show();
        final String url="http://sigequip.esy.es/obtenerEquipoPorId2.php?idEquipo="
                +etID.getText().toString();

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.hide();

                Equipos miEquipo=new Equipos();

                JSONArray json=response.optJSONArray("equipo");
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
                System.out.println();
                pDialog.hide();
                Log.d("ERROR: ", error.toString());
            }
        });

        // request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private void webServiceActualizar() {
        pDialog=new ProgressDialog(getContext());
        pDialog.setMessage("Cargando...");
        pDialog.show();


        String urlActualiza="http://sigequip.esy.es/actualizarEquipo2.php";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, urlActualiza, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();

                if (response.trim().equalsIgnoreCase("actualiza")){
                    Toast.makeText(getContext(),"Se ha Actualizado con exito",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"No se ha Actualizado ",Toast.LENGTH_SHORT).show();
                    Log.i("RESPUESTA: ",""+response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"..."+error.toString(),Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String id = etID.getText().toString();
                String desc = etDescripcion.getText().toString();
                String ma = etMarca.getText().toString();
                String mo = etModelo.getText().toString();
                String se = etSerie.getText().toString();

                Map<String, String> parametros = new HashMap<>();
                parametros.put("idEquipo", id);
                parametros.put("Descripcion", desc);
                parametros.put("Marca", ma);
                parametros.put("Modelo", mo);
                parametros.put("Serie", se);

                return parametros;

            }
        };
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(stringRequest);
    }

    /////////////////////////////////////////////////////////////////////////////////////

    private void webServiceEliminar() {
        pDialog=new ProgressDialog(getContext());
        pDialog.setMessage("Cargando...");
        pDialog.show();


        String urlElimina="http://sigequip.esy.es/borrarEquipo2.php?idEquipo="+etID.getText().toString();

        stringRequest=new StringRequest(Request.Method.GET, urlElimina, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();

                if (response.trim().equalsIgnoreCase("elimina")){
                    etID.setText("");
                    etDescripcion.setText("");
                    etMarca.setText("");
                    etModelo.setText("");
                    etSerie.setText("");

                    Toast.makeText(getContext(),"Se ha Eliminado con exito",Toast.LENGTH_SHORT).show();
                }else{
                    if (response.trim().equalsIgnoreCase("noExiste")){
                        Toast.makeText(getContext(),"No se encuentra el registro ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }else{
                        Toast.makeText(getContext(),"No se ha Eliminado ",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        });
        //request.add(stringRequest);
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


