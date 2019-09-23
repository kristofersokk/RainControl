package com.timotheteus.raincontrol.block.properties;

import com.google.common.collect.Lists;
import net.minecraft.state.IProperty;

import java.util.Collection;
import java.util.Optional;

public class RainSensorProperty implements IProperty<Integer> {

    @Override
    public String getName() {
        return "RainSensor_power";
    }

    @Override
    public Collection<Integer> getAllowedValues() {
        return Lists.newArrayList(0, 15);
    }

    @Override
    public Class<Integer> getValueClass() {
        return Integer.class;
    }

    @Override
    public Optional<Integer> parseValue(String value) {
        switch (value) {
            case "ON":
                return Optional.of(15);
            case "OFF":
                return Optional.of(0);
            default:
                return Optional.empty();
        }
    }

    @Override
    public String getName(Integer value) {
        switch (value) {
            case 15:
                return "ON";
            case 0:
                return "OFF";
            default:
                return "ERROR";
        }
    }


}
