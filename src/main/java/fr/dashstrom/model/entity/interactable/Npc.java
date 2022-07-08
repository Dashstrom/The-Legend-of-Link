package fr.dashstrom.model.entity.interactable;

import fr.dashstrom.model.movement.Patrol;
import fr.dashstrom.model.Land;
import fr.dashstrom.model.ModelConstants;
import fr.dashstrom.model.entity.dommageable.fighter.Player;

public class Npc extends Interactable {

    private static int count = 0;
    //Stores the sentences known by a NPC
    private final String[] dialog;
    //Stores the position of the last sentence said in the list above
    private int lastSentenceIndex;
    private String id;

    public Npc(Land land, int x, int y, String[] dialog, int[][] points) {
        super(land, x, y);
        setWalkbox(16, 16);
        this.dialog = dialog;
        this.lastSentenceIndex = 0;
        this.id = "NPC" + count;
        count++;
        Patrol patrol = new Patrol(this, 1);
        for (int[] point : points)
            patrol.addPoint(point);
        setMove(patrol);
    }

    public Npc(Land land, int x, int y, String[] dialog, String id) {
        this(land, x, y, dialog, new int[][]{});
        this.id = id;
    }

    public Npc(Land land, int x, int y, String[] dialog) {
        this(land, x, y, dialog, new int[][]{});
        this.id = "NPC" + count;
        count++;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void startInteracting() {
        if (distance() == 1)
            //Starts the dialog with the NPC
            startTalking();
    }

    @Override
    public void endInteracting() {
        //Ends the dialog with the NPC (takes a empty list in parameter)
        stopTalking();
    }

    /**
     * Determines which sentence needs to be prompt while talking.
     */
    private void startTalking() {
        //If the dialog is not ended
        if (lastSentenceIndex < dialog.length) {
            //Last sentence of the dialog
            if (lastSentenceIndex == dialog.length - 1) {
                getLand().setSays(dialog[lastSentenceIndex]);
                //The dialog is ended (reset lastSentenceNumber)
                lastSentenceIndex = 0;
            }

            //Other sentences
            else {
                getLand().setSays(dialog[lastSentenceIndex] + " [...]");
                lastSentenceIndex++;
            }
        }
    }

    /**
     * Set the Observable String to an empty value
     */
    private void stopTalking() {
        getLand().setSays("");
    }

    @Override
    public int distance() {
        Player p = getLand().getPlayer();

        if (Math.abs(p.getX() - (getX() + 20)) <= 50 && Math.abs(p.getY() - (getY() + 24)) <= 60)
            return 1;
        return ModelConstants.TOO_FAR;
    }

}
