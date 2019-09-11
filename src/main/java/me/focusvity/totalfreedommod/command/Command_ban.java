package me.focusvity.totalfreedommod.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.player.PlayerKickEvent;
import cn.nukkit.utils.TextFormat;
import me.focusvity.totalfreedommod.banning.BanManager;
import me.focusvity.totalfreedommod.rank.Rank;
import me.focusvity.totalfreedommod.util.FUtil;
import org.apache.commons.lang3.StringUtils;

@CommandParameters(description = "Bans a player", usage = "/<command> <player> [reason]", aliases = "gtfo", rank = Rank.SUPER_ADMIN, source = SourceType.BOTH)
public class Command_ban
{

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

        if (BanManager.isBanned(player))
        {
            sender.sendMessage(TextFormat.RED + "That player is already banned!");
            return true;
        }

        if (args.length > 1)
        {
            reason = StringUtils.join(args, " ", 1, args.length);
        }

        FUtil.adminAction(sender.getName(), "Banning " + player.getName()
                + (reason != null ? "\nReason: " + TextFormat.YELLOW + reason : ""), true);

        long expiry = FUtil.getUnixTime(FUtil.parseDateOffset("1d"));

        BanManager.addBan(sender, player, reason, expiry);
        player.kick(TextFormat.RED + "Banned!");
        return true;
    }
}
