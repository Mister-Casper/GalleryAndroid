package com.journaldev.mvpdagger2.model.Selectable;

import android.net.Uri;

public interface AlbumSelectable extends Selectable {

    Uri getUri(int position);
    String getName();

}
