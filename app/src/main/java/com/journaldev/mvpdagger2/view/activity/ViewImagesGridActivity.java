package com.journaldev.mvpdagger2.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.utils.ThemeUtils;
import com.journaldev.mvpdagger2.view.fragment.ImagesFragment.GridAlbumImagesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewImagesGridActivity extends AppCompatActivity {

    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.changeTheme(this, R.style.DarkTheme2, R.style.LightTheme2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_images_grid);
        ButterKnife.bind(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment baseGridImages = new GridAlbumImagesFragment();
        Bundle imagesBundle = getIntent().getExtras();
        baseGridImages.setArguments(imagesBundle);
        fragmentTransaction.add(R.id.imagesFragment, baseGridImages);
        fragmentTransaction.commit();

        setTitle(imagesBundle.getString("albumName"));
    }

    private void setTitle(String albumName) {
        title.setText(albumName);
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }
}
