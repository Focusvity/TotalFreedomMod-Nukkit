package me.focusvity.totalfreedommod.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import me.focusvity.totalfreedommod.PlayerData;
import me.focusvity.totalfreedommod.rank.Rank;
import me.focusvity.totalfreedommod.util.FUtil;

@CommandParameters(name = "mute", description = "Freeze a player", usage = "/<command> <player>", aliases = "stfu", rank = Rank.SUPER_ADMIN, source = SourceType.BOTH)
public class Command_mute extends FreedomCommand
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length != 1)
        {
            return false;
        }

        Player player = Server.getInstance().getPlayer(args[0]);

        if (player == null)
        {
            sender.sendMessage(TextFormat.RED + "That player does not exist!");
            return true;
        }

        boolean mute = PlayerData.getData(player).isMuted();
        FUtil.adminAction(sender.getName(), (mute ? "Unmuting " : "Muting ") + player.getName(), false);
        PlayerData.getData(player).setMuted(!mute);
        return true;
    }
}
