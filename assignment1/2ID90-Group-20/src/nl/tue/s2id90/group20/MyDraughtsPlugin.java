/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import nl.tue.s2id90.draughts.DraughtsPlayerProvider;
import nl.tue.s2id90.draughts.DraughtsPlugin;

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
                /*String name, int pieceWeight, int kingWeight, int sideWeight, 
                 int kingLaneWeight, int tandemWeight, int centerWeight, 
                 int endStateWeight*/
                new Player20Base(25, 125, 25, 75, 5, 10, 1000),
                new Player20Base(25, 125, -1, -1, -1, -1, -1),
                new Player20Base(25, 50, -1, -1, -1, -1, -1),
                new Player20Omni(),
                /*int window, int bounds, String name, int pieceWeight, int kingWeight, int sideWeight, 
                 int kingLaneWeight, int tandemWeight, int centerWeight, 
                 int endStateWeight*/
                new Player20TranspositionBase(11, 20000, 25, 125, 25, 75, 5, 10, 1000),
                new Player20TranspositionBase(11, 20000, 25, 125, -1, -1, -1, -1, -1),
                new Player20TranspositionBase(11, 20000, 25, 125, -1, -1, -1, -1, 2000),
                new Player20TranspositionBase(11, 20000, 25, 50, -1, -1, -1, -1, -1),
                
                new Player20TranspositionOmni()
        );
    }
}
