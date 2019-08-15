package com.journaldev.mvpdagger2.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.journaldev.mvpdagger2.Data.AppPreference;
import com.journaldev.mvpdagger2.Data.ImageUrls;
import com.journaldev.mvpdagger2.Data.ItemPhotoData;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.adapters.ImagesPageAdapter;
import com.journaldev.mvpdagger2.myVIew.ImageViewTouchViewPager;
import com.journaldev.mvpdagger2.utils.OnSwipeTouchListener;
import com.journaldev.mvpdagger2.utils.ThemeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewImagesActivity extends AppCompatActivity {

    @BindView(R.id.pager)
    ImageViewTouchViewPager pager;
    ImagesPageAdapter mCustomPagerAdapter = null;
    @BindView(R.id.deleteImage)
    Button deleteImage;
    LinkedList<ItemPhotoData> uri;
    @BindView(R.id.likeImage)
    Button likeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prepareTheLook();
        super.onCreate(savedInstanceState);
        allScreen();
        setContentView(R.layout.viewimages);
        ButterKnife.bind(this);
        uri = getAllDataImage();
        getOtherIntent();
        initViewPager();
        processingChangeCurrentItem();
        if (getImageId() == 0)
            setLikeState(0);
    }


    private void prepareTheLook()
    {
        ThemeUtils.chandgeTheme(this, R.style.DarkTheme, R.style.LightTheme);
        setTitle("");
        transparentActionBar();
    }


    private void initViewPager() {
        pager.setOffscreenPageLimit(3);
        mCustomPagerAdapter = new ImagesPageAdapter(getApplicationContext(), uri);
        pager.setAdapter(mCustomPagerAdapter);
    }

    private void transparentActionBar() {
        ColorDrawable abDrawable = new ColorDrawable(getResources().getColor(R.color.gray));
        abDrawable.setAlpha(0);
        getSupportActionBar().setBackgroundDrawable(abDrawable);
    }


    private void processingChangeCurrentItem() {
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                setLikeState(position);
            }
        });
    }


    private LinkedList<ItemPhotoData> getAllDataImage() {
        LinkedList<ItemPhotoData> uri;
        ArrayList<String> strUri = getIntent().getStringArrayListExtra("uri");
        ArrayList<String> strLike = getIntent().getStringArrayListExtra("like");

        if (strUri != null) {
            uri = getAllImageDataFromIntent(strUri, strLike);
        } else
            uri = ImageUrls.getUrls(getApplicationContext());
        return uri;
    }

    private LinkedList<ItemPhotoData> getAllImageDataFromIntent(ArrayList<String> strUri, ArrayList<String> strLike) {
        LinkedList<ItemPhotoData> itemPhotoData = new LinkedList<>();

        for (int i = 0; i < strUri.size(); i++) {
            itemPhotoData.add(getImageDataFromIntent(i, strUri, strLike));
        }

        return itemPhotoData;
    }

    private ItemPhotoData getImageDataFromIntent(int id, ArrayList<String> strUri, ArrayList<String> strLike) {
        Uri uri = Uri.parse(strUri.get(id));
        Boolean isLike = Boolean.parseBoolean(strLike.get(id));
        return new ItemPhotoData(uri, isLike);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.file_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.info:
                return viewFileInfoActivity();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Boolean viewFileInfoActivity() {
        hideNavigationBar();
        Intent intent = new Intent(this, FIleInfo.class);
        Uri currentUri = mCustomPagerAdapter.getCurrentUri(pager.getCurrentItem()).getPhoto();
        intent.putExtra("uri", currentUri.toString());
        startActivity(intent);
        if (AppPreference.getIsAnim()) {
            overridePendingTransition(R.anim.back, R.anim.next);
        }
        return true;
    }

    private void hideNavigationBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void getOtherIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_VIEW.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                selectCurrentItemFromIntent(intent);
            }
        } else
            selectImage();
    }

    private void selectCurrentItemFromIntent(Intent intent) {
        String intentStr = intent.toString();

        final String intentFileName = getFileNameFromPath(intentStr);
        final String[] allFileName = getAllPath();

        int currentItem = getCurrentItem(intentFileName, allFileName);
        selectImageFromId(currentItem);
    }

    private String[] getAllPath() {
        String[] allFileName = new String[uri.size()];

        for (int i = 0; i < uri.size(); i++) {
            allFileName[i] = getFileNameFromPath(uri.get(i).toString());
        }

        return allFileName;
    }

    private int getCurrentItem(String intentFileName, String[] allFileName) {
        for (int i = 0; i < allFileName.length; i++) {
            if (allFileName[i].equals(intentFileName)) {
                return i;
            }
        }
        return -1;
    }

    private String getFileNameFromPath(String str) {
        String[] fileStr = getPath(str).split("/");
        String fileName = fileStr[fileStr.length - 1];
        return fileName;
    }

    private String getPath(String str) {
        int start = getIdStartPath(str);
        int end = getIdEndPath(str);

        if (start != -1 && end != -1)
            return str.substring(start, end);
        else
            return str;
    }

    private int getIdStartPath(String str) {
        String startStr = " dat=";
        int start = str.indexOf(startStr) + startStr.length();
        return start;
    }

    private int getIdEndPath(String str) {
        String endStr = " typ=";
        int end = str.indexOf(endStr);
        return end;
    }


    private void allScreen() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }


    private void selectImage() {
        selectImageFromId(getImageId());
    }

    private int getImageId() {
        return getIntent().getIntExtra("idImage", 0);
    }

    private void selectImageFromId(final int id) {
        pager.post(new Runnable() {
            @Override
            public void run() {
                pager.setCurrentItem(id, false);
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    @OnClick(R.id.deleteImage)
    public void clickDeleteImage() {
        AlertDialog.Builder dialog = createAlertDialogDeleteImage();
        dialog.show();
    }

    private AlertDialog.Builder createAlertDialogDeleteImage() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setMessage("Вы действительно хотите удалить изображение?");
        ad.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                deleteImage();
            }
        });
        ad.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
        return ad;
    }

    private void deleteImage() {
        ContentResolver contentResolver = getContentResolver();
        contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.ImageColumns.DATA + "=?", new String[]{uri.get(pager.getCurrentItem()).getPhoto().toString()});
        ImageUrls.isUpdate = true;
        int currentPosition = pager.getCurrentItem();
        uri.remove(currentPosition);
        viewPagerUpdate(currentPosition);
    }

    private void viewPagerUpdate(int currentPosition) {
        pager.setAdapter(mCustomPagerAdapter);
        pager.invalidate();
        pager.setCurrentItem(currentPosition);
    }


    @OnClick(R.id.likeImage)
    public void clickLikeImage(View view) {
        chandgeLikeState(view);
        Uri fileUri = mCustomPagerAdapter.getCurrentUri(pager.getCurrentItem()).getPhoto();
        Boolean like = view.isSelected();
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(fileUri.toString());
            exif.setAttribute(ExifInterface.TAG_USER_COMMENT, like.toString());
            exif.saveAttributes();
            ImageUrls.isUpdate = true;
            uri.get(pager.getCurrentItem()).setLike(like);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setLikeState(int imageId) {
        likeImage.setSelected(uri.get(imageId).getLike());
    }

    private void chandgeLikeState(View v) {
        v.setSelected(!v.isSelected());
    }

}