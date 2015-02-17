package me.timlampen.util;

import java.io.File;
 
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
 
public class ConfigMaker extends YamlConfiguration{
 
    private JavaPlugin plugin;
    private String fileName;
 
    public ConfigMaker(JavaPlugin plugin, String fileName){
        this.plugin = plugin;
        this.fileName = fileName + (fileName.endsWith(".yml") ? "" : ".yml");
 
        createFile();
    }
 
    private void createFile() {
        try {
            File file = new File(plugin.getDataFolder(), fileName);
            if (!file.exists()){
                if (plugin.getResource(fileName) != null){
                    plugin.saveResource(fileName, false);
                }else{
                    save(file);
                }
            }else{
                load(file);
                save(file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
 
    public void save(){
        try {
            save(new File(plugin.getDataFolder(), fileName));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
 