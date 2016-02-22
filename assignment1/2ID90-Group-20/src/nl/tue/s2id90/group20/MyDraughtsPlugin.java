/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20;

import nl.tue.s2id90.group20.player.UninformedPlayer;
import nl.tue.s2id90.group20.player.StupidPlayer;
import nl.tue.s2id90.group20.player.OptimisticPlayer;
import nl.tue.s2id90.group20.player.Player20Complete;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import nl.tue.s2id90.draughts.DraughtsPlayerProvider;
import nl.tue.s2id90.draughts.DraughtsPlugin;
import nl.tue.s2id90.group20.player.Player20DetectDuplicate;
import nl.tue.s2id90.group20.player.players.Player20_BP;
import nl.tue.s2id90.group20.player.players.Player20_CCP;
import nl.tue.s2id90.group20.player.players.Player20_CP;
import nl.tue.s2id90.group20.player.players.Player20_CP_BP;
import nl.tue.s2id90.group20.player.players.Player20_CP_CCP;
import nl.tue.s2id90.group20.player.players.Player20_CP_CCP_BP;
import nl.tue.s2id90.group20.player.players.Player20_CP_CCP_BP_CE_PE;
import nl.tue.s2id90.group20.player.players.Player20_CP_CCP_BP_PE;
import nl.tue.s2id90.group20.player.players.Player20_CP_CCP_BP_TE;
import nl.tue.s2id90.group20.player.players.Player20_CP_CCP_BP_TE_CE;
import nl.tue.s2id90.group20.player.players.Player20_CP_CCP_BP_TE_CE_PE;
import nl.tue.s2id90.group20.player.players.Player20_CP_CCP_BP_TE_PE;
import nl.tue.s2id90.group20.player.players.Player20_TE;

/**
 *
 * @author huub
 */
@PluginImplementation
public class MyDraughtsPlugin extends DraughtsPlayerProvider implements DraughtsPlugin {

    public MyDraughtsPlugin() {
        // make two players available to the AICompetition tool
        // During the final competition you should make only your 
        // best player available. For testing it might be handy
        // to make more than one player available.
        super(
                new UninformedPlayer(), 
                new OptimisticPlayer(), 
                new StupidPlayer(),
                new Player20_CP(), 
                new Player20_CCP(), 
                new Player20_BP(), 
                new Player20_TE(),
                new Player20_CP_BP(), 
                new Player20_CP_CCP(), 
                new Player20_CP_CCP_BP(), 
                new Player20_CP_CCP_BP_PE(), 
                new Player20_CP_CCP_BP_CE_PE(), 
                new Player20_CP_CCP_BP_TE(),
                new Player20_CP_CCP_BP_TE_CE(),
                new Player20_CP_CCP_BP_TE_PE(),
                new Player20_CP_CCP_BP_TE_CE_PE(),
                new Player20DetectDuplicate());
    }
}
