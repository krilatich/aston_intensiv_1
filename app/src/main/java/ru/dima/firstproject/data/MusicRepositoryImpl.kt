package ru.dima.firstproject.data

import ru.dima.firstproject.R
import ru.dima.firstproject.data.model.Track

class MusicRepositoryImpl {


    fun getMusicList() = listOf(
        Track(
            name = "Linkin Park - Faint",
            srcAudio = R.raw.lp_faint,
            srcImage = R.drawable.faint_img
        ),
        Track(
            name = "дора - Дорадура",
            srcAudio = R.raw.dora_dura,
            srcImage = R.drawable.dora_img
        ),
        Track(
            name = "Billie Eilish - What was i made for",
            srcAudio = R.raw.billie_eilish,
            srcImage = R.drawable.billie_eilish_img
        )
    )


}