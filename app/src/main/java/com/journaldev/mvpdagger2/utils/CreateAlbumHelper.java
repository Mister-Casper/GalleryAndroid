package com.journaldev.mvpdagger2.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.journaldev.mvpdagger2.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.journaldev.mvpdagger2.utils.ImageUtils.getGlobalPath;

public class CreateAlbumHelper {

    ProgressBar statusCreateFolder;
    AlertDialog alertDialog;

    public void createImageFolder(Activity context, ArrayList<Uri> images, String folderName) {
        File dataDir = createFolder(folderName);
        alertDialog = onCreateDialog(context);
        statusCreateFolder = alertDialog.findViewById(R.id.statusCreateFolder);
        TransferImages transferImages = new TransferImages();
        transferImages.execute(new TransferImagesParameters(images, context, dataDir.getPath()));
    }

    private AlertDialog onCreateDialog(Activity context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Создание нового альбома");
        LayoutInflater inflater = context.getLayoutInflater();
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


    private File getFileFromBitmap(Bitmap image, String folderPath, String imageName) throws IOException {
        Bitmap bitmap = image;
        File file = new File(folderPath + "/" + imageName);
        FileOutputStream fOut = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        fOut.flush();
        fOut.close();
        return file;
    }

    private Bitmap getBitmapFromUri(Context context, Uri uriImage) throws IOException {
        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), getGlobalPath(context, uriImage.getPath()));
        return imageBitmap;
    }


    private class TransferImages extends AsyncTask<TransferImagesParameters, Double, Void> {

        double progress = 0;

        @Override
        protected Void doInBackground(TransferImagesParameters... transferImagesParameters) {
            TransferImagesParameters parameters = transferImagesParameters[0];
            ArrayList<Uri> images = parameters.images;
            Context context = parameters.context;
            String folderPath = parameters.folderPath;
            double stepProgress = 100 / images.size();

            try {
                for (int i = 0; i < images.size(); i++) {
                    progress += stepProgress;
                    publishProgress(progress);
                    addImageToFolder(context, images.get(i), folderPath);
                }
            } catch (IOException e) {
                return null;
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Double... values) {
            super.onProgressUpdate(values);
            statusCreateFolder.setProgress(values[0].intValue());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            alertDialog.cancel();
        }

        private void addImageToFolder(Context context, Uri image, String folderPath) throws IOException {
            Bitmap imageBitmap = getBitmapFromUri(context, image);
            File imageFile = getFileFromBitmap(imageBitmap, folderPath, ImageUtils.getFileName(context, image));
            if (!imageFile.getPath().equals(image.toString())) {
                imageFile.createNewFile();
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(imageFile));
                context.sendBroadcast(intent);
                ImageUtils.deleteImage(context.getContentResolver(), image);
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
