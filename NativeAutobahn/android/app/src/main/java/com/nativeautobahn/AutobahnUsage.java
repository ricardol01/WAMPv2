package com.nativeautobahn;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.lang.reflect.Array;
import java.util.concurrent.TimeUnit;

import io.netty.handler.codec.serialization.ObjectEncoder;
import rx.Observable;import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import ws.wamp.jawampa.WampClient;
import ws.wamp.jawampa.WampClientBuilder;
import ws.wamp.jawampa.transport.netty.NettyWampClientConnectorProvider;

public class AutobahnUsage extends ReactContextBaseJavaModule {
    ReactApplicationContext mReactContext;
    public AutobahnUsage(ReactApplicationContext reactContext){
        super(reactContext);
        mReactContext=reactContext;
    }
    @Override
    public String getName() {
        return "AutobahnUsage";
    }


//    public class  MSG{
//        private int number;
//        private  String string;
//    }
    WampClient client;
    int lastEventValue=0;
    Subscription procSubscription,subscription,addProcSubscription,eventPublication,eventSubscription;
    private void start() {
    //    WampClient client;
        String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxIiwiZXhwaXJlZCI6MTQ5MjExMzYwMCwibGFzdGxvZ2luIjoxNDg2MDYwMjUwfQ.KTlYJvFNJAXuFOd9CnfgeCiBj9UtS7-u3a9Ir3zHyzQ";



        NettyWampClientConnectorProvider connectorProvider = new NettyWampClientConnectorProvider();
        try {



            Log.d("Msg", "Client built");
            // Create a builder and configure the client
            WampClientBuilder builder = new WampClientBuilder();
            builder.withConnectorProvider(connectorProvider)
                    .withUri("ws://wsdriver.chanmao.ca:7474")
                    .withRealm("realm2")
                    .withAuthMethod(new CmAuth(token))
                    .withAuthId("111");
                    //CmAuth
//                    .withInfiniteReconnects()
//                    .withReconnectInterval(1, TimeUnit.SECONDS);
            // Create a client through the builder. This will not immediatly start
            // a connection attempt
            client = builder.build();
        }  catch (Exception e) {
            e.printStackTrace();
        }



        client.statusChanged().subscribe(new Action1<WampClient.State>() {
            @Override
            public void call(WampClient.State t1) {
                Log.d("Msg","Session1 status changed to " + t1);

                if (t1 instanceof WampClient.ConnectedState) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) { }
                    Log.d("Msg1","Session1 status changed to connected");

                    //rx.Observable<Array> result1 = client.call("task_refresh", Array.class, new String[]{"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIxIiwiZXhwaXJlZCI6MTQ5MjExMzYwMCwibGFzdGxvZ2luIjoxNDg2MDYwMjUwfQ.KTlYJvFNJAXuFOd9CnfgeCiBj9UtS7-u3a9Ir3zHyzQ"});

                    Object[] obj = new Object[]{2,"3"};
                    Observable<Object> result1 = client.call("geo_trace", Object.class,obj);

                    result1.subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object obj1) {
                            Log.d("Msg","Completed add with result " +obj1);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable t1) {
                            Log.d("Msg","Completed add with error " + t1);
                        }
                    });

                    Object[] obj2= new Object[]{token};
                    Observable<Object>result2=client.call("task_refresh",Object.class,obj2);
                    result2.subscribe(new Action1<Object>() {

                        @Override
                        public void call(Object o) {
                            Log.d("Msg", "Completed add with result2 " + o);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable t1) {
                            Log.d("Msg","Completed add with error "+t1);
                        }
                    });

                    eventSubscription=client.makeSubscription("1",Object.class)
                            .subscribe(new Action1<Object>() {
                                @Override
                                public void call(Object t1) {
                                    Log.d("Msg", "Received event test.event with value " + t1);
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable t1) {
                                    Log.d("Msg","Completed event test.event with error " + t1);
                                }
                            }, new Action0() {
                                @Override
                                public void call() {
                                    Log.d("Msg","Completed event test.event");
                                }

                            });
                }
                else if (t1 instanceof  WampClient.ConnectingState){

                }
                else if (t1 instanceof  WampClient.DisconnectedState){

                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable t) {
                Log.d("Msg","Session ended with error " + t);
            }
        }, new Action0() {
            @Override
            public void call() {
                Log.d("Msg","Session ended normally");
            }
        });

        client.open();
        try{
            Thread thread=Thread.currentThread();
            thread.sleep(3000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void connect()
    {
        Log.d("Msg","Connect module Start");
        start();
        try{
            Thread thread=Thread.currentThread();
            thread.sleep(20000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.close().toBlocking().last();
    }

    @ReactMethod
    public void Test()
    {
        Log.d("Msg","Test Start");
        connect();


//        Toast.makeText(getReactApplicationContext(),"Lost Connection",Toast.LENGTH_LONG).show();
    }
    @ReactMethod
    public void disconnect(){

    }
}
