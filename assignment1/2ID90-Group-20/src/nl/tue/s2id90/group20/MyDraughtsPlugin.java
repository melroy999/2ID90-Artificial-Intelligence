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
import nl.tue.s2id90.group20.player.Player20Blabbermouth;
import nl.tue.s2id90.group20.player.Player20CountAllPieces;
import nl.tue.s2id90.group20.player.Player20CountBorderPieces;
import nl.tue.s2id90.group20.player.Player20DetectDuplicate;



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
        super(new UninformedPlayer(), new OptimisticPlayer(), new StupidPlayer(), new Player20Complete(), new Player20CountAllPieces(), new Player20CountBorderPieces(), new Player20Blabbermouth(), new Player20DetectDuplicate());
    }
}
