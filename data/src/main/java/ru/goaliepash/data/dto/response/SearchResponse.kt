package ru.goaliepash.data.dto.response

import ru.goaliepash.data.dto.TrackDto

class SearchResponse(val resultCount: Int, val results: List<TrackDto>) : Response()