package com.drinker.processor.method;

import com.drinker.processor.handler.IHandler;
import com.drinker.processor.handler.FormMapHandler;
import com.drinker.processor.writter.FormMapWriter;
import com.drinker.processor.writter.MethodWriter;

public final class PutFormMapHandler extends HttpPutHandler {
    @Override
    protected MethodWriter getWriter() {
        return new FormMapWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new FormMapHandler();
    }
}
