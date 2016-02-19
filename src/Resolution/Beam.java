package Resolution;

import Solution.Solution;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by n on 08/02/16.
 */
public class Beam {
    public int B;
    public Solution[] beam;//Solutions dans l'ordre croissant de leur coût
    public ArrayList<Solution> potential;

    public Beam(int beamValue, Solution firstSolution) {
        this.B = beamValue;
        this.beam = new Solution[B];
        for (int i = 0; i < B; i++) this.beam[i] = firstSolution.clone();
        this.potential = new ArrayList<Solution>();
    }

    public void procedure(String procedure, int iteration) {
        for (int i = 0; i < iteration; i++) {
            for (int j = 0; j < B; j++)
                if (procedure == "random")
                    for (Solution fille : Procedures.random(beam[j].clone()))
                        potential.add(fille);  //VERIFIER que le tableau est généré une seule fois
                else if (procedure == "neh")
                    for (Solution fille : Procedures.neh(beam[j].clone())) potential.add(fille);   //idem
            select();
        }
    }

    /**
     * S�lectionne les B meilleures solutions parmi (le beam et potential)
     * et les ordonne dans le beam selon le co�t (meilleures solutions en premier)
     */
    public void select() {
        Solution[] allSolutions = new Solution[B + potential.size()];
        for (int i = 0; i < B; i++)
            allSolutions[i] = beam[i];
        for (int i = 0; i < this.potential.size(); i++)
            allSolutions[B + i] = this.potential.get(i);

        try {
            for (int i = 0; i < allSolutions.length; i++) {
                if (allSolutions[i].getCost() == 0)
                    throw new Exception("cout nul");
                if (!allSolutions[i].isValid())
                    throw new Exception("La solution "+i+" du beam n'est pas valide");
            }
            Arrays.sort(allSolutions, new SolutionComparator());
            for (int i = 0; i < B; i++)
                this.beam[i] = allSolutions[i];
            this.potential.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the best solution from the beam
     */
    public Solution bestSolution() {
        beam[0].print();
        return beam[0];
    }

    public void print() {
        System.out.println("Le BEAM contient actuellement :");
        for (int s = 0; s < beam.length; s++)
            beam[s].printShort();
    }
}
