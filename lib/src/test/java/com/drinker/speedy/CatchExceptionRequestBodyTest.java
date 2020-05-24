package com.drinker.speedy;

import org.junit.Test;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class CatchExceptionRequestBodyTest {

    @Test
    public void getBodyTest() throws IOException {
        StringConverterFactory converterFactory = new StringConverterFactory();
        TypeToken2<String> typeToken2 = new TypeToken2<String>() {
        };

        Converter<String, RequestBody> reqBodyConverter = converterFactory.reqBodyConverter(typeToken2);
        CatchExceptionRequestBody<String> catchBody = new CatchExceptionRequestBody<>(reqBodyConverter);
        RequestBody finalBody = catchBody.getBody("hello");

        assert finalBody.contentLength() == 5;
        assert MediaType.get("application/form-data; charset=utf-8").equals(finalBody.contentType());
    }


    @Test
    public void getBodyNullPointExceptionTest() {
        StringConverterFactory converterFactory = new StringConverterFactory();
        TypeToken2<String> typeToken2 = new TypeToken2<String>() {
        };

        Converter<String, RequestBody> reqBodyConverter = converterFactory.reqBodyConverter(typeToken2);
        CatchExceptionRequestBody<String> catchBody = new CatchExceptionRequestBody<>(reqBodyConverter);
        RequestBody finalBody = null;

        Throwable exception = null;
        try {
            finalBody = catchBody.getBody(null);
        } catch (Throwable e) {
            exception = e;
        }
        assert finalBody == null;
        assert exception instanceof NullPointerException;
    }

    @Test
    public void getBodyIOExceptionTest() {
        IOExceptionConverterFactory converterFactory = new IOExceptionConverterFactory();
        TypeToken2<String> typeToken2 = new TypeToken2<String>() {
        };

        Converter<String, RequestBody> reqBodyConverter = converterFactory.reqBodyConverter(typeToken2);
        CatchExceptionRequestBody<String> catchBody = new CatchExceptionRequestBody<>(reqBodyConverter);
        RequestBody finalBody = null;

        Throwable exception = null;
        try {
            finalBody = catchBody.getBody("hello");
        } catch (Throwable e) {
            exception = e;
        }

        assert finalBody == null;
        assert exception instanceof IllegalStateException;
        assert ("can't transform with requestBody converter hello").equals(exception.getMessage());
    }

    static class StringConverterFactory implements Converter.Factory {
        @Nonnull
        @Override
        public <T> Converter<T, RequestBody> reqBodyConverter(@Nonnull final TypeToken2<T> token) {
            return new Converter<T, RequestBody>() {
                @Nullable
                @Override
                public RequestBody transform(T value) {
                    if (token.getType() == String.class) {
                        return RequestBody.create(MediaType.get("application/form-data"), (String) value);
                    }
                    return null;
                }
            };
        }

        @Nonnull
        @Override
        public <T> Converter<ResponseBody, T> respBodyConverter(@Nonnull TypeToken2<T> token) {
            return null;
        }
    }


    static class IOExceptionConverterFactory implements Converter.Factory {

        @Nonnull
        @Override
        public <T> Converter<T, RequestBody> reqBodyConverter(@Nonnull TypeToken2<T> token) {
            return new Converter<T, RequestBody>() {
                @Nullable
                @Override
                public RequestBody transform(T value) throws IOException {
                    throw new IOException("failure");
                }
            };
        }

        @Nonnull
        @Override
        public <T> Converter<ResponseBody, T> respBodyConverter(@Nonnull TypeToken2<T> token) {
            return null;
        }
    }
}