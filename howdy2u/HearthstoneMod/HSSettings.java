package howdy2u.HearthstoneMod;

import java.io.File;
import net.minecraftforge.common.Configuration;

public class HSSettings {
	
	public static int idHearthstone = 23020;
	public static int uses = 0;
	
	private static Configuration config;
	
	public static void loadConfig(File file)
    {
        String genCategory = Configuration.CATEGORY_GENERAL;
        
        config = new Configuration(file);
        
        uses = config.get(Configuration.CATEGORY_GENERAL, "uses", uses, "Number of Hearthstone uses. Set to 0 for infinite.").getInt(0);
        idHearthstone = config.get(Configuration.CATEGORY_ITEM, "hearthstone", idHearthstone).getInt(idHearthstone);
        
        config.save();
    }
}
