package com.drinker.processor.method;

import com.drinker.processor.handler.IHandler;
import com.drinker.processor.handler.MultipartHandler;
import com.drinker.processor.writter.MethodWriter;
import com.drinker.processor.writter.MultipartBodyWriter;

public final class PostMultipartHandler extends HttpPostHandler {
    @Override
    protected MethodWriter getWriter() {
        return new MultipartBodyWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new MultipartHandler();
    }
}
