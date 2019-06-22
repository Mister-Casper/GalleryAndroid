package com.journaldev.mvpdagger2.fragments.ViewAllImagesByDate.view;


//grid:layout_columnWeight="1"

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.journaldev.mvpdagger2.Data.ImageUrls;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.adapters.GridPhotoAdapter;
import com.journaldev.mvpdagger2.fragments.ViewAllImagesByDate.model.ItemDate;
import com.journaldev.mvpdagger2.fragments.ViewAllImagesByDate.model.ViewAllImagesByDateModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewAllImagesByDate extends Fragment {


    @BindView(R.id.DataList)
    ListView DataList;
    Unbinder unbinder;
    ViewAllImagesByDateModel model = new ViewAllImagesByDateModel();

    public static ViewAllImagesByDate getInstance() {
        ViewAllImagesByDate fragment = new ViewAllImagesByDate();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragmentviewallimagesbydate, container, false);

        unbinder = ButterKnife.bind(this, view);

        model.init(getContext());

        Uri[] uri = ImageUrls.getUrls();
        ItemDate photo = new ItemDate(uri);

        final ArrayList<ItemDate> arrayList = new ArrayList<>();
        arrayList.add(photo);

        GridPhotoAdapter adapter = new GridPhotoAdapter(getActivity().getApplicationContext(), arrayList);
        DataList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
