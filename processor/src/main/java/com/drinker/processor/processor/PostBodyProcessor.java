package com.drinker.processor.processor;

import com.drinker.processor.handler.IHandler;
import com.drinker.processor.handler.BodyHandler;
import com.drinker.processor.writter.BodyWriter;
import com.drinker.processor.writter.MethodWriter;

public final class PostBodyProcessor extends HttpPostProcessor {

    @Override
    protected MethodWriter getWriter() {
        return new BodyWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new BodyHandler();
    }

}
