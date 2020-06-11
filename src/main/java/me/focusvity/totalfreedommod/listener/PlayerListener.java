package me.focusvity.totalfreedommod.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.SimpleCommandMap;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.*;
import cn.nukkit.level.Location;
import cn.nukkit.utils.TextFormat;
import me.focusvity.totalfreedommod.PlayerData;
import me.focusvity.totalfreedommod.TotalFreedomMod;
import me.focusvity.totalfreedommod.admin.AdminList;
import me.focusvity.totalfreedommod.banning.Ban;
import me.focusvity.totalfreedommod.banning.BanManager;
import me.focusvity.totalfreedommod.rank.Rank;

import java.lang.reflect.Field;

public class PlayerListener implements Listener
{

    private final TotalFreedomMod plugin;
    private final Server server;
    private SimpleCommandMap cmap = getCommandMap();

    public PlayerListener(TotalFreedomMod plugin)
    {
        this.plugin = plugin;
        this.server = plugin.getServer();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreLogin(PlayerAsyncPreLoginEvent event)
    {
        Ban ban = BanManager.getNameBan(event.getName());

        if (ban == null)
        {
            ban = BanManager.getIpBan(event.getAddress());
        }

        if (ban != null && !ban.isExpired())
        {
            if (AdminList.isAdmin(event.getPlayer()))
            {
                event.allow();
                return;
            }

            event.disAllow(ban.getKickMessage());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        boolean isAdmin = AdminList.isAdmin(player);

        if (isAdmin)
        {
            if (!AdminList.getAdmin(player).getIps().contains(player.getAddress()))
            {
                AdminList.getImposters().add(player.getName());
                server.broadcastMessage(TextFormat.RED + player.getName() + " has been flagged as an imposter!");
                player.getInventory().clearAll();
                player.setGamemode(0);
                player.sendMessage(TextFormat.RED + "You have been marked as an imposter, please verify yourself.");
                return;
            }

            server.broadcastMessage(TextFormat.AQUA + player.getName() + " is " + Rank.getDisplay(player).getLoginMessage());
        }

        PlayerData.getData(player).setTag(Rank.getDisplay(player).getTag());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event)
    {
        if (AdminList.isImposter(event.getPlayer()))
        {
            AdminList.getImposters().remove(event.getPlayer().getName());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChat(PlayerChatEvent event)
    {
        Player player = event.getPlayer();

        if (PlayerData.getData(player).isMuted())
        {
            player.sendMessage(TextFormat.RED + "You may not be able to chat while muted!");
            event.setCancelled(true);
            return;
        }

        String tag = PlayerData.getData(player).getTag() != null ? PlayerData.getData(player).getTag() + " " : "";
        String name = player.getDisplayName();
        event.setFormat(tag + TextFormat.WHITE + "<" + name + "> " + event.getMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        String command = event.getMessage();
        boolean isAdmin = AdminList.isAdmin(player);

        plugin.getLogger().info(TextFormat.LIGHT_PURPLE + "[COMMAND] " + TextFormat.WHITE + player.getName() + " has executed " + command.replaceFirst("/", ""));

        for (Player players : server.getOnlinePlayers().values())
        {
            if (AdminList.isAdmin(players) && Rank.getRank(players).getLevel() > Rank.getRank(player).getLevel() && players != player)
            {
                players.sendMessage(TextFormat.GRAY + player.getName() + ": " + command);
            }
        }

        for (String string : plugin.config.getStringList("commands.default"))
        {
            if (command.equalsIgnoreCase(string) || command.split(" ")[0].equalsIgnoreCase(string))
            {
                player.sendMessage(TextFormat.RED + "That command is blocked!");
                event.setCancelled(true);
            }

            if (cmap.getCommand(string) == null)
            {
                continue;
            }

            if (cmap.getCommand(string).getAliases() == null)
            {
                continue;
            }

            for (String alias : cmap.getCommand(string).getAliases())
            {
                if (command.equalsIgnoreCase(alias) || command.split(" ")[0].equalsIgnoreCase(alias))
                {
                    player.sendMessage(TextFormat.RED + "That command is blocked!");
                    event.setCancelled(true);
                }
            }
        }

        for (String string : plugin.config.getStringList("commands.admins"))
        {
            if ((command.equalsIgnoreCase(string) || command.split(" ")[0].equalsIgnoreCase(string)) && !isAdmin)
            {
                player.sendMessage(TextFormat.RED + "That command is blocked!");
                event.setCancelled(true);
            }

            if (cmap.getCommand(string) == null)
            {
                continue;
            }

            if (cmap.getCommand(string).getAliases() == null)
            {
                continue;
            }

            for (String alias : cmap.getCommand(string).getAliases())
            {
                if ((command.equalsIgnoreCase(alias) || command.split(" ")[0].equalsIgnoreCase(alias)) && !isAdmin)
                {
                    player.sendMessage(TextFormat.RED + "That command is blocked!");
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        if (PlayerData.getData(player).isFrozen())
        {
            player.teleport(location);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTeleport(PlayerTeleportEvent event)
    {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        if (PlayerData.getData(player).isFrozen())
        {
            player.teleport(location);
            event.setCancelled(true);
        }
    }

    private SimpleCommandMap getCommandMap()
    {
        if (cmap == null)
        {
            try
            {
                final Field f = Server.getInstance().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (SimpleCommandMap) f.get(Server.getInstance());
                return getCommandMap();
            }
            catch (NoSuchFieldException | IllegalAccessException ex)
            {
                plugin.getLogger().critical(ex.getMessage());
            }
        }
        else
        {
            return cmap;
        }
        return getCommandMap();
    }
}
