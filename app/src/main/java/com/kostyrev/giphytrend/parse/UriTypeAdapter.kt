package com.kostyrev.giphytrend.parse

import android.net.Uri
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class UriTypeAdapter : TypeAdapter<Uri>() {

    override fun write(writer: JsonWriter, value: Uri) {
        writer.value(value.toString())
    }

    override fun read(reader: JsonReader): Uri {
        require(reader.peek() == JsonToken.STRING)
        return Uri.parse(reader.nextString())
    }

}