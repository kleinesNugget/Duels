package net.withery.duels.duel;

import net.withery.duels.arena.Arena;
import net.withery.duels.kit.Kit;
import org.bukkit.entity.Player;

public class Duel {

    private final Player player1;
    private final Player player2;
    private final Arena arena;
    private final Kit kit;

    public Duel(Player player1, Player player2, Arena arena, Kit kit) {
        this.player1 = player1;
        this.player2 = player2;
        this.arena = arena;
        this.kit = kit;
    }

}