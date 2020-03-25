package com.drinker.processor.processor;

import com.drinker.processor.handler.IHandler;
import com.drinker.processor.handler.FormMapHandler;
import com.drinker.processor.writter.FormMapWriter;
import com.drinker.processor.writter.MethodWriter;

public final class PostFormMapProcessor extends HttpPostProcessor {

    @Override
    protected MethodWriter getWriter() {
        return new FormMapWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new FormMapHandler();
    }
}
