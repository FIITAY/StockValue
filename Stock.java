import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class Stock {
    private String name;
    private URL investingURL;
    private int amount;
    private double bought;
    private Date lastUpdate;
    private double curValue;

    public Stock(URL investingURL, int amount, double bought) {
        this.name = "";
        this.investingURL = investingURL;
        this.amount = amount;
        this.bought = bought;
        curValue = -1;
        getCurValue();
    }

    public Stock(String name, URL investingURL, int amount, double bought) {
        this.name = name;
        this.investingURL = investingURL;
        this.amount = amount;
        this.bought = bought;
        curValue = -1;
        getCurValue();
    }

    private final int MINUTES_DIFFERENCE = 10;

    /**
     * check if there is need to update cur value, if not return the old value
     * @return curValue
     */
    public double getCurValue(){
        if(curValue != -1){
            if(lastUpdate != null && ((new Date()).getTime() - lastUpdate.getTime()) > MINUTES_DIFFERENCE*60*1000){
                updateCurValue();
            }
        }else{
            updateCurValue();
        }
        return curValue;
    }

    /**
     * update the curValue and lastUpdate
     */
    private void updateCurValue(){
        try {
            this.curValue = this.getStockValue();
            lastUpdate = new Date();
        }catch(Exception except){
            System.out.println("Error with the url/site.");
            lastUpdate = null;
            curValue = -1;
        }
    }

    /**
     * update the name
     * @param name a non empty name
     */
    public void updateName(String name){
        if( name != null && !name.isEmpty()){
            this.name= name;
        }
    }

    /**
     * gets a stock value from investor.com url
     * @return return the value of the stock OR VALUE_NOT_FOUND as the program need to
     * @throws IOException the system will throw this exception when the connection to the url was failed.
     * @throws ValueNotFoundException a program defined exception that shows that there wasnt any value found in the webpage
     */
    private double getStockValue() throws IOException, ValueNotFoundException {
        //open new connection to the stock and configure it to fool the website to think this is a user
        URLConnection uc = investingURL.openConnection();
        uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        //get regular java input stream of the html
        InputStream is = uc.getInputStream();
        //make reader that gives me line each time
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = null;
        // read each line
        while ((line = br.readLine()) != null) {
            //if name is null- update the name also
            if(name.isEmpty() && line.contains("<h1")){
                //the actual name is in the next line
                line = br.readLine();
                //this line includes the name seperate it right and then insert into name var
                String[] split = line.split(">");
                this.name = split[1].split("<")[0];
            }
            //if the html in this line includes a tag that his id is last_last, this id identifies the value of the stock
            if(line.contains("id=\"last_last\"")){
                //split the string until i have in value the stock value only
                String[] split = line.split(">");
                String temp = split[1].split("<")[0];
                return Double.parseDouble(temp.replace(",",""));
            }
        }
        throw new ValueNotFoundException();
    }

    @Override
    public String toString(){
        String out = name + "\t"+"amount: "+amount+"\t";
        getCurValue();
        if(curValue != -1){
            out += "rate: "+curValue+"\t";
            double worth =  ((int)((amount*curValue/100) * 100)) / 100.0;
            out += "worth: "+worth+"\t"
                    +"gained: "+(((int)((worth-bought)*100))/100.0);
        }else{
            out += "rate: "+"ERROR"+"\t";
            out += "worth: "+"ERROR"+"\t"
                    +"gained: "+"ERROR";
        }
        return out;
    }
}

