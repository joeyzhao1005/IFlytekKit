package com.kit.iflytek.model.music;

import android.graphics.drawable.Drawable;

import com.kit.iflytek.model.DataWarpper;
import com.kit.utils.ResWrapper;
import com.kit.utils.StringUtils;
import com.kit.utils.media.MediaUtils;
import com.kit.utils.media.MusicInfo;

/**
 * Created by Zhao on 16/7/31.
 */
public class MusicInfoWarpper implements DataWarpper {

    private MusicInfo musicInfo;


    public MusicInfo getMusicInfo() {
        return musicInfo;
    }

    public void setMusicInfo(MusicInfo musicInfo) {
        this.musicInfo = musicInfo;
    }


    @Override
    public String getTitle() {
        return musicInfo.getTitle();
    }

    @Override
    public String getContent() {
        return musicInfo.getArtist();
    }


    @Override
    public Drawable getIcon() {
        if (!StringUtils.isEmptyOrNullOrNullStr((musicInfo.getUrl()))) {
            return MediaUtils.createAlbumArt(musicInfo.getUrl());
        } else {
            return ResWrapper.getInstance().getDrawable(android.R.drawable.ic_media_play);
        }
    }


    public static MusicInfoWarpper cast(MusicInfo musicInfo) {
        MusicInfoWarpper packageInfoWarpper = new MusicInfoWarpper();
        packageInfoWarpper.setMusicInfo(musicInfo);
        return packageInfoWarpper;
    }

}
