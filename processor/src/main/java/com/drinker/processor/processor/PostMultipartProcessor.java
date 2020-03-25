package com.drinker.processor.processor;

import com.drinker.processor.handler.IHandler;
import com.drinker.processor.handler.MultipartHandler;
import com.drinker.processor.writter.MethodWriter;
import com.drinker.processor.writter.MultipartBodyWriter;

public final class PostMultipartProcessor extends HttpPostProcessor {
    @Override
    protected MethodWriter getWriter() {
        return new MultipartBodyWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new MultipartHandler();
    }
}
