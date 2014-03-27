package howdy2u.HearthstoneMod;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = HearthstoneMod.modId, name="Hearthstone Mod", version = "0.1")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class HearthstoneMod {
	public static final String modId = "Hearthstone";
	
    @Metadata(value = "HearthstoneMod")
    public static ModMetadata       metadata;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        metadata = event.getModMetadata();
        HSSettings.loadConfig(event.getSuggestedConfigurationFile());
    }
    
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		Item hearthstoneItem = new ItemHearthstone(HSSettings.idHearthstone).setUnlocalizedName("hearthstoneItem");
		LanguageRegistry.addName(hearthstoneItem, "Hearthstone");
        GameRegistry.registerItem(hearthstoneItem, "hearthstoneItem", modId);
        GameRegistry.addRecipe(new ItemStack(hearthstoneItem, 1), new Object[] {
                "GDG", 
                "DED", 
                "GDG", 
                Character.valueOf('G'), Item.ingotGold, 
                Character.valueOf('D'), Item.diamond,
                Character.valueOf('E'), Item.enderPearl
        });
        
	}
}
