package com.drinker.speedy;

/**
 * a task delivery ,you can delivery to another thread or do another operation
 * before or after it
 */
public interface IDelivery {

    /**
     * delivery runnable to main thread
     *
     * @param task IDeliveryTask task to run
     */
    void delivery(IDeliveryTask task);

    /**
     * just use task.exec , need't other operation
     */
    class DefaultDelivery implements IDelivery {
        @Override
        public void delivery(IDeliveryTask task) {
            task.exec();
        }
    }
}
