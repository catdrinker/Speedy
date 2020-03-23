package com.drinker.processor.method;

import com.drinker.processor.IHandler;
import com.drinker.processor.handler.BodyHandler;
import com.drinker.processor.writter.BodyWriter;
import com.drinker.processor.writter.MethodWriter;

public final class PutBodyHandler extends HttpPutHandler {
    @Override
    protected MethodWriter getWriter() {
        return new BodyWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new BodyHandler();
    }
}
