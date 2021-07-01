package net.withery.duels.duel;

import net.withery.duels.arena.Arena;
import net.withery.duels.kit.Kit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class DuelFactory {

    private final static Set<Duel> duels = new HashSet<>();

    public static Duel createDuel(Player player1, Player player2, Kit kit) {
        return new Duel(player1, player2, kit);
    }

    public static Duel createDuel(Player player1, Player player2, Kit kit, Arena arena) {
        return new Duel(player1, player2, kit, arena);
    }

    public static void stopDuel(Duel duel) {
        duels.remove(duel);
    }

    public static Set<Duel> getDuels() {
        return duels;
    }

}