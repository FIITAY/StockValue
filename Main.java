import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Stock[] stocks =  new Stock[4];
        //initialize the stocks into the array and then print the stocks
        if(initialize(stocks)){
            printStocks(stocks);
        }
        Scanner s = new Scanner(System.in);
        while(!s.next().equals("q")){
            printStocks(stocks);
        }
    }

    static boolean initialize(Stock[] stocks){
        try {
            //make new stock object from each stock
            stocks[0] = new Stock(new URL("https://www.investing.com/etfs/harel-sal-4a-sp-500-currency-hedged")
                    , 175, 4176.17);
            stocks[1] = new Stock(new URL("https://www.investing.com/etfs/harel-sal-4a-sp-500-currency-hedged")
                    , 155, 4983.25);
            stocks[2] = new Stock(new URL("https://www.investing.com/funds/kesem-ktf-non-cpi-gvt-bd-fix-intrst")
                    , 7487, 9900.06);
            stocks[3] = new Stock(new URL("https://www.investing.com/etfs/tachlit-sal-4d-nasdaq-100")
                    , 58, 7998.2);

        } catch (MalformedURLException mue){
            //one of the urls wasnt accepted
            System.out.println("check the urls");
            return false;
        }
        //everything is right
        return true;
    }

    static void printStocks(Stock[] stocks){
        //will hold each stock toString
        String[] out = new String[stocks.length];
        //will hold all of the threads
        Thread[] t = new Thread[stocks.length];
        for (int i = 0; i < out.length; i++) {
            //make new thread for each stock and put into the right out place the right toString
            int finalI = i;
            t[i] = new Thread(() -> out[finalI] = stocks[finalI].toString());
            //start the thread
            t[i].start();
        }
        boolean notPrinted = true;
        while(notPrinted) {
            boolean isAlive = false;
            //check if one of the threads is alive continue to the next iteration to wait for all of them
            for(Thread thread : t){
                if(thread.isAlive() && !isAlive){
                    isAlive = true;
                }
            }
            //if all of the threads are dead print the output
            if (!isAlive) {
                //sort the names of the stocks alphabetic
                Arrays.sort(out);
                //print the out while fixing the string from beiing [ "", "", "", ""] to ""\n""\n""\n""
                System.out.println(Arrays.toString(out).replace(", ", "\n").
                        replace("[", "").replace("]", ""));
                notPrinted = false;//stop the while
            }
        }
    }
}



