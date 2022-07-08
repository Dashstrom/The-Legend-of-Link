package fr.dashstrom.model;

import org.junit.jupiter.api.Test;

import static fr.dashstrom.model.Element.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ElementTest {

    @Test
    void testAgainstMultiplicator() {
        assertEquals(NORMAL.againstMultiplicator(NORMAL), 1, "NORMAL vs NORMAL");
        assertEquals(NORMAL.againstMultiplicator(HOT), 1, "NORMAL vs HOT");
        assertEquals(NORMAL.againstMultiplicator(COLD), 1, "NORMAL vs COLD");
        assertEquals(NORMAL.againstMultiplicator(ICE), 0, "NORMAL vs ICE");
        assertEquals(NORMAL.againstMultiplicator(FIRE), 0, "NORMAL vs FIRE");

        assertEquals(HOT.againstMultiplicator(NORMAL), 1, "HOT vs NORMAL");
        assertEquals(HOT.againstMultiplicator(HOT), 1, "HOT vs HOT");
        assertEquals(HOT.againstMultiplicator(COLD), 2, "HOT vs COLD");
        assertEquals(HOT.againstMultiplicator(ICE), 3, "HOT vs ICE");
        assertEquals(HOT.againstMultiplicator(FIRE), 0, "HOT vs FIRE");

        assertEquals(COLD.againstMultiplicator(NORMAL), 1, "COLD vs NORMAL");
        assertEquals(COLD.againstMultiplicator(HOT), 2, "COLD vs HOT");
        assertEquals(COLD.againstMultiplicator(COLD), 1, "COLD vs COLD");
        assertEquals(COLD.againstMultiplicator(ICE), 0, "COLD vs ICE");
        assertEquals(COLD.againstMultiplicator(FIRE), 3, "COLD vs FIRE");

        assertEquals(ICE.againstMultiplicator(NORMAL), 1, "ICE vs NORMAL");
        assertEquals(ICE.againstMultiplicator(HOT), 0, "ICE vs HOT");
        assertEquals(ICE.againstMultiplicator(COLD), 1, "ICE vs COLD");
        assertEquals(ICE.againstMultiplicator(ICE), 1, "ICE vs ICE");
        assertEquals(ICE.againstMultiplicator(FIRE), 2, "ICE vs FIRE");

        assertEquals(FIRE.againstMultiplicator(NORMAL), 1, "FIRE vs NORMAL");
        assertEquals(FIRE.againstMultiplicator(HOT), 1, "FIRE vs HOT");
        assertEquals(FIRE.againstMultiplicator(COLD), 0, "FIRE vs COLD");
        assertEquals(FIRE.againstMultiplicator(ICE), 2, "FIRE vs ICE");
        assertEquals(FIRE.againstMultiplicator(FIRE), 1, "FIRE vs FIRE");
    }

}