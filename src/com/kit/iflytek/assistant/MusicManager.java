package com.kit.iflytek.assistant;

import com.kit.extend.iflytek.R;
import com.kit.iflytek.model.Answer;
import com.kit.iflytek.model.UnderstandResponse;
import com.kit.iflytek.model.music.MusicInfoWarpper;
import com.kit.utils.FileUtils;
import com.kit.utils.ListUtils;
import com.kit.utils.ResWrapper;
import com.kit.utils.StringUtils;
import com.kit.utils.log.ZogUtils;
import com.kit.utils.media.MediaUtils;
import com.kit.utils.media.MusicInfo;
import com.kit.utils.media.MusicLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhao on 16/7/21.
 */
public class MusicManager {

    private static MusicManager musicManager;


    public static MusicManager getInstance() {
        if (musicManager == null)
            musicManager = new MusicManager();

        return musicManager;
    }

    /**
     * 分发音乐
     *
     * @param understandResponse
     * @return
     */
    @SuppressWarnings("unchecked")
    public UnderstandResponse dispatch(UnderstandResponse understandResponse) {
        if (understandResponse == null)
            return null;

        String replyStr;

        switch (understandResponse.operation) {

            case Operation.PLAY:
                MusicLoader musicLoader = MusicLoader.getInstance();
                String songName = "";

                List<MusicInfo> list;
                if (understandResponse.semantic != null) {
                    songName = understandResponse.semantic.getSlots("song", String.class);
                    list = musicLoader.getMusicInfoByName(songName);
                } else {
                    list = musicLoader.getMusicList();
                }


                if (ListUtils.isNullOrEmpty(list)) {

                    understandResponse.operation = Operation.ASK_BACK;
                    if (StringUtils.isEmptyOrNullOrNullStr(songName)) {
                        songName = "";
                    } else {
                        songName = ":" + songName;
                    }

                    UnderstandResponse ur = understandResponse.clone();
                    replyStr = String.format(ResWrapper.getInstance().getString(R.string.search_no_music_local), songName);
                    ur.answer = AnswerManager.getInstance().creatAnswer(replyStr, Answer.Type.TEXT);

                    return ur;

                } else {


                    ArrayList<MusicInfoWarpper> musicInfoWarppers = new ArrayList<MusicInfoWarpper>();
                    for (MusicInfo musicInfo : list) {
                        musicInfoWarppers.add(MusicInfoWarpper.cast(musicInfo));
                    }
                    understandResponse.data.setResult(musicInfoWarppers);

                    if (list.size() == 1) {
                        MusicInfo musicInfo = list.get(0);
                        songName = FileUtils.getFilenameWithoutSuffix(musicInfo.getTitle());
                        ZogUtils.i("songName:" + songName);

                        replyStr = String.format(ResWrapper.getInstance().getString(R.string.playing_music), songName);
                        MediaUtils.playMusic("file://" + musicInfo.getUrl());
                    } else {
                        replyStr = ResWrapper.getInstance().getString(R.string.search_music);
                    }

                    understandResponse.answer = AnswerManager.getInstance().creatAnswer(replyStr, Answer.Type.TEXT);

                    return understandResponse;

                }

        }

        return understandResponse;


    }


    public class Operation {

        public static final String PLAY = "PLAY";


        /**
         * 反问
         */
        public static final String ASK_BACK = "ASK_BACK";

    }


}
