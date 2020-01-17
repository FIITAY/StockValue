import javax.swing.*;


public class ShowInfo {
    private JTable tbInfo;

    public ShowInfo() {
        Stock[] stocks = (new Main()).getStocks();
        String[][] data = parseStocks(stocks);
        String[] columnNames = {"Name", "Amount","Rate","Worth","Gained"};
        tbInfo = new JTable(data, columnNames);
    }
//
    private String[][] parseStocks(Stock[] stocks){
        String[][] out = new String[stocks.length][5];
        for (int i = 0; i < stocks.length; i++) {
            int amount = stocks[i].getAmount();
            double curValue = stocks[i].getCurValue();
            double worth =  ((int)(amount*curValue/100 * 100)) / 100.0;
            out[i][0] =  stocks[i].getName();
            out[i][1] =  amount   + "";
            out[i][2] =  curValue + "";
            out[i][3] =  worth + "";
            out[i][4] =  (((int)((worth-stocks[i].getBought())*100))/100.0) + "";
        }
        return out;
    }

    public static void main(String[] args) {
        double sTime = System.currentTimeMillis();
        JFrame frame = new JFrame("Stocks");
        JScrollPane sp = new JScrollPane(new ShowInfo().tbInfo);
        frame.add(sp);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        double fTime = System.currentTimeMillis();
        System.out.println(fTime-sTime);
    }
}
