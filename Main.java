import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        double start = System.currentTimeMillis();
        LinkedList<Stock> head = new LinkedList<>();
        //make the view
        JFrame frame = new JFrame("Stocks");
        ShowInfo si = new ShowInfo(frame);
        JScrollPane sp = new JScrollPane(si.getTbInfo());
        frame.add(sp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        //initialize the list
        try {
            initialize(head,si);
        }catch (MalformedURLException m){
            System.out.println("check URLS");
        }catch (FileNotFoundException f){
            System.out.println("check file exsistance");
        }
        System.out.println(""+(System.currentTimeMillis()-start));
    }

    static void initialize(LinkedList<Stock> stocks, ShowInfo si) throws MalformedURLException, FileNotFoundException{
        //initialize from file
        File file = new File("stocks.dt");
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
        boolean isAlive = true;
        while(isAlive){
            isAlive = false;
            //check if one of the threads is alive continue to the next iteration to wait for all of them
            for(Thread thread : threads){
                if(thread.isAlive() && !isAlive){
                    isAlive = true;
                }
            }
        }
    }
}



