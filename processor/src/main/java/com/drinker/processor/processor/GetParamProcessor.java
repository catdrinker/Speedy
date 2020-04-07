package com.drinker.processor.processor;

import com.drinker.processor.handler.IHandler;
import com.drinker.processor.handler.TrueHandler;
import com.drinker.processor.writter.MethodWriter;
import com.drinker.processor.writter.NoBodyMethodWriter;

public final class GetParamProcessor extends GetProcessor {
    @Override
    protected MethodWriter getWriter() {
        return new NoBodyMethodWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new TrueHandler();
    }
}
