package com.timotheteus.raincontrol.packets;

import com.timotheteus.raincontrol.handlers.PacketHandler;
import com.timotheteus.raincontrol.tileentities.Syncable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.nio.charset.Charset;

public class PacketConfig implements IMessage {

    private PacketTypes.CONFIG[] types;
    private BlockPos blockPos;
    private int dimensionID;
    private int generator_generation;

    /**
     * Required objects are in the PacketTypes descriptions.
     * Put the objects in the order of the packet types
     *
     * @param te      tile entity
     * @param types   different config packet types
     *                in the right order
     * @param objects objects in the right order for the packets
     */
    public PacketConfig(TileEntity te, PacketTypes.CONFIG[] types, Object... objects) {
        this.types = types;
        blockPos = te.getPos();
        dimensionID = te.getWorld().provider.getDimension();
        int index = 0;
        for (PacketTypes.CONFIG type : types) {
            switch (type) {
                case GENERATOR_GENERATION:
                    generator_generation = (int) objects[index];
                    index += 1;
                    continue;

            }
        }
    }

    public PacketConfig() {

    }

    private static String intArrayToString(int[] ints) {
        StringBuilder builder = new StringBuilder();
        for (int a : ints) {
            builder.append(a);
        }
        return builder.toString();
    }

    private static int[] stringToIntArray(CharSequence str) {
        int[] result = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            result[i] = ((int) str.charAt(i));
        }
        return result;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        blockPos = BlockPos.fromLong(buf.readLong());
        dimensionID = buf.readInt();
        int len = buf.readInt();
        this.types = PacketTypes.CONFIG.getTypes(stringToIntArray(
                buf.readCharSequence(len, Charset.defaultCharset())
        ));

        for (PacketTypes.CONFIG type : types) {
            switch (type) {
                case GENERATOR_GENERATION:
                    generator_generation = buf.readInt();
                    continue;

            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(blockPos.toLong());
        buf.writeInt(dimensionID);
        CharSequence sequence = intArrayToString(PacketTypes.CONFIG.getIds(types));
        int len = sequence.length();
        buf.writeInt(len);
        buf.writeCharSequence(sequence, Charset.defaultCharset());
        for (PacketTypes.CONFIG type : types) {
            switch (type) {
                case GENERATOR_GENERATION:
                    buf.writeInt(generator_generation);
                    continue;
            }

        }

    }

    public void sendToDimension() {
        PacketHandler.INSTANCE.sendToDimension(this, dimensionID);
    }

    public static class Handler implements IMessageHandler<com.timotheteus.raincontrol.packets.PacketConfig, IMessage> {

        @Override
        public IMessage onMessage(com.timotheteus.raincontrol.packets.PacketConfig message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(com.timotheteus.raincontrol.packets.PacketConfig message, MessageContext ctx) {
            BlockPos pos = message.blockPos;
            EntityPlayer player = Minecraft.getMinecraft().player;
            World world = player.getEntityWorld();
            TileEntity te = world.getTileEntity(pos);
            if (te != null) {
                Syncable syncable = ((Syncable) te);
                for (PacketTypes.CONFIG type : message.types) {
                    switch (type) {
                        case GENERATOR_GENERATION:
                            Minecraft.getMinecraft().addScheduledTask(() -> syncable.sync(PacketTypes.CONFIG.GENERATOR_GENERATION, message.generator_generation, false));
                            continue;
                    }
                }

            }
        }
    }
}
