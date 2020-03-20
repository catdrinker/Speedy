package com.drinker.speedy;


import java.io.IOException;

import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Timeout;

/**
 * 参考 Retrofit OkHttpCall 结合apt 实际需要内容改造
 *
 * some code copy from {@code retrofit.OkHttpCall} and thank's for retrofit https://github.com/square/retrofit
 *
 * @param <T>
 */
public class WrapperCall<T> implements Call<T> {

    private okhttp3.Call rawCall;
    private okhttp3.Call.Factory callFactory;
    private Request rawRequest;
    private Converter<ResponseBody, T> converter;
    private IDelivery delivery;

    private boolean isExecuted;

    public WrapperCall(Converter<ResponseBody, T> converter, IDelivery delivery, okhttp3.Call rawCall, okhttp3.Call.Factory callFactory, Request rawRequest) {
        this.converter = converter;
        this.delivery = delivery;
        this.rawCall = rawCall;
        this.callFactory = callFactory;
        this.rawRequest = rawRequest;
    }

    @Override
    public Response<T> execute() throws IOException {
        assert rawCall != null;
        synchronized (this) {
            if (isExecuted) {
                throw new IllegalStateException("one call only can execute one time");
            }
            isExecuted = true;
        }
        return parseResponse(rawCall.execute());
    }

    @Override
    public void enqueue(final Callback<T> callback) {
        assert rawCall != null;
        synchronized (this) {
            if (isExecuted) {
                throw new IllegalStateException("one call only can execute one time");
            }
            isExecuted = true;
        }
        rawCall.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, final IOException e) {
                delivery.delivery(new IDeliveryTask() {
                    @Override
                    public void exec() {
                        callback.onFailure(WrapperCall.this, e);
                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response rawResponse) {
                delivery.delivery(new IDeliveryTask() {
                    @Override
                    public void exec() {
                        Response<T> response;
                        try {
                            response = parseResponse(rawResponse);
                        } catch (Throwable e) {
                            throwIfFatal(e);
                            callFailure(e);
                            return;
                        }

                        try {
                            callback.onResponse(WrapperCall.this, response);
                        } catch (Throwable t) {
                            throwIfFatal(t);
                            t.printStackTrace(); // TODO this is not great
                        }
                    }
                });
            }

            private void callFailure(Throwable e) {
                try {
                    callback.onFailure(WrapperCall.this, e);
                } catch (Throwable t) {
                    throwIfFatal(t);
                    t.printStackTrace(); // TODO this is not great
                }
            }
        });
    }

    static ResponseBody buffer(final ResponseBody body) throws IOException {
        Buffer buffer = new Buffer();
        body.source().readAll(buffer);
        return ResponseBody.create(body.contentType(), body.contentLength(), buffer);
    }

    private Response<T> parseResponse(okhttp3.Response rawResponse) throws IOException {
        ResponseBody rawBody = rawResponse.body();

        // Remove the body's source (the only stateful object) so we can pass the response along.
        rawResponse = rawResponse.newBuilder()
                .body(new NoContentResponseBody(rawBody.contentType(), rawBody.contentLength()))
                .build();

        int code = rawResponse.code();
        if (code < 200 || code >= 300) {
            try {
                // Buffer the entire body to avoid future I/O.
                ResponseBody bufferedBody = buffer(rawBody);
                return Response.error(bufferedBody, rawResponse);
            } finally {
                rawBody.close();
            }
        }

        if (code == 204 || code == 205) {
            rawBody.close();
            return Response.success(null, rawResponse);
        }

        ExceptionCatchingResponseBody catchingBody = new ExceptionCatchingResponseBody(rawBody);
        try {
            T body = converter.transform(catchingBody);
            return Response.success(body, rawResponse);
        } catch (RuntimeException e) {
            // If the underlying source threw an exception, propagate that rather than indicating it was
            // a runtime exception.
            catchingBody.throwIfCaught();
            throw e;
        }
    }

    public void cancel() {
        if (!rawCall.isCanceled()) {
            rawCall.cancel();
        }
    }

    public Timeout timeout() {
        return rawCall.timeout();
    }

    public okhttp3.Call.Factory getCallFactory() {
        return callFactory;
    }

    public Request getRawRequest() {
        return rawRequest;
    }

    static void throwIfFatal(Throwable t) {
        if (t instanceof VirtualMachineError) {
            throw (VirtualMachineError) t;
        } else if (t instanceof ThreadDeath) {
            throw (ThreadDeath) t;
        } else if (t instanceof LinkageError) {
            throw (LinkageError) t;
        }
    }

    static final class NoContentResponseBody extends ResponseBody {
        private final @Nullable
        MediaType contentType;
        private final long contentLength;

        NoContentResponseBody(@Nullable MediaType contentType, long contentLength) {
            this.contentType = contentType;
            this.contentLength = contentLength;
        }

        @Override
        public MediaType contentType() {
            return contentType;
        }

        @Override
        public long contentLength() {
            return contentLength;
        }

        @Override
        public BufferedSource source() {
            throw new IllegalStateException("Cannot read raw response body of a converted body.");
        }
    }

    static final class ExceptionCatchingResponseBody extends ResponseBody {
        private final ResponseBody delegate;
        private final BufferedSource delegateSource;
        @Nullable IOException thrownException;

        ExceptionCatchingResponseBody(ResponseBody delegate) {
            this.delegate = delegate;
            this.delegateSource = Okio.buffer(new ForwardingSource(delegate.source()) {
                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    try {
                        return super.read(sink, byteCount);
                    } catch (IOException e) {
                        thrownException = e;
                        throw e;
                    }
                }
            });
        }

        @Override
        public MediaType contentType() {
            return delegate.contentType();
        }

        @Override
        public long contentLength() {
            return delegate.contentLength();
        }

        @Override
        public BufferedSource source() {
            return delegateSource;
        }

        @Override
        public void close() {
            delegate.close();
        }

        void throwIfCaught() throws IOException {
            if (thrownException != null) {
                throw thrownException;
            }
        }
    }

}
