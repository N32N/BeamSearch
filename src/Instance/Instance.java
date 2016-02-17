package Instance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

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
    private int coutProd;
    private int coutPenalite;

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

    public int getCoutProd() {
        return coutProd;
    }

    public int getCoutPenalite() {
        return coutPenalite;
    }

    //-----------------------Constructeur--------------------------

    /**
     * From a file
     *
     * @param fName
     * @throws IOException
     */
    public Instance(String fName) {
        fileName = fName;
        read(fileName);
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
        this.coutProd = 1;
        this.coutPenalite = 3;

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
        this.jobs[3] = new Job(4, 1, 3, 3, 20);
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
    public Instance(int M, int F, int nbJob, int nbProd, int nbType, int nbTypes, int coutPenalite) {
        this.nbM1 = M;
        this.nbM2 = F;
        this.nbJob = nbJob;
        this.nbProd = nbProd;
        this.nbTypes = nbTypes;
        this.coutProd = 1;
        this.coutPenalite =coutPenalite;

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
     *Crée une fichier texte à partir des variable d'instances actuelles.
     */
    public void write(String fileName){
        File file = new File("instances/"+fileName);
        FileWriter fw;

        try{
            fw = new FileWriter(file);
            fw.write("nbM1 "+this.getNbM1()+"\n");
            fw.write("nbM2 "+this.getNbM2()+"\n");
            fw.write("nbJob " + this.nbJob + "\n");
            fw.write("nbProd " + this.nbProd + "\n");
            fw.write("nbTypes "+this.nbTypes+"\n");
            fw.write("coutProd "+this.coutProd+"\n");
            fw.write("coutPenalite"+this.coutPenalite+"\n");
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

            fw.write("setUpTimeM\n");
            String Mstring = "";
            for(int i=0; i<this.setUpTimeM.length; i++){
                Mstring += this.setUpTimeM[i]+" ";
            }
            Mstring+="\n";
            fw.write(Mstring);

            fw.write("setUpTimeF\n");
            String Fstring = "";
            for(int i=0; i<this.setUpTimeF.length; i++){
                Fstring += this.setUpTimeF[i]+" ";
            }
            Fstring+="\n";
            fw.write(Fstring);

            String jobsString = "jobs\n";
            for(int i=0; i< this.getNbJob(); i++){
                jobsString+=this.getJob(i).getAsString()+"\n";
            }
            fw.write(jobsString);

            fw.write("stockCapa\n");
            String stockString  = "";
            for(int i=0; i<this.stockCapa.length; i++){
                for(int j=0; j<this.stockCapa[0].length ; j++){
                    stockString += this.stockCapa[i][j]+" ";
                }
                stockString += "\n";
            }
            fw.write(stockString);

            fw.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Lis l'instance à partir du fichier texte selectionné
     * @param fileName
     */
    public void read(String fileName){
        try{
            Scanner sc = new Scanner(new File("instances/"+fileName));
            Scanner lineSc = new Scanner(sc.nextLine());

            lineSc.next();
            this.nbM1 = lineSc.nextInt();

            lineSc=new Scanner(sc.nextLine());
            lineSc.next();
            this.nbM2 = lineSc.nextInt();

            lineSc=new Scanner(sc.nextLine());
            lineSc.next();
            this.nbJob = lineSc.nextInt();

            lineSc=new Scanner(sc.nextLine());
            lineSc.next();
            this.nbProd = lineSc.nextInt();

            lineSc=new Scanner(sc.nextLine());
            lineSc.next();
            this.nbTypes= lineSc.nextInt();

            lineSc=new Scanner(sc.nextLine());
            lineSc.next();
            this.coutProd= lineSc.nextInt();

            lineSc=new Scanner(sc.nextLine());
            lineSc.next();
            this.coutPenalite= lineSc.nextInt();

            sc.nextLine();
            this.setUp = new int[nbProd][nbProd];
            for(int i=0; i <nbProd; i++){
                lineSc = new Scanner(sc.nextLine());
                for(int j=0; j<nbProd; j++){
                    this.setUp[i][j] = lineSc.nextInt();
                }
            }

            sc.nextLine();
            this.duration = new double[nbM2][nbTypes];
            for(int i=0; i <nbM2; i++){
                lineSc = new Scanner(sc.nextLine()).useLocale(Locale.US);
                for(int j=0; j<nbTypes; j++){
                    this.duration[i][j] = lineSc.nextDouble();
                }
            }
            sc.nextLine();

            this.setUpTimeM = new int[this.nbM1];
            lineSc = new Scanner(sc.nextLine());
            for(int i=0; i<nbM1; i++){
                this.setUpTimeM[i]=lineSc.nextInt();
            }
            sc.nextLine();

            this.setUpTimeF = new int[this.nbM2];
            lineSc = new Scanner(sc.nextLine());
            for(int i=0; i<nbM2; i++){
                this.setUpTimeF[i]=lineSc.nextInt();
            }
            sc.nextLine();

            this.jobs = new Job[nbJob];//Convention : Job 0 n'est pas utilis�, Commande vide
            for(int i=0; i<nbJob; i++){
                lineSc = new Scanner(sc.nextLine());
                this.jobs[i] = new Job(lineSc.nextInt(), lineSc.nextInt(),lineSc.nextInt(),lineSc.nextInt(),lineSc.nextInt() );
            }
            sc.nextLine();

            this.stockCapa = new int[nbM2][3];
            for(int i=0; i <nbM2; i++){
                lineSc = new Scanner(sc.nextLine());
                for(int j=0; j<3; j++){
                    this.stockCapa[i][j] = lineSc.nextInt();
                }
            }
            sc.close();

        } catch (FileNotFoundException e ){
            System.out.println("Le fichier " + fileName + " n'existe pas");
        }

    }
}
