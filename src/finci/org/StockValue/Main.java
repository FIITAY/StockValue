package finci.org.StockValue;
import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.util.*;


public class Main {
    public static final int CLOCK_CYCLE_MILLIS = 300000;
    private static String fileIn;
    public static void main(String[] args) {
        LinkedList<Stock> head = new LinkedList<>();
        //make the view
        JFrame frame = new JFrame("Stocks");;
        ShowInfo si = makeView(frame);
        //initialize the file name
        fileIn = getFileName(args);
        //initialize the list
        try {
            initialize(head,si);
        }catch (FileNotFoundException f){
            //show error message
            JOptionPane.showMessageDialog(frame, "file not found.\n use \"-f\" argument to point on file.\n" +
                                            "use stocks.dt file as default.",
                                            "file not found", JOptionPane.ERROR_MESSAGE);
            //close the program
            System.exit(0);
        }
        //add a clock
        new Thread(()-> {
            while(true){
                try{
                    Thread.sleep(CLOCK_CYCLE_MILLIS);
                }catch (InterruptedException exception){
                    break;
                }
                si.reset();
                update(head, si);

            }
        }).start();
    }

    static ShowInfo makeView(JFrame frame){
        ShowInfo si = new ShowInfo(frame);
        JScrollPane sp = new JScrollPane(si.getTbInfo());
        frame.add(sp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        return si;
    }

    /**
     * if user enter the argument -f, then the next argument is the file name.
     * default value is stocks.dt file.
     * @param args the program arguments.
     * @return the file path
     */
    static String getFileName(String[] args){
        for (int i = 0; i < args.length-1; i++) {
            if(args[i].equals("-f")){
                return args[i+1];
            }
        }
        return "./stocks.dt";
    }

    static void update(LinkedList<Stock> stocks, ShowInfo si){
        LinkedList<Thread> threads = new LinkedList<>();
        for(Stock s: stocks){
            Thread t = new Thread(()->si.notify(s));
            t.start();
            threads.add(t);
        }
        summery(threads,stocks,si);
    }

    static void initialize(LinkedList<Stock> stocks, ShowInfo si) throws FileNotFoundException{
        //initialize from file
        File file = new File(fileIn);
        Scanner scan = new Scanner(file);
        LinkedList<Thread> threads = new LinkedList<>();
        while(scan.hasNext()){
            String line = scan.nextLine();
            Thread t = new Thread(() -> {
                try{
                    Stock newS  = new Stock(line);
                    si.notify(newS);
                    stocks.add(newS);
                }catch (MalformedURLException m){
                    //nothing
                }
            });
            t.start();
            threads.add(t);
        }
        summery(threads,stocks,si);

    }

    private static void summery(LinkedList<Thread> threads, LinkedList<Stock> stocks, ShowInfo si){
        //wait for all threads to finish
        boolean isAlive = true;
        while(isAlive){
            isAlive = false;
            for (int i = 0; i < threads.size() && !isAlive; i++) {
                isAlive = threads.get(i).isAlive();
            }
        }
        //all thread finished, sum every thing up to make summery
        //the amount and rate arent interesting
        double sumBought = 0;
        double sumWorth = 0;
        for (Stock s : stocks){
            sumBought+=s.getBought();
            sumWorth += ((int)(s.getAmount()*s.getCurValue()/100 * 100)) / 100.0;
        }
        si.notify(new String[]{"Summery","-","-",""+sumWorth,""+(((int)((sumWorth-sumBought)*100))/100.0)});
    }
}



