package com.drinker.processor.method;

import com.drinker.processor.IHandler;
import com.drinker.processor.handler.MultipartMapHandler;
import com.drinker.processor.writter.MethodWriter;
import com.drinker.processor.writter.MultipartMapWriter;

public final class PutMultipartMapHandler extends HttpPutHandler {
    @Override
    protected MethodWriter getWriter() {
        return new MultipartMapWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new MultipartMapHandler();
    }
}
