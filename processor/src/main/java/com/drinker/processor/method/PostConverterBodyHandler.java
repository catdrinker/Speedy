package com.drinker.processor.method;

import com.drinker.processor.handler.BodyConverterHandler;
import com.drinker.processor.handler.IHandler;
import com.drinker.processor.writter.ConverterBodyWriter;
import com.drinker.processor.writter.MethodWriter;

public class PostConverterBodyHandler extends HttpPostHandler {
    @Override
    protected MethodWriter getWriter() {
        return new ConverterBodyWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new BodyConverterHandler();
    }
}
