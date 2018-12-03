package me.focusvity.totalfreedommod.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import me.focusvity.totalfreedommod.rank.Rank;
import me.focusvity.totalfreedommod.util.FUtil;
import org.apache.commons.lang3.StringUtils;

@CommandParameters(description = "Smite a bad player", usage = "/<command> <player> [reason]", rank = Rank.SUPER_ADMIN, source = SourceType.BOTH)
public class Command_smite
{

    public static void smite(CommandSender sender, Player player, String reason)
    {
        FUtil.bcastMsg(player.getName() + " has been a naughty, naughty boy.", TextFormat.RED);

        if (reason != null)
        {
            FUtil.bcastMsg("  Reason: " + TextFormat.YELLOW + reason, TextFormat.RED);
        }
        FUtil.bcastMsg("  Smited by: " + TextFormat.YELLOW + sender.getName(), TextFormat.RED);

        // Deop
        player.setOp(false);

        // Set gamemode to survival
        player.setGamemode(0);

        // Clear inventory
        player.getInventory().clearAll();

        // Kill
        player.setHealth(0);

        if (reason != null)
        {
            player.sendMessage(TextFormat.RED + "You've been smitten. Reason: " + TextFormat.YELLOW + reason);
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        Player player = Server.getInstance().getPlayer(args[0]);
        String reason = null;

        if (player == null)
        {
            sender.sendMessage(TextFormat.RED + "That player does not exist!");
            return true;
        }

        if (args.length > 2)
        {
            reason = StringUtils.join(args, " ", 1, args.length);
        }

        smite(sender, player, reason);
        return true;
    }
}

