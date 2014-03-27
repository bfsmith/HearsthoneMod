package howdy2u.HearthstoneMod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class ItemHearthstone extends Item
{
    private int coolDown = 0;
    
    public ItemHearthstone(int id)
    {
    	super(id);
        maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);
        
        if (HSSettings.uses > 0)
        {
            setMaxDamage(HSSettings.uses - 1);
        }
    }
    
    @Override
    public Item setUnlocalizedName(String par1Str)
    {
        super.setUnlocalizedName(par1Str);
        this.setTextureName(par1Str.replaceAll("\\.", ":"));
        return this;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
    {
        if (!world.isRemote && coolDown == 0)
        {
            ChunkCoordinates chunkCoords = entityPlayer.getBedLocation(world.provider.dimensionId);
            
            if (chunkCoords == null)
                chunkCoords = world.getSpawnPoint();
            
            chunkCoords = verifyRespawnCoordinates(world, chunkCoords, false);
            
            if (chunkCoords == null)
                chunkCoords = world.getSpawnPoint();
            
            entityPlayer.rotationPitch = 0.0F;
            entityPlayer.rotationYaw = 0.0F;
            entityPlayer.setPositionAndUpdate(chunkCoords.posX + 0.5D, chunkCoords.posY + 0.1D, chunkCoords.posZ);
            
            while (!world.getCollidingBoundingBoxes(entityPlayer, entityPlayer.boundingBox).isEmpty())
            {
                entityPlayer.setPositionAndUpdate(entityPlayer.posX, entityPlayer.posY + 1.0D, entityPlayer.posZ);
            }
            
            world.playSoundAtEntity(entityPlayer, "mob.endermen.portal", 1.0F, 1.0F);
            spawnExplosionParticleAtEntity(entityPlayer);
            
            if (HSSettings.uses > 0)
                itemStack.damageItem(1, entityPlayer);
            
            coolDown = 40;
        }
        
        return itemStack;
    }
    
    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (coolDown > 0)
            coolDown--;
    }
    

    /**
     * Ensure that a block enabling respawning exists at the specified coordinates and find an empty space nearby to spawn.
     */
    private static ChunkCoordinates verifyRespawnCoordinates(World world, ChunkCoordinates chunkCoords, boolean par2)
    {
        if (!world.isRemote)
        {
            IChunkProvider ichunkprovider = world.getChunkProvider();
            ichunkprovider.loadChunk(chunkCoords.posX - 3 >> 4, chunkCoords.posZ - 3 >> 4);
            ichunkprovider.loadChunk(chunkCoords.posX + 3 >> 4, chunkCoords.posZ - 3 >> 4);
            ichunkprovider.loadChunk(chunkCoords.posX - 3 >> 4, chunkCoords.posZ + 3 >> 4);
            ichunkprovider.loadChunk(chunkCoords.posX + 3 >> 4, chunkCoords.posZ + 3 >> 4);
        }
        
        ChunkCoordinates c = chunkCoords;
        int blockId = world.getBlockId(c.posX, c.posY, c.posZ);
        Block block = Block.blocksList[blockId];
        
        if (block.equals(Block.bed))
        {
            return block.getBedSpawnPosition(world, c.posX, c.posY, c.posZ, null);
        }
        else
        {
            Material material = world.getBlockMaterial(chunkCoords.posX, chunkCoords.posY, chunkCoords.posZ);
            Material material1 = world.getBlockMaterial(chunkCoords.posX, chunkCoords.posY + 1, chunkCoords.posZ);
            boolean flag1 = !material.isSolid() && !material.isLiquid();
            boolean flag2 = !material1.isSolid() && !material1.isLiquid();
            return par2 && flag1 && flag2 ? chunkCoords : null;
        }
    }

    public void spawnExplosionParticleAtEntity(Entity entity)
    {
        double d3 = 10.0D;
        for (int i = 0; i < 20; ++i)
        {
            double d0 = entity.worldObj.rand.nextGaussian() * 0.02D;
            double d1 = entity.worldObj.rand.nextGaussian() * 0.02D;
            double d2 = entity.worldObj.rand.nextGaussian() * 0.02D;
            entity.worldObj.spawnParticle("explode", entity.posX + entity.worldObj.rand.nextFloat() * entity.width * 2.0F - entity.width - d0 * d3, entity.posY + entity.worldObj.rand.nextFloat() * entity.height - d1 * d3, entity.posZ + entity.worldObj.rand.nextFloat() * entity.width * 2.0F - entity.width - d2 * d3, d0, d1, d2);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public  void registerIcons(IconRegister iconRegister)
    {
    	itemIcon = iconRegister.registerIcon(HearthstoneMod.modId + ":" + getUnlocalizedName().substring(5));
    }
}