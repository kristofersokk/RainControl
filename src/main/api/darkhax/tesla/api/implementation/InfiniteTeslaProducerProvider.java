package darkhax.tesla.api.implementation;

import darkhax.tesla.api.capability.TeslaCapabilities;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * A capability provider for the infinite Tesla producer capability.
 */
public class InfiniteTeslaProducerProvider implements ICapabilityProvider {

    @Override
    public boolean hasCapability (Capability<?> capability, EnumFacing facing) {

        return capability == TeslaCapabilities.CAPABILITY_PRODUCER;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability (Capability<T> capability, EnumFacing facing) {

        return capability == TeslaCapabilities.CAPABILITY_PRODUCER ? (T) new InfiniteTeslaConsumer() : null;
    }
}
