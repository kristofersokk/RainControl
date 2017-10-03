package com.timotheteus.raincontrol.packets;

import com.timotheteus.raincontrol.tileentities.TileEntityRainBlock;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketEnergyChange implements IMessage {

    private BlockPos blockPos;
    private int energy;

    @Override
    public void fromBytes(ByteBuf buf) {
        blockPos = BlockPos.fromLong(buf.readLong());
        energy = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(blockPos.toLong());
        buf.writeInt(energy);
    }

    public PacketEnergyChange(BlockPos blockPos, int energy) {
        this.blockPos = blockPos;
        this.energy = energy;
    }

    public PacketEnergyChange(){

    }


    public static class Handler implements IMessageHandler<PacketEnergyChange, IMessage> {

        @Override
        public IMessage onMessage(PacketEnergyChange message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> handle(message,ctx));
            return null;
        }

        private void handle(PacketEnergyChange message, MessageContext ctx) {
            BlockPos pos = message.blockPos;
            EntityPlayer player = Minecraft.getMinecraft().player;
            World world = player.getEntityWorld();
            TileEntity te = world.getTileEntity(pos);
            if (te != null && te instanceof TileEntityRainBlock){
                TileEntityRainBlock teRB = (TileEntityRainBlock)te;
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    teRB.setEnergy(message.energy);
                });
            }
        }
    }
}
