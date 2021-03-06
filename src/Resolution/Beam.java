package Resolution;

import Solution.Solution;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *Contient les meilleures solutions, qui sont actualisées avec les solutions données par les procédures
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

    /**
     * Effectue à chaque itération chaque procédure sur chaque solution du beam
     * @param iteration
     */
    public void procedureSet(int iteration) {
        for (int i = 0; i < iteration; i++) {
            System.out.println("Iteration "+i+" on "+iteration+".");
            bestSolution();
            for (int j = 0; j < B; j++) {
                for (Solution fille : Procedures.random(beam[j].clone()))
                    potential.add(fille);
                select();
            }
            for (int j = 0; j < B; j++) {
                for (Solution fille : Procedures.bmp(beam[j].clone(), 1))
                    potential.add(fille);
                select();
            }
            for (int j = 0; j < B; j++) {
                for (Solution fille : Procedures.neh(beam[j].clone()))
                    potential.add(fille);
                select();
            }
            for (int j = 0; j < B; j++) {
                for (Solution fille : Procedures.bmp(beam[j].clone()))
                    potential.add(fille);
                select();
            }
            for (int j = 0; j < B; j++) {
                for (Solution fille : Procedures.neh(beam[j].clone()))
                    potential.add(fille);
                select();
            }
        }
    }

    public void procedure(String procedure, int iteration) {
        for (int i = 0; i < iteration; i++)
            for (int j = 0; j < B; j++) {
                if (procedure == "random")
                    for (Solution fille : Procedures.random(beam[j].clone()))
                        potential.add(fille);  //VERIFIER que le tableau est généré une seule fois
                else if (procedure == "neh")
                    for (Solution fille : Procedures.neh(beam[j].clone()))
                        potential.add(fille);   //idem
                else if (procedure == "bmp")
                    for (Solution fille : Procedures.bmp(beam[j].clone()))
                        potential.add(fille);   //idem
                select();
            }
    }

    /**
     * S�lectionne les B meilleures solutions parmi (le beam et potential)
     * et les ordonne dans le beam selon le co�t (meilleures solutions en premier)
     */
    public void select() {
        ArrayList<Solution> allSolutions = new ArrayList<Solution>();
        for (int i = 0; i < B; i++)
            if (beam[i].isNotIn(allSolutions))
                allSolutions.add(beam[i]);
        for (Solution s : potential)
            if (s.isNotIn(allSolutions))
                allSolutions.add(s);
        Solution[] differentSolutions = new Solution[allSolutions.size()];
        int in = 0;
        for (Solution s : allSolutions) {
            differentSolutions[in] = s;
            in++;
        }
        try {
            for (int i = 0; i < allSolutions.size(); i++) {
                if (allSolutions.get(i).getCost() == 0)
                    throw new Exception("cout nul");
                if (!allSolutions.get(i).isValid())
                    throw new Exception("La solution " + i + " du beam n'est pas valide");
            }
            Arrays.sort(differentSolutions, new SolutionComparator());
            try {
                for (int i = 0; i < B; i++)
                    this.beam[i] = differentSolutions[i];
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Only " + differentSolutions.length + " different valid solutions in the beam of length " + this.beam.length);
                for (int i = 0; i < B; i++) {
                    System.err.print("Sol " + i + " : " + this.beam[i].getCost() + " ---");
                }
                System.err.println();
            }

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
        for (int s = 0; s < beam.length; s++) {
            System.out.print(s);
            beam[s].printShort();
        }
    }
}
