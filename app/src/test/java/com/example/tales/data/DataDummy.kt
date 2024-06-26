package com.example.tales.data

import com.example.tales.model.Story

object DataDummy {
    fun generateDummyStories(): List<Story> {
        val stories = ArrayList<Story>()
        for (i in 0..1) {
            val story = Story(
                id = "$i",
                name = "Name $i",
                description = "Description $i",
                photoUrl = "photoUrl$i",
                lat = i.toDouble(),
                lon = i.toDouble()
            )
            stories.add(story)
        }
        return stories
    }
}
