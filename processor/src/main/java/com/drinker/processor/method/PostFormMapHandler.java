package com.drinker.processor.method;

import com.drinker.processor.IHandler;
import com.drinker.processor.handler.FormMapHandler;
import com.drinker.processor.writter.FormMapWriter;
import com.drinker.processor.writter.MethodWriter;

public final class PostFormMapHandler extends HttpPostHandler {

    @Override
    protected MethodWriter getWriter() {
        return new FormMapWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new FormMapHandler();
    }
}
