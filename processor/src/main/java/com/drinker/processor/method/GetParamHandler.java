package com.drinker.processor.method;

import com.drinker.processor.handler.IHandler;
import com.drinker.processor.handler.TrueHandler;
import com.drinker.processor.writter.MethodWriter;
import com.drinker.processor.writter.NoBodyMethodWriter;

public class GetParamHandler extends GetHandler {
    @Override
    protected MethodWriter getWriter() {
        return new NoBodyMethodWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new TrueHandler();
    }
}
