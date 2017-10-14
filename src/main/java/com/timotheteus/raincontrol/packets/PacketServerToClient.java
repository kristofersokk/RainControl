package com.timotheteus.raincontrol.packets;

import com.timotheteus.raincontrol.block.Syncable;
import com.timotheteus.raincontrol.handlers.PacketHandler;
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

public class PacketServerToClient implements IMessage {

    private PacketTypes.SERVER[] types;
    private BlockPos blockPos;
    private int dimensionID;
    private int energy;
    private int burnTime;

    /**
     * Required objects are in the PacketTypes descriptions.
     * Put the objects in the order of the packet types
     *
     * @param te      tile entity
     * @param types
     * @param objects
     */
    public PacketServerToClient(TileEntity te, PacketTypes.SERVER[] types, Object... objects) {
        this.types = types;
        blockPos = te.getPos();
        dimensionID = te.getWorld().provider.getDimension();
        int index = 0;
        for (PacketTypes.SERVER type : types) {
            switch (type) {
                case ENERGY:
                    energy = (int) objects[index];
                    index += 1;
                    continue;
                case BURN_TIME:
                    burnTime = (int) objects[index];
                    index += 1;
                    continue;
            }
        }
    }

    public PacketServerToClient() {

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
        this.types = PacketTypes.SERVER.getTypes(stringToIntArray(
                buf.readCharSequence(len, Charset.defaultCharset())
        ));

        for (PacketTypes.SERVER type : types) {
            switch (type) {
                case ENERGY:
                    energy = buf.readInt();
                    continue;
                case BURN_TIME:
                    burnTime = buf.readInt();
                    continue;
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(blockPos.toLong());
        buf.writeInt(dimensionID);
        CharSequence sequence = intArrayToString(PacketTypes.SERVER.getIds(types));
        int len = sequence.length();
        buf.writeInt(len);
        buf.writeCharSequence(sequence, Charset.defaultCharset());
        for (PacketTypes.SERVER type : types) {
            switch (type) {
                case ENERGY:
                    buf.writeInt(energy);
                    continue;
                case BURN_TIME:
                    buf.writeInt(burnTime);
                    continue;
            }

        }

    }

    public void sendToDimension() {
        PacketHandler.INSTANCE.sendToDimension(this, dimensionID);
    }

    public static class Handler implements IMessageHandler<PacketServerToClient, IMessage> {

        @Override
        public IMessage onMessage(PacketServerToClient message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketServerToClient message, MessageContext ctx) {
            BlockPos pos = message.blockPos;
            EntityPlayer player = Minecraft.getMinecraft().player;
            World world = player.getEntityWorld();
            TileEntity te = world.getTileEntity(pos);
            if (te != null) {
                for (PacketTypes.SERVER type : message.types) {
                    switch (type) {
                        case ENERGY:
                            Syncable.Energy handlerEnergy = ((Syncable.Energy) te);
                            Minecraft.getMinecraft().addScheduledTask(() -> handlerEnergy.setEnergy(message.energy, false));
                            continue;
                        case BURN_TIME:
                            Syncable.BurnTime handlerBurnTime = ((Syncable.BurnTime) te);
                            Minecraft.getMinecraft().addScheduledTask(() -> handlerBurnTime.setBurnTime(message.burnTime, false));
                            continue;
                    }
                }

            }
        }
    }
}
