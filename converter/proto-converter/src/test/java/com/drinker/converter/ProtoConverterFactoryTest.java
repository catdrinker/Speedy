package com.drinker.converter;

import com.drinker.speedy.Converter;
import com.drinker.speedy.TypeToken2;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import com.speedy.protobuf.PhoneProtos;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class ProtoConverterFactoryTest {

    private ProtoConverterFactory protoConverterFactory;

    @Before
    public void setUp() throws Exception {
        ExtensionRegistry registry = ExtensionRegistry.newInstance();
        PhoneProtos.registerAllExtensions(registry);
        protoConverterFactory = ProtoConverterFactory.create(registry);
    }

    @After
    public void tearDown() throws Exception {
        protoConverterFactory = null;
    }

    @Test
    public void reqBodySuccessTest() throws IOException {
        PhoneProtos.Phone phone = PhoneProtos.Phone.newBuilder()
                .setNumber("10086")
                .build();

        TypeToken2<PhoneProtos.Phone> typeToken2 = new TypeToken2<PhoneProtos.Phone>() {
        };
        Converter<PhoneProtos.Phone, RequestBody> reqConverter = protoConverterFactory.reqBodyConverter(typeToken2);
        RequestBody requestBody = reqConverter.transform(phone);
        assert requestBody != null;
        MediaType mediaType = requestBody.contentType();
        assert mediaType != null;
        assert "application/x-protobuf".equals(mediaType.toString());
    }

    @Test
    public void respBodySuccessTest() throws IOException {
        TypeToken2<PhoneProtos.Phone> typeToken2 = new TypeToken2<PhoneProtos.Phone>() {
        };

        Converter<ResponseBody, PhoneProtos.Phone> respBodyConverter = protoConverterFactory.respBodyConverter(typeToken2);
        PhoneProtos.Phone phone = PhoneProtos.Phone.newBuilder()
                .setNumber("10086")
                .build();
        byte[] bytes = phone.toByteArray();
        ResponseBody responseBody = ResponseBody.create(MultipartBody.FORM, bytes);
        PhoneProtos.Phone transformPhone = respBodyConverter.transform(responseBody);
        assert transformPhone != null;
        assert transformPhone.hasNumber();
        String number = transformPhone.getNumber();
        assert "10086".equals(number);
    }


    @Test
    public void reqBodyNotClassTest() {
        TypeToken2<int[]> typeToken2 = new TypeToken2<int[]>() {
        };
        Throwable e = null;
        try {
            protoConverterFactory.reqBodyConverter(typeToken2);
        } catch (Throwable throwable) {
            e = throwable;
        }
        assert e instanceof IllegalStateException;
        assert "token type must be cast Class<?> with protobuf".equals(e.getMessage());
    }

    @Test
    public void reqBodyNotMessageLiteTest() {
        TypeToken2<User> typeToken2 = new TypeToken2<User>() {
        };
        Throwable e = null;
        try {
            protoConverterFactory.reqBodyConverter(typeToken2);
        } catch (Throwable throwable) {
            e = throwable;
        }
        assert e instanceof IllegalStateException;
        assert "token type must be assignableFrom MessageLite".equals(e.getMessage());
    }


    @Test
    public void respBodyNotClassTest() {
        TypeToken2<int[]> typeToken2 = new TypeToken2<int[]>() {
        };
        Throwable e = null;
        try {
            protoConverterFactory.respBodyConverter(typeToken2);
        } catch (Throwable throwable) {
            e = throwable;
        }
        assert e instanceof IllegalStateException;
        assert "token type must be cast Class<?> with protobuf".equals(e.getMessage());
    }

    @Test
    public void respBodyNotMessageLiteTest() {
        TypeToken2<User> typeToken2 = new TypeToken2<User>() {
        };
        Throwable e = null;
        try {
            protoConverterFactory.respBodyConverter(typeToken2);
        } catch (Throwable throwable) {
            e = throwable;
        }
        assert e instanceof IllegalStateException;
        assert "token type must be assignableFrom MessageLite".equals(e.getMessage());
    }

    @Test
    public void respBodyNoParseTest() {
        TypeToken2<HomeProto> typeToken2 = new TypeToken2<HomeProto>() {
        };
        Throwable e = null;
        try {
            protoConverterFactory.respBodyConverter(typeToken2);
        } catch (Throwable throwable) {
            e = throwable;
        }
        assert e instanceof IllegalArgumentException;
        assert "Found a protobuf message but com.drinker.converter.ProtoConverterFactoryTest$HomeProto had no parser() method or PARSER field.".equals(e.getMessage());
    }

    static class HomeProto implements MessageLite {

        @Override
        public void writeTo(CodedOutputStream output) throws IOException {

        }

        @Override
        public int getSerializedSize() {
            return 0;
        }

        @Override
        public Parser<? extends MessageLite> getParserForType() {
            return null;
        }

        @Override
        public ByteString toByteString() {
            return null;
        }

        @Override
        public byte[] toByteArray() {
            return new byte[0];
        }

        @Override
        public void writeTo(OutputStream output) throws IOException {

        }

        @Override
        public void writeDelimitedTo(OutputStream output) throws IOException {

        }

        @Override
        public Builder newBuilderForType() {
            return null;
        }

        @Override
        public Builder toBuilder() {
            return null;
        }

        @Override
        public MessageLite getDefaultInstanceForType() {
            return null;
        }

        @Override
        public boolean isInitialized() {
            return false;
        }
    }


    static class User {
        String name;
        String num;
    }

}