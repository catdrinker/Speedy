package com.drinker.processor.processor;

import com.drinker.processor.handler.IHandler;
import com.drinker.processor.handler.ParamMapHandler;
import com.drinker.processor.writter.MethodWriter;
import com.drinker.processor.writter.UrlMapWriter;

public final class GetParamMapProcessor extends GetProcessor {
    @Override
    protected MethodWriter getWriter() {
        return new UrlMapWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new ParamMapHandler();
    }
}
