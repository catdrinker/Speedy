/*
 * Copyright (C) 2020 catdrinker.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
