package com.drinker.processor.method;

import com.drinker.processor.handler.IHandler;
import com.drinker.processor.handler.ParamMapHandler;
import com.drinker.processor.writter.MethodWriter;
import com.drinker.processor.writter.UrlMapWriter;

public class GetParamMapHandler extends GetHandler {
    @Override
    protected MethodWriter getWriter() {
        return new UrlMapWriter();
    }

    @Override
    protected IHandler getHandler() {
        return new ParamMapHandler();
    }
}
