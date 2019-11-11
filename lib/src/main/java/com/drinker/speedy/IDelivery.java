package com.drinker.speedy;

public interface IDelivery {

    /**
     * delivery runnable to main thread
     *
     * @param runnable runnable to delivery
     */
    void delivery(Runnable runnable);

}
