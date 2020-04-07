package com.drinker.processor.processor;

import com.drinker.processor.handler.BodyConverterHandler;
import com.drinker.processor.handler.IHandler;
import com.drinker.processor.writter.ConverterBodyWriter;
import com.drinker.processor.writter.MethodWriter;

public final class PostConverterBodyProcessor extends HttpPostProcessor {
    @Override
    protected MethodWriter getWriter() {
        return new ConverterBodyWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new BodyConverterHandler();
    }
}
