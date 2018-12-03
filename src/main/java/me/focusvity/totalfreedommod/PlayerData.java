package me.focusvity.totalfreedommod;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.scheduler.NukkitRunnable;
import lombok.Getter;
import lombok.Setter;
import me.focusvity.totalfreedommod.util.FUtil;

import java.util.HashMap;
import java.util.Map;

public class PlayerData
{

    private static final Map<Player, PlayerData> DATA_MAP = new HashMap<>();
    private final Player player;
    //
    @Getter
    private boolean muted;
    private NukkitRunnable muteTask;
    @Getter
    private boolean frozen;
    private NukkitRunnable freezeTask;
    @Getter
    @Setter
    private String tag;

    private PlayerData(Player player)
    {
        this.player = player;
    }

    public static boolean hasPlayerData(Player player)
    {
        return DATA_MAP.containsKey(player);
    }

    public static PlayerData getData(Player player)
    {
        PlayerData data = DATA_MAP.get(player);

        if (data != null)
        {
            return data;
        }

        data = new PlayerData(player);
        DATA_MAP.put(player, data);
        return data;
    }

    public static PlayerData getData(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            return null; // Console doesn't get a player data
        }

        return getData((Player) sender);
    }

    public void setMuted(boolean mute)
    {
        if (muteTask != null)
        {
            muteTask.cancel();
            muteTask = null;
        }

        muted = false;

        if (!mute)
        {
            return;
        }

        muted = true;
        Runnable task = new NukkitRunnable()
        {
            @Override
            public void run()
            {
                FUtil.adminAction("TotalFreedom", "Unmuting " + player.getName(), false);
                setMuted(false);
            }
        }.runTaskLater(TotalFreedomMod.plugin, 6000); // Probs using ticks (20 ticks per second) -> 6000 ticks = 5 minutes
        muteTask = (NukkitRunnable) task;
    }

    public void setFrozen(boolean freeze)
    {
        if (freezeTask != null)
        {
            freezeTask.cancel();
            freezeTask = null;
        }

        frozen = false;

        if (!freeze)
        {
            return;
        }

        frozen = true;
        Runnable task = new NukkitRunnable()
        {
            @Override
            public void run()
            {
                FUtil.adminAction("TotalFreedom", "Unfreezing " + player.getName(), false);
                setFrozen(false);
            }
        }.runTaskLater(TotalFreedomMod.plugin, 6000);
        freezeTask = (NukkitRunnable) task;
    }
}
