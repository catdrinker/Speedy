package com.drinker.processor.method;

import com.drinker.processor.handler.IHandler;
import com.drinker.processor.handler.FormHandler;
import com.drinker.processor.writter.FormBodyWriter;
import com.drinker.processor.writter.MethodWriter;

public final class PostFormHandler extends HttpPostHandler {
    @Override
    protected MethodWriter getWriter() {
        return new FormBodyWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new FormHandler();
    }
}
