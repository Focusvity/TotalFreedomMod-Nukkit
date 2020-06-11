package me.focusvity.totalfreedommod;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import me.focusvity.totalfreedommod.admin.AdminList;
import me.focusvity.totalfreedommod.banning.BanManager;
import me.focusvity.totalfreedommod.command.CommandLoader;
import me.focusvity.totalfreedommod.listener.PlayerListener;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class TotalFreedomMod extends PluginBase
{

    public static TotalFreedomMod plugin;
    public Config config;
    public Config admins;
    public Config bans;
    public AdminList al;
    public BanManager bm;
    public CommandLoader cl;
    public PlayerListener pl;

    @Override
    public void onLoad()
    {
        plugin = this;
        initConfig();
    }

    @Override
    public void onEnable()
    {
        plugin = this;

        al = new AdminList(plugin);
        bm = new BanManager(plugin);
        cl = new CommandLoader();
        pl = new PlayerListener(plugin);

        this.getLogger().info(TextFormat.WHITE + "Created by " + StringUtils.join(this.getDescription().getAuthors(), ", "));
        this.getLogger().info(TextFormat.WHITE + "Version: " + this.getDescription().getVersion());
        this.getLogger().info(TextFormat.WHITE + "The plugin has been enabled.");
    }

    @Override
    public void onDisable()
    {
        plugin = null;

        al.save();
        BanManager.save();

        config.save();
        admins.save();
        bans.save();
        this.getLogger().info(TextFormat.WHITE + "The plugin has been disabled.");
    }

    private void initConfig()
    {
        if (getResource("config.yml") != null)
        {
            saveResource("config.yml");
        }

        if (getResource("admins.yml") != null)
        {
            saveResource("admins.yml");
        }

        if (getResource("bans.yml") != null)
        {
            saveResource("bans.yml");
        }

        config = new Config(new File(getDataFolder() + "/config.yml"), Config.YAML);
        admins = new Config(new File(getDataFolder() + "/admins.yml"), Config.YAML);
        bans = new Config(new File(getDataFolder() + "/bans.yml"), Config.YAML);
    }
}
