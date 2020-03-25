package com.drinker.processor.processor;

import com.drinker.processor.handler.IHandler;
import com.drinker.processor.handler.MultipartMapHandler;
import com.drinker.processor.writter.MethodWriter;
import com.drinker.processor.writter.MultipartMapWriter;

public final class PostMultipartMapProcessor extends HttpPostProcessor {
    @Override
    protected MethodWriter getWriter() {
        return new MultipartMapWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new MultipartMapHandler();
    }
}
