/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.group20;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import nl.tue.s2id90.draughts.DraughtsPlayerProvider;
import nl.tue.s2id90.draughts.DraughtsPlugin;
import nl.tue.s2id90.group20.evaluation.OmniEvaluation;
import nl.tue.s2id90.group20.extensiveTesters.*;

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
                /*new UninformedPlayer(),
                new OptimisticPlayer(),
                new StupidPlayer(),*/
                /*String name, int pieceWeight, int kingWeight, int sideWeight, 
                 int kingLaneWeight, int tandemWeight, int centerWeight, 
                 int endStateWeight*/
                /*new Player20Base(25, 125, 25, 75, 5, 10, 1000),
                new Player20Base(25, 125, -1, -1, -1, -1, -1),
                new Player20Base(25, 50, -1, -1, -1, -1, -1),
                new Player20Omni(),*/
                /*int window, int bounds, String name, int pieceWeight, int kingWeight, int sideWeight, 
                 int kingLaneWeight, int tandemWeight, int centerWeight, 
                 int endStateWeight*/
                /*new Player20TranspositionBase(11, 20000, 25, 125, 25, 75, 5, 10, 1000),
                new Player20TranspositionBase(11, 20000, 25, 125, -1, -1, -1, -1, -1),
                new Player20TranspositionBase(11, 20000, 25, 125, -1, -1, -1, -1, 2000),
                new Player20TranspositionBase(11, 20000, 25, 50, -1, -1, -1, -1, -1),*/
                /*new Player20OmniMemTesterP1(),
                new Player20OmniMemTesterP2(),
                new Player20TranspositionOmni()*/
                new OmniTesterBase(20, 200000, 10, 0, 50, 0, 1, 5, 50, 1, 0, 5, 0, 0, 50, 1, "TestPlayer#0"),
                new OmniTesterBase(20, 200000, 5, 50, 50, 10, 5, 10, 50, 0, 50, 0, 10, 0, 5, 10, "TestPlayer#1"),
                new OmniTesterBase(20, 200000, 50, 0, 5, 5, 10, 0, 1, 5, 1, 10, 0, 0, 10, 5, "TestPlayer#2"),
                new OmniTesterBase(20, 200000, 5, 1, 50, 0, 1, 0, 50, 10, 50, 0, 10, 0, 50, 1, "TestPlayer#3"),
                new OmniTesterBase(20, 200000, 50, 5, 10, 10, 10, 10, 0, 0, 10, 1, 50, 50, 0, 1, "TestPlayer#4"),
                new OmniTesterBase(20, 200000, 10, 5, 50, 10, 5, 1, 1, 10, 0, 50, 0, 50, 5, 0, "TestPlayer#5"),
                new OmniTesterBase(20, 200000, 10, 0, 1, 0, 0, 1, 1, 50, 1, 5, 10, 1, 0, 5, "TestPlayer#6"),
                new OmniTesterBase(20, 200000, 50, 5, 10, 50, 0, 0, 5, 10, 1, 50, 0, 0, 10, 1, "TestPlayer#7"),
                new OmniTesterBase(20, 200000, 10, 1, 50, 50, 50, 5, 1, 5, 1, 10, 50, 1, 5, 5, "TestPlayer#8"),
                new OmniTesterBase(20, 200000, 0, 10, 50, 50, 50, 5, 50, 10, 5, 10, 50, 5, 50, 10, "TestPlayer#9"),
                new OmniTesterBase(20, 200000, 1, 1, 10, 10, 5, 10, 1, 1, 5, 50, 1, 1, 0, 1, "TestPlayer#10"),
                new OmniTesterBase(20, 200000, 1, 1, 10, 1, 0, 0, 1, 0, 5, 0, 0, 50, 1, 5, "TestPlayer#11"),
                new OmniTesterBase(20, 200000, 1, 10, 50, 10, 10, 0, 5, 5, 1, 1, 10, 10, 50, 5, "TestPlayer#12"),
                new OmniTesterBase(20, 200000, 1, 0, 50, 0, 50, 1, 0, 10, 10, 10, 5, 1, 10, 10, "TestPlayer#13"),
                new OmniTesterBase(20, 200000, 50, 1, 1, 10, 50, 5, 0, 50, 5, 50, 50, 1, 50, 10, "TestPlayer#14"),
                new OmniTesterBase(20, 200000, 50, 10, 50, 5, 10, 10, 0, 50, 10, 5, 10, 50, 5, 5, "TestPlayer#15"),
                new OmniTesterBase(20, 200000, 5, 1, 50, 50, 0, 10, 10, 50, 1, 5, 10, 1, 50, 5, "TestPlayer#16"),
                new OmniTesterBase(20, 200000, 50, 5, 50, 50, 5, 50, 10, 50, 10, 5, 0, 0, 5, 50, "TestPlayer#17"),
                new OmniTesterBase(20, 200000, 5, 10, 50, 0, 5, 10, 5, 5, 10, 1, 50, 0, 1, 50, "TestPlayer#18"),
                new OmniTesterBase(20, 200000, 1, 5, 10, 1, 10, 5, 0, 1, 5, 1, 50, 10, 1, 50, "TestPlayer#19"),
                new OmniTesterBase(20, 200000, 5, 5, 50, 50, 1, 0, 50, 0, 1, 1, 0, 10, 1, 0, "TestPlayer#20"),
                new OmniTesterBase(20, 200000, 0, 1, 50, 10, 0, 1, 10, 0, 50, 1, 10, 0, 1, 50, "TestPlayer#21"),
                new OmniTesterBase(20, 200000, 1, 50, 50, 5, 1, 1, 50, 10, 1, 10, 10, 50, 10, 0, "TestPlayer#22"),
                new OmniTesterBase(20, 200000, 50, 0, 1, 0, 1, 1, 0, 10, 50, 10, 10, 50, 1, 50, "TestPlayer#23"),
                new OmniTesterBase(20, 200000, 5, 5, 10, 1, 5, 1, 5, 50, 50, 1, 1, 5, 5, 5, "TestPlayer#24"),
                new OmniTesterBase(20, 200000, 5, 1, 5, 0, 5, 1, 50, 50, 0, 0, 1, 50, 1, 50, "TestPlayer#25"),
                new OmniTesterBase(20, 200000, 1, 0, 5, 1, 0, 1, 10, 1, 0, 1, 5, 5, 5, 10, "TestPlayer#26"),
                new OmniTesterBase(20, 200000, 10, 1, 5, 50, 5, 50, 1, 50, 0, 5, 10, 50, 1, 10, "TestPlayer#27"),
                new OmniTesterBase(20, 200000, 50, 10, 50, 5, 50, 1, 1, 5, 0, 5, 50, 50, 0, 50, "TestPlayer#28"),
                new OmniTesterBase(20, 200000, 0, 1, 10, 5, 50, 5, 0, 0, 10, 0, 5, 10, 10, 1, "TestPlayer#29"),
                new OmniTesterBase(20, 200000, 10, 1, 50, 0, 0, 1, 1, 0, 1, 0, 1, 5, 10, 0, "TestPlayer#30"),
                new OmniTesterBase(20, 200000, 50, 0, 5, 10, 10, 10, 50, 50, 1, 50, 50, 1, 0, 1, "TestPlayer#31"),
                new OmniTesterBase(20, 200000, 0, 0, 10, 10, 0, 0, 10, 50, 10, 50, 5, 1, 10, 0, "TestPlayer#32"),
                new OmniTesterBase(20, 200000, 50, 50, 50, 10, 50, 50, 10, 5, 1, 5, 0, 10, 1, 10, "TestPlayer#33"),
                new OmniTesterBase(20, 200000, 0, 0, 1, 0, 0, 5, 0, 1, 50, 5, 0, 10, 10, 1, "TestPlayer#34"),
                new OmniTesterBase(20, 200000, 10, 0, 5, 0, 1, 0, 0, 10, 5, 50, 5, 5, 5, 5, "TestPlayer#35"),
                new OmniTesterBase(20, 200000, 5, 1, 1, 10, 1, 0, 10, 50, 1, 1, 5, 10, 50, 10, "TestPlayer#36"),
                new OmniTesterBase(20, 200000, 50, 0, 50, 1, 10, 1, 50, 0, 1, 10, 50, 1, 10, 50, "TestPlayer#37"),
                new OmniTesterBase(20, 200000, 50, 5, 10, 5, 50, 0, 10, 50, 50, 0, 50, 1, 50, 10, "TestPlayer#38"),
                new OmniTesterBase(20, 200000, 0, 0, 0, 10, 5, 5, 1, 5, 10, 50, 10, 10, 50, 0, "TestPlayer#39"),
                new OmniTesterBase(20, 200000, 1, 1, 50, 0, 1, 5, 1, 5, 10, 10, 10, 50, 50, 0, "TestPlayer#40"),
                new OmniTesterBase(20, 200000, 50, 1, 5, 50, 5, 1, 5, 5, 5, 0, 10, 10, 50, 1, "TestPlayer#41"),
                new OmniTesterBase(20, 200000, 0, 0, 50, 5, 1, 1, 10, 0, 5, 0, 0, 5, 1, 50, "TestPlayer#42"),
                new OmniTesterBase(20, 200000, 5, 5, 5, 1, 0, 0, 50, 0, 1, 0, 10, 0, 1, 5, "TestPlayer#43"),
                new OmniTesterBase(20, 200000, 10, 1, 50, 50, 10, 10, 5, 10, 10, 10, 0, 10, 1, 50, "TestPlayer#44"),
                new OmniTesterBase(20, 200000, 5, 1, 10, 50, 0, 1, 50, 0, 0, 1, 0, 1, 1, 50, "TestPlayer#45"),
                new OmniTesterBase(20, 200000, 50, 0, 0, 50, 5, 50, 50, 1, 5, 10, 0, 50, 10, 1, "TestPlayer#46"),
                new OmniTesterBase(20, 200000, 50, 5, 5, 5, 1, 50, 0, 10, 50, 0, 50, 5, 0, 0, "TestPlayer#47"),
                new OmniTesterBase(20, 200000, 1, 1, 5, 1, 0, 0, 0, 5, 1, 10, 50, 1, 50, 5, "TestPlayer#48"),
                new OmniTesterBase(20, 200000, 1, 1, 50, 5, 1, 5, 5, 5, 5, 0, 0, 10, 1, 5, "TestPlayer#49"),
                new OmniTesterBase(20, 200000, 1, 5, 10, 5, 5, 50, 50, 5, 50, 5, 0, 10, 0, 5, "TestPlayer#50"),
                new OmniTesterBase(20, 200000, 0, 0, 10, 50, 1, 0, 1, 0, 0, 5, 5, 1, 1, 50, "TestPlayer#51"),
                new OmniTesterBase(20, 200000, 10, 1, 10, 50, 50, 5, 1, 10, 0, 0, 0, 1, 0, 0, "TestPlayer#52"),
                new OmniTesterBase(20, 200000, 1, 5, 50, 50, 50, 1, 10, 0, 1, 5, 10, 5, 5, 50, "TestPlayer#53"),
                new OmniTesterBase(20, 200000, 10, 0, 50, 10, 10, 10, 0, 50, 10, 10, 10, 50, 0, 5, "TestPlayer#54"),
                new OmniTesterBase(20, 200000, 10, 0, 5, 1, 5, 50, 5, 0, 1, 10, 5, 5, 50, 10, "TestPlayer#55"),
                new OmniTesterBase(20, 200000, 0, 0, 50, 0, 1, 5, 0, 0, 1, 1, 0, 50, 10, 1, "TestPlayer#56"),
                new OmniTesterBase(20, 200000, 0, 5, 5, 10, 50, 10, 50, 50, 1, 5, 0, 5, 1, 5, "TestPlayer#57"),
                new OmniTesterBase(20, 200000, 10, 5, 50, 10, 1, 0, 0, 0, 1, 10, 5, 50, 5, 1, "TestPlayer#58"),
                new OmniTesterBase(20, 200000, 50, 0, 1, 1, 1, 0, 5, 10, 0, 10, 1, 1, 50, 10, "TestPlayer#59"),
                new OmniTesterBase(20, 200000, 5, 10, 50, 1, 10, 10, 10, 5, 5, 0, 1, 1, 0, 10, "TestPlayer#60"),
                new OmniTesterBase(20, 200000, 1, 0, 5, 1, 10, 50, 0, 10, 50, 5, 5, 5, 5, 5, "TestPlayer#61"),
                new OmniTesterBase(20, 200000, 10, 1, 50, 5, 50, 50, 1, 1, 0, 10, 1, 50, 50, 0, "TestPlayer#62"),
                new OmniTesterBase(20, 200000, 0, 0, 1, 0, 5, 10, 1, 10, 0, 10, 1, 1, 0, 1, "TestPlayer#63"),
                new OmniTesterBase(20, 200000, 1, 1, 1, 50, 50, 1, 10, 1, 50, 50, 1, 10, 0, 1, "TestPlayer#64"),
                new OmniTesterBase(20, 200000, 5, 10, 50, 50, 10, 50, 5, 50, 1, 50, 50, 5, 50, 1, "TestPlayer#65"),
                new OmniTesterBase(20, 200000, 10, 1, 5, 10, 5, 10, 1, 1, 50, 1, 1, 10, 5, 10, "TestPlayer#66"),
                new OmniTesterBase(20, 200000, 50, 0, 10, 1, 50, 10, 5, 1, 0, 10, 5, 1, 10, 5, "TestPlayer#67"),
                new OmniTesterBase(20, 200000, 1, 5, 10, 1, 0, 5, 0, 50, 5, 0, 10, 1, 10, 1, "TestPlayer#68"),
                new OmniTesterBase(20, 200000, 0, 10, 50, 5, 0, 50, 0, 10, 0, 0, 50, 5, 5, 10, "TestPlayer#69"),
                new OmniTesterBase(20, 200000, 50, 10, 50, 50, 1, 50, 50, 50, 5, 0, 50, 0, 0, 50, "TestPlayer#70"),
                new OmniTesterBase(20, 200000, 50, 0, 1, 0, 0, 5, 10, 10, 10, 50, 1, 10, 5, 1, "TestPlayer#71"),
                new OmniTesterBase(20, 200000, 0, 10, 50, 0, 0, 1, 5, 10, 50, 1, 5, 5, 5, 50, "TestPlayer#72"),
                new OmniTesterBase(20, 200000, 0, 0, 50, 50, 50, 0, 50, 10, 5, 1, 5, 1, 50, 10, "TestPlayer#73"),
                new OmniTesterBase(20, 200000, 10, 5, 10, 10, 50, 5, 50, 50, 50, 1, 10, 10, 1, 50, "TestPlayer#74"),
                new OmniTesterBase(20, 200000, 5, 0, 1, 1, 1, 50, 1, 0, 10, 0, 1, 10, 10, 1, "TestPlayer#75"),
                new OmniTesterBase(20, 200000, 10, 0, 1, 50, 5, 1, 5, 5, 50, 50, 10, 50, 50, 1, "TestPlayer#76"),
                new OmniTesterBase(20, 200000, 0, 0, 50, 1, 50, 5, 5, 5, 10, 10, 0, 10, 10, 5, "TestPlayer#77"),
                new OmniTesterBase(20, 200000, 1, 10, 50, 0, 5, 1, 1, 50, 5, 0, 1, 0, 50, 10, "TestPlayer#78"),
                new OmniTesterBase(20, 200000, 50, 5, 50, 1, 50, 5, 50, 50, 50, 5, 5, 10, 0, 5, "TestPlayer#79"),
                new OmniTesterBase(20, 200000, 0, 5, 10, 10, 0, 50, 0, 10, 0, 1, 5, 1, 5, 50, "TestPlayer#80"),
                new OmniTesterBase(20, 200000, 1, 1, 1, 10, 50, 50, 0, 5, 50, 50, 10, 5, 10, 5, "TestPlayer#81"),
                new OmniTesterBase(20, 200000, 5, 0, 50, 10, 1, 5, 1, 0, 10, 10, 1, 5, 5, 0, "TestPlayer#82"),
                new OmniTesterBase(20, 200000, 1, 1, 5, 10, 10, 0, 10, 5, 5, 0, 0, 1, 5, 5, "TestPlayer#83"),
                new OmniTesterBase(20, 200000, 10, 5, 50, 50, 0, 0, 5, 10, 50, 10, 50, 1, 50, 5, "TestPlayer#84"),
                new OmniTesterBase(20, 200000, 0, 10, 50, 10, 10, 50, 0, 0, 1, 50, 0, 5, 5, 1, "TestPlayer#85"),
                new OmniTesterBase(20, 200000, 50, 50, 50, 50, 0, 5, 1, 5, 5, 5, 0, 50, 1, 5, "TestPlayer#86"),
                new OmniTesterBase(20, 200000, 5, 10, 50, 50, 5, 50, 0, 10, 50, 10, 1, 0, 1, 50, "TestPlayer#87"),
                new OmniTesterBase(20, 200000, 5, 5, 50, 0, 0, 10, 0, 5, 0, 50, 5, 5, 0, 10, "TestPlayer#88"),
                new OmniTesterBase(20, 200000, 1, 0, 5, 1, 0, 5, 1, 10, 0, 1, 0, 0, 50, 5, "TestPlayer#89"),
                new OmniTesterBase(20, 200000, 1, 5, 50, 0, 1, 0, 10, 5, 50, 5, 0, 10, 50, 1, "TestPlayer#90"),
                new OmniTesterBase(20, 200000, 0, 0, 10, 1, 50, 0, 0, 10, 50, 1, 1, 1, 1, 10, "TestPlayer#91"),
                new OmniTesterBase(20, 200000, 50, 0, 0, 0, 5, 0, 5, 10, 10, 5, 5, 5, 5, 5, "TestPlayer#92"),
                new OmniTesterBase(20, 200000, 5, 0, 50, 50, 1, 1, 10, 5, 1, 0, 1, 10, 1, 0, "TestPlayer#93"),
                new OmniTesterBase(20, 200000, 10, 10, 10, 5, 50, 0, 1, 10, 1, 0, 1, 50, 50, 50, "TestPlayer#94"),
                new OmniTesterBase(20, 200000, 0, 0, 1, 50, 10, 10, 10, 10, 5, 0, 0, 0, 0, 50, "TestPlayer#95"),
                new OmniTesterBase(20, 200000, 1, 5, 10, 1, 1, 50, 1, 1, 1, 0, 50, 5, 50, 0, "TestPlayer#96"),
                new OmniTesterBase(20, 200000, 1, 5, 5, 5, 1, 10, 0, 1, 5, 1, 5, 1, 10, 50, "TestPlayer#97"),
                new OmniTesterBase(20, 200000, 0, 1, 1, 10, 0, 1, 50, 5, 5, 1, 1, 5, 10, 0, "TestPlayer#98"),
                new OmniTesterBase(20, 200000, 10, 0, 1, 1, 0, 5, 0, 50, 50, 50, 10, 50, 1, 50, "TestPlayer#99")
        );
    }
}
