package net.withery.duels.duel;

import net.withery.duels.arena.Arena;
import net.withery.duels.kit.Kit;
import org.bukkit.entity.Player;

public class Duel {

    private final Player player1;
    private final Player player2;
    private final Kit kit;
    private Arena arena;

    public Duel(Player player1, Player player2, Kit kit) {
        this(player1, player2, kit, null);
    }

    public Duel(Player player1, Player player2, Kit kit, Arena arena) {
        this.player1 = player1;
        this.player2 = player2;
        this.kit = kit;
        this.arena = arena;
    }

    public void startPreparation() {
        // TODO: 02/07/2021 open bet GUI
    }

    public void startDuel() {
        // TODO: 02/07/2021 start duel
    }

    public void stopDuel() {
        // TODO: 02/07/2021 stop duel, maybe add winner and stuff as passing argument
    }

}