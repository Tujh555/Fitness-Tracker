package com.example.fitnesstracker.data.rest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class StreamRequestBody extends RequestBody {
    private final @NonNull InputStream stream;

    public StreamRequestBody(@NonNull InputStream stream) {
        this.stream = stream;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return null;
    }

    @Override
    public void writeTo(@NonNull BufferedSink bufferedSink) throws IOException {
        final byte[] buffer = new byte[1024 * 16];
        while (true) {
            final var readBytes = stream.read(buffer);
            if (readBytes < 0) break;
            bufferedSink.write(buffer, 0, readBytes);
        }
    }
}
