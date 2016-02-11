package Instance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by n on 08/02/16.
 */
public class Instance {
    private String fileName;

    //--------------------Donn�es du probl�me-----------------------
    private int nbM1;//nb de machines nbM1
    private int nbM2;//nb de machines nbM2
    private int nbJob;//nb de commandes
    private int nbProd;//nb de produits
    private int nbTypes;//nb de types

    int[][] setUp;//nbProd*nbProd, setUp[i][j]=1 si il faut un set-up entre le produit i et le produit j (i avant, j apr�s)
    double[][] duration;//nbM2*nbTypes, �gal � (1 / vitesse de production de la machine f pour le type t), donc Tprocess=quantity*duration.
    int[] setUpTimeM;//nbM1, �gal au temps de set-up de la machine m
    int[] setUpTimeF;//nbM2, �gal au temps de set-up de la machine f
    Job[] jobs;//nbJob, Liste des jobs alias commandes

    int[][] stockCapa; //nbM2*3, capacité des stocks par machine F -- ordonnées par ordre croissant

    //----------------------Accesseurs----------------------------
    public String getFileName() {
        return fileName;
    }

    public int getNbM1() {
        return nbM1;
    }

    public int getNbM2() {
        return nbM2;
    }

    public int getNbJob() {
        return nbJob;
    }

    public int getNbProd() {
        return nbProd;
    }

    public int getNbTypes() {
        return nbTypes;
    }

    public int[][] getSetUp() {
        return setUp;
    }

    public int getSetUp(int i, int j) {
        return getSetUp()[i][j];
    }

    public double[][] getDuration() {
        return duration;
    }

    public double getDuration(int i, int j) {
        return getDuration()[i][j];
    }

    public int[] getSetUpTimeM() {
        return setUpTimeM;
    }

    public int[] getSetUpTimeF() {
        return setUpTimeF;
    }

    public int getSetupTime(int stage, int machine) {
        if (stage == 1) return getSetUpTimeM()[machine];
        else if (stage == 2) return getSetUpTimeF()[machine];
        else return 1000;
    }

    public Job[] getJobs() {
        return jobs;
    }

    public Job getJob(int i) {
        return getJobs()[i];
    }

    public int[] getStockCapa(int machine) {
        return stockCapa[machine];
    }

    //-----------------------Constructeur--------------------------

    /**
     * From a file
     *
     * @param fName
     * @throws IOException
     */
    public Instance(String fName) throws IOException {
        fileName = fName;
        read();
    }

    /**
     * An exemple
     */
    public Instance() {
        this.nbM1 = 1;
        this.nbM2 = 3;
        this.nbJob = 5;
        this.nbProd = 5;
        this.nbTypes = 4;

        this.setUp = new int[nbProd][nbProd];
        for (int i = 0; i < nbProd; i++) {
            for (int j = 0; j < nbProd; j++) {
                if (i == j) {
                    this.setUp[i][j] = 0;
                } else {
                    this.setUp[i][j] = 1;
                }
            }
        }

        this.duration = new double[nbM2][nbTypes];
        for (int i = 0; i < nbM2; i++) {
            for (int j = 0; j < nbTypes; j++) {
                this.duration[i][j] = 1;
            }
        }

        this.setUpTimeM = new int[nbM1];
        for (int i = 0; i < nbM1; i++) {
            this.setUpTimeM[i] = 1;
        }
        this.setUpTimeF = new int[nbM2];
        for (int i = 0; i < nbM2; i++) {
            this.setUpTimeF[i] = 1;
        }

        this.jobs = new Job[nbJob];//Convention : Job 0 n'est pas utilis�, Commande vide
        this.jobs[0] = new Job(1, 0, 0, 1, 20);
        this.jobs[1] = new Job(2, 1, 2, 2, 20);
        this.jobs[2] = new Job(3, 3, 1, 3, 20);
        this.jobs[3] = new Job(4, 1, 4, 3, 20);
        this.jobs[4] = new Job(5, 2, 2, 4, 20);

        this.stockCapa = new int[nbM2][3];
        for(int i = 0; i<stockCapa.length; i++){
            stockCapa[i][0] = 50;
            stockCapa[i][1] = 50;
            stockCapa[i][2] = 50;
        }
    }

    /**
     * Instance aléatoire
     *
     * @param M       : number at first floor
     * @param F       : number at second floor
     * @param nbJob
     * @param nbProd
     * @param nbTypes
     */
    public Instance(int M, int F, int nbJob, int nbProd, int nbTypes) {
        this.nbM1 = M;
        this.nbM2 = F;
        this.nbJob = nbJob;
        this.nbProd = nbProd;
        this.nbTypes = nbTypes;

        this.setUp = new int[nbProd][nbProd];
        for (int i = 0; i < nbProd; i++)
            for (int j = 0; j < nbProd; j++)
                if (i == j) this.setUp[i][j] = 0;
                else this.setUp[i][j] = (int) Math.random();

        this.duration = new double[nbM2][nbTypes];
        for (int i = 0; i < nbM2; i++)
            for (int j = 0; j < nbTypes; j++)
                this.duration[i][j] = (0.5 + (2 * Math.random()));

        this.setUpTimeM = new int[nbM1];
        for (int i = 0; i < nbM1; i++)
            this.setUpTimeM[i] = (int) (1 + 4 * Math.random());

        this.setUpTimeF = new int[nbM2];
        for (int i = 0; i < nbM2; i++)
            this.setUpTimeF[i] = (int) (1 + 4 * Math.random());


        this.jobs = new Job[nbJob];
        for (int i = 0; i < nbJob; i++)
            this.jobs[i] = new Job(i+1, (int)(nbProd*Math.random()), (int)(nbTypes*Math.random()), (int)(2+18*Math.random()), (int)(3*i+5*Math.random()));

        this.stockCapa = new int[nbM2][3];
        for(int i = 0; i<stockCapa.length; i++){
            stockCapa[i][0] = 50;
            stockCapa[i][1] = 50;
            stockCapa[i][2] = 50;
        }
    }

    /**
     * ------------------A FAIRE-----------------------------
     * Lis l'instance � partir de son nom, et set les donn�es dans les variables d'instances
     */
    public void write(String fileName){
        File file = new File(fileName);
        FileWriter fw;

        try{
            fw = new FileWriter(file);
            fw.write("nbM1 "+this.getNbM1()+"\n");
            fw.write("nbM2 "+this.getNbM2()+"\n");
            fw.write("nbJob "+this.nbJob+"\n");
            fw.write("nbProd "+this.nbProd+"\n");
            fw.write("nbTypes "+this.nbTypes+"\n");
            fw.write("setUp\n");
            String setUpString = "";
            for(int i=0; i<this.setUp.length; i++){
                for(int j=0; j<this.setUp[0].length ; j++){
                    setUpString += this.setUp[i][j]+" ";
                }
                setUpString += "\n";
            }
            fw.write(setUpString);

            fw.write("duration\n");
            String durationString  = "";
            for(int i=0; i<this.duration.length; i++){
                for(int j=0; j<this.duration[0].length ; j++){
                    durationString += this.duration[i][j]+" ";
                }
                durationString += "\n";
            }
            fw.write(durationString);


            int[] setUpTimeM;//nbM1, �gal au temps de set-up de la machine m
            int[] setUpTimeF;//nbM2, �gal au temps de set-up de la machine f
            Job[] jobs;//nbJob, Liste des jobs alias commandes

            int[][] stockCapa; //nbM2*3, capacité des stocks par machine F -- ordonnées par ordre croissant
            fw.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void read(String fileName){
        File f = new File(fileName);

        System.out.println("Chemin absolu du fichier : " + f.getAbsolutePath());

        System.out.println("Nom du fichier : " + f.getName());

        System.out.println("Est-ce qu'il existe ? " + f.exists());

        System.out.println("Est-ce un répertoire ? " + f.isDirectory());

        System.out.println("Est-ce un fichier ? " + f.isFile());

    }
    public void read() throws IOException {
     /*   File mfile = new File(fileName);
        if (!mfile.exists()) {
            throw new IOException("Le fichier saisi : "+ fileName + ", n'existe pas.");
        }
        Scanner sc = new Scanner(mfile);

        String line;
        do {
            line = sc.nextLine();
            System.err.println(line);
        }
        while (!line.startsWith("DIMENSION"));
        Scanner lineSc = new Scanner(line);
        lineSc.next();
        if (!lineSc.hasNextInt()) {
            lineSc.next();
        }
       /* nbSommets =lineSc.nextInt();
        coordX = new double[nbSommets];
        coordY = new double[nbSommets];
        labels = new String[nbSommets];
        demande = new int[nbSommets];
        datePlusTot = new int[nbSommets];
        datePlusTard = new int[nbSommets];
        tempsService = new int[nbSommets];




        while (!line.startsWith("CAPACITE")) {
            line = sc.nextLine();
            System.err.println(line);
        }
        lineSc.close();
        lineSc = new Scanner(line);
        lineSc.next();
        if (!lineSc.hasNextInt()) {
            lineSc.next();
        }
        capaVehicule =lineSc.nextInt();

        while (!line.contains("ID")) {
            line = sc.nextLine();
            System.err.println(line);
        }
        line = sc.nextLine();

        int idx = 0;
        for (int s=0;s<nbSommets;s++){
            assert(idx<nbSommets);
            lineSc = new Scanner(line);
            lineSc.useLocale(Locale.US);
            labels[idx] = lineSc.next();
            coordX[idx] = lineSc.nextDouble();
            coordY[idx] = lineSc.nextDouble();
            demande[idx]= lineSc.nextInt();
            datePlusTot[idx]= lineSc.nextInt();
            datePlusTard[idx] = lineSc.nextInt();
            tempsService[idx]=lineSc.nextInt();
            line = sc.nextLine();
            idx++;
        }

        // Cr�ation de la matrice de distances
        distances = new long[nbSommets][];
        for (int i = 0; i < nbSommets; i++) {
            distances[i] = new long[nbSommets];
        }

        // Calcul des distances
        for (int i = 0; i < nbSommets; i++) {
            distances[i][i] = 0;
            for (int j = i + 1; j < nbSommets; j++) {
                long dist = distance(i,j);
                //				System.out.println("Distance " + i + " " +j + ": " + dist);
                distances[i][j] = dist;
                distances[j][i] = dist;
            }
        }
        lineSc.close();
        sc.close();*/
    }
}
