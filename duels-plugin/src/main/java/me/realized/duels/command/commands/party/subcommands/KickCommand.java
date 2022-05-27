package me.realized.duels.command.commands.party.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.realized.duels.DuelsPlugin;
import me.realized.duels.Permissions;
import me.realized.duels.command.BaseCommand;
import me.realized.duels.party.Party;

public class KickCommand extends BaseCommand {
    
    public KickCommand(final DuelsPlugin plugin) {
        super(plugin, "kick", "kick [player]", "Kicks a player from your party.", Permissions.PARTY, 2, true, "remove");
    }

    @Override
    protected void execute(final CommandSender sender, final String label, final String[] args) {
        final Player player = (Player) sender;
        final Party party = partyManager.get(player);

        if (party == null) {
            lang.sendMessage(sender, "ERROR.party.not-in-party.sender");
            return;
        }

        if (!party.isOwner(player)) {
            lang.sendMessage(sender, "ERROR.party.is-not-owner");
            return;
        }
        

        final Player target = Bukkit.getPlayerExact(args[1]);

        if (target == null || !player.canSee(target)) {
            lang.sendMessage(sender, "ERROR.player.not-found", "name", args[1]);
            return;
        }

        if (player.equals(target)) {
            lang.sendMessage(sender, "ERROR.party.kick-self");
            return;
        }

        if (!party.isMember(target)) {
            lang.sendMessage(sender, "ERROR.party.not-a-member", "name", target.getName());
            return;
        }
        
        partyManager.remove(target, party);
        lang.sendMessage(player, "COMMAND.party.kick.receiver", "owner", player.getName());
        lang.sendMessage(party.getOnlineMembers(), "COMMAND.party.kick.members", "owner", player.getName(), "name", target.getName());
    }
}
