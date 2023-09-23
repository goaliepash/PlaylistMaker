package ru.goaliepash.playlistmaker.data.dto.response

import ru.goaliepash.playlistmaker.data.dto.TrackDto

class SearchResponse(val resultCount: Int, val results: List<TrackDto>) : Response()