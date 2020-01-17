import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        double start = System.currentTimeMillis();
        LinkedList<Stock> head = new LinkedList<Stock>();
        try {
            initialize(head);
        }catch (MalformedURLException m){
            System.out.println("check URLS");
        }catch (FileNotFoundException f){
            System.out.println("check file exsistance");
        }
        JFrame frame = new JFrame("Stocks");
        JScrollPane sp = new JScrollPane(new ShowInfo(head).getTbInfo());
        frame.add(sp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        System.out.println(""+(System.currentTimeMillis()-start));
    }

    static void initialize(LinkedList<Stock> stocks) throws MalformedURLException, FileNotFoundException{
        //initialize from file
        File file = new File("stocks.dt");
        Scanner scan = new Scanner(file);
        while(scan.hasNext()){
            String line = scan.nextLine();
            stocks.add(new Stock(line));
        }
    }
}



