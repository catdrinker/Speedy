package com.drinker.processor.method;

import com.drinker.processor.IHandler;
import com.drinker.processor.handler.FormHandler;
import com.drinker.processor.writter.FormBodyWriter;
import com.drinker.processor.writter.MethodWriter;

public final class PutFormHandler extends HttpPutHandler {
    @Override
    protected MethodWriter getWriter() {
        return new FormBodyWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new FormHandler();
    }
}
