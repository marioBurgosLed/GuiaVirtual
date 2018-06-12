package com.example.note.guiavirtual.Equipos;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.note.guiavirtual.AppController;
import com.example.note.guiavirtual.MainActivity;
import com.example.note.guiavirtual.R;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AltaEquipo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AltaEquipo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AltaEquipo extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //private static String TAG = MainActivity.class.getSimpleName();

    //private static final String REGISTER_URL = "http://sigequip.esy.es/insertarEquipo.php";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    static final String REGISTER_URL = "http://sigequip.esy.es/InsertaEquipo2.php";

    private EditText descripcion, marca, modelo, serie;
    Button alta;
    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    TextView texto;

    private OnFragmentInteractionListener mListener;

    public AltaEquipo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AltaEquipo.
     */
    // TODO: Rename and change types and number of parameters
    public static AltaEquipo newInstance(String param1, String param2) {
        AltaEquipo fragment = new AltaEquipo();
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

        View vista = inflater.inflate(R.layout.fragment_alta_equipo, container, false);

        descripcion=(EditText) vista.findViewById(R.id.etDescripcion);
        marca=(EditText) vista.findViewById(R.id.etMarca);
        modelo=(EditText) vista.findViewById(R.id.etModelo);
        serie=(EditText) vista.findViewById(R.id.etSerie);
        texto=(TextView) vista.findViewById(R.id.textView7);

        alta=(Button) vista.findViewById(R.id.btAlta);

        request= Volley.newRequestQueue(getContext());

        alta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AltaRegistros();
            }
        });

        return vista;
    }


  private void AltaRegistros() {
        String url = "http://sigequip.esy.es/insertarEquipo2.php?Descripcion="
                + descripcion.getText().toString()+
                "&Marca=" + marca.getText().toString()+
                "&Modelo=" + modelo.getText().toString() +
                "&Serie=" + serie.getText().toString();

        url=url.replace(" ","%20");

        jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url, null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getContext(),"Se guardó el registro...",Toast.LENGTH_LONG).show();
        descripcion.setText("");
        marca.setText("");
        modelo.setText("");
        serie.setText("");
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(),"Se produjo un error..."+error.toString(),Toast.LENGTH_LONG).show();

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
