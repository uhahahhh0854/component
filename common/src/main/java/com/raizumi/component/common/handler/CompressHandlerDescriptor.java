package com.raizumi.component.common.handler;

import com.raizumi.component.common.utils.ByteUtil;

import java.io.IOException;

public interface CompressHandlerDescriptor {
    default byte[] compress(byte[] bytes) throws IOException {
        return ByteUtil.gzip_compress(bytes);
    }

    default byte[] decompress(byte[] bytes) throws IOException {
        return ByteUtil.gzip_decompress(bytes);
    }

}
