package Resolution;

import Solution.*;

/**
 * Created by n on 08/02/16.
 * Ne retourne que des solutions valides.
 */
public final class Procedures {


    public static Solution[] random(Solution mere) {

        return new Solution[1];
    }


    /**
     * Selectionne un job arbitrairement, le place � tout emplacement possible dans la solution
     * @param mere
     * @return
     */
    public static Solution[] neh(Solution mere) {
        return null;
    }

    /**
     * Selectionne un job dans une des machines les plus utilis�e. Le place sur une des autres machine
     * Si 1 seule machine au niveau 1, ne s'�x�cute que pour le niveau 2
     * @param mere
     * @return
     */
    public static Solution[] bmp(Solution mere) {
        Solution sol = mere.clone();

        return null;
    }
}
