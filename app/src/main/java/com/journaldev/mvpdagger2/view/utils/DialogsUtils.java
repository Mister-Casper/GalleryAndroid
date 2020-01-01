package com.journaldev.mvpdagger2.view.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.application.App;

public class DialogsUtils {

    public interface DialogsListener {
        void createAlbum(String albumName);
    }

    public static void showAlbumNameDialog(final Context context, String message, DialogsListener dialogsListener) {
        AlertDialog createAlbumDialog = new AlertDialog.Builder(context)
                .setTitle(message).create();
        View createAlbumView = setViewToDialog(context, createAlbumDialog, R.layout.create_new_album_dialog);

        createAlbumView.findViewById(R.id.cancel).setOnClickListener(view -> createAlbumDialog.cancel());

        createAlbumView.findViewById(R.id.ok).setOnClickListener(view ->
                createAlbum(createAlbumView,dialogsListener,createAlbumDialog));

        createAlbumDialog.show();
    }

    private static void createAlbum(View dialogView , DialogsListener dialogsListener, AlertDialog createAlbumDialog){
        EditText albumText = dialogView.findViewById(R.id.albumName);
        String albumName = albumText.getText().toString();
        if(albumName.length() != 0 ) {
            dialogsListener.createAlbum(albumText.getText().toString());
            createAlbumDialog.cancel();
        }else
            albumText.setError(App.getApp().getString(R.string.enter_album_name));
    }


    private static View setViewToDialog(Context context, AlertDialog alertDialog, int layoutId) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(layoutId, null);
        alertDialog.setView(dialogView);
        return dialogView;
    }

}
