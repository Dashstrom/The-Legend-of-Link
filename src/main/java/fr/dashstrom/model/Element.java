package fr.dashstrom.model;

public enum Element {
    NORMAL, HOT, COLD, ICE, FIRE;

    /**
     * return a multiplier depending on the source element to the element against
     *
     * @param element Element to attack
     * @return multiplication
     */
    public int againstMultiplicator(Element element) {
        switch (this) {
            case HOT:
                if (element == ICE)
                    return 3;
                if (element == COLD)
                    return 2;
                else if (element == FIRE)
                    return 0;
                break;
            case COLD:
                if (element == FIRE)
                    return 3;
                if (element == HOT)
                    return 2;
                if (element == ICE)
                    return 0;
                break;
            case ICE:
                if (element == FIRE)
                    return 2;
                if (element == HOT)
                    return 0;
                break;
            case FIRE:
                if (element == ICE)
                    return 2;
                if (element == COLD)
                    return 0;
                break;
            case NORMAL:
                if (element == FIRE)
                    return 0;
                if (element == ICE)
                    return 0;
                break;
        }
        return 1;
    }
}
