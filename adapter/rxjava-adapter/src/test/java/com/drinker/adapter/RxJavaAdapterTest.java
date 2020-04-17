package com.drinker.adapter;

import com.drinker.speedy.WrapperCall;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

public class RxJavaAdapterTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void adapt() {
        final Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                System.out.println("call subscriber");
                subscriber.onNext("hello");
                subscriber.onError(new IOException("DDSDSDSDS"));
                subscriber.onNext("sdsds");
                subscriber.onCompleted();
            }
        });

        observable.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("oncompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("on error " + e);
            }

            @Override
            public void onNext(String s) {
                System.out.println("onnext " + s);
            }
        });


    }

    @Test
    public void testRxJavaAdapter() {
        RxJavaAdapter<User<Home>> userRxJavaAdapter = new RxJavaAdapter<>(true);

    }

    @Test
    public void test() {
        float testmoney = testmoney(3000);
        System.out.println("testmoney" + testmoney);
    }

    private float testmoney(int money) {
        float roomPay = money * 0.25f;
        float pricePay = money * 0.4f;
        return money + roomPay + pricePay;
    }

    static class Home {
        int value;
        String h;

        @Override
        public String toString() {
            return "value = " + value
                    + " h = " + h;
        }
    }

    static class User<T> {
        T data;
        String name;
        String value;

        @Override
        public String toString() {
            return "data = " + data
                    + " name = " + name
                    + " value = " + value;
        }
    }

}