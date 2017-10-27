package darkhax.tesla.api.implementation;

import darkhax.tesla.api.ITeslaConsumer;

/**
 * Logic for a Tesla Consumer that will consume infinite amounts of power.
 */
public class InfiniteTeslaConsumer implements ITeslaConsumer {

    @Override
    public long givePower (long power, boolean simulated) {

        return power;
    }
}