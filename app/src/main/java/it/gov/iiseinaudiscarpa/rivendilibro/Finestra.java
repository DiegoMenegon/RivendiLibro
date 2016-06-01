package it.gov.iiseinaudiscarpa.rivendilibro;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.support.v4.app.DialogFragment;

/**
 * Created by menegondiego on 01/06/2016.
 */
public class Finestra {
    private EditText mEditText;

    public Finestra() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog, container);
        mEditText = (EditText) view.findViewById(R.id.txt_your_name);


        return view;
    }

}
