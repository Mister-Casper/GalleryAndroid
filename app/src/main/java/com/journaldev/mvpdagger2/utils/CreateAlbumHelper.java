package com.journaldev.mvpdagger2.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.journaldev.mvpdagger2.R;

import java.io.File;
import java.util.ArrayList;

import static com.journaldev.mvpdagger2.utils.ImageUtils.getFileName;

public class CreateAlbumHelper {

    private ProgressBar statusCreateFolder;
    private TextView textStatus;
    private AlertDialog alertDialog;

    public void createImageFolder(Context context, ArrayList<Uri> images, String folderName) {
        File dataDir = createFolder(folderName);
        alertDialog = onCreateDialog(context);
        statusCreateFolder = alertDialog.findViewById(R.id.statusCreateFolder);
        textStatus = alertDialog.findViewById(R.id.countMove);
        TransferImages transferImages = new TransferImages();
        transferImages.execute(new TransferImagesParameters(images, context, dataDir.getPath()));
    }

    private AlertDialog onCreateDialog(Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.create_folder_status_dialog, null);
        alertDialog.setView(dialogView);
        return alertDialog.show();
    }

    private File createFolder(String folderName) {
        File dataDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + folderName);

        if (!dataDir.isDirectory()) {
            dataDir.mkdirs();
        }

        return dataDir;
    }

    private class TransferImages extends AsyncTask<TransferImagesParameters, String, Void> {
        Double progress = 0.;

        int countImageMove = 0;
        int countImageSync = 0;

        int countAllImage;
        int countAllImageSync;

        Context context;

        ArrayList<String> pathArray = new ArrayList<>();

        String folderPath;
        ArrayList<Uri> images;

        double stepMoveProgress;
        double stepSyncProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(TransferImagesParameters... transferImagesParameters) {
            loadParameters(transferImagesParameters);
            moveImage();
            syncImage();
            return null;
        }

        private void loadParameters(TransferImagesParameters... transferImagesParameters) {
            TransferImagesParameters parameters = transferImagesParameters[0];
            images = parameters.images;
            context = parameters.context;
            folderPath = parameters.folderPath;
            countAllImage = images.size();
            countAllImageSync = countAllImage * 2;
            stepMoveProgress = 20. / countAllImage;
            stepSyncProgress = 80. / countAllImageSync;
        }

        private void moveImage() {
            for (int i = 0; i < images.size(); i++) {
                moveImageToNewFolder(images.get(i), folderPath);
                progress += stepMoveProgress;
                countImageMove++;
                publishProgress(progress.toString(), "Перемещено изображений : " + countImageMove + " / " + countAllImage);
            }
        }

        private void syncImage() {
            MediaScannerConnection.scanFile(context, pathArray.toArray(new String[pathArray.size()]), null, (path, uri) ->
            {
                if (path.equals(pathArray.get(pathArray.size() - 1))) {
                    alertDialog.cancel();
                }
                progress += stepSyncProgress;
                countImageSync++;
                publishProgress(progress.toString(), "Синхранизировано изображений : " + countImageSync + " / " + countAllImageSync);
            });
        }

        @Override
        protected void onProgressUpdate(String... values) {
            statusCreateFolder.setProgress(Double.valueOf(values[0]).intValue());
            textStatus.setText(values[1]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        private void moveImageToNewFolder(Uri image, String folderPath) {
            if (image != null) {
                File imageFile = new File(image.getPath());
                File newImageView = new File(folderPath + File.separator + getFileName(image));
                imageFile.renameTo(newImageView);

                pathArray.add(imageFile.getPath());
                pathArray.add(newImageView.getPath());
            }
        }
    }

    private static class TransferImagesParameters {
        ArrayList<Uri> images;
        Context context;
        String folderPath;

        public TransferImagesParameters(ArrayList<Uri> images, Context context, String folderPath) {
            this.images = images;
            this.context = context;
            this.folderPath = folderPath;
        }
    }
}
