import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;


public class ShowInfo {
    private JTable tbInfo;

    public ShowInfo(List<Stock> stocks) {
        String[][] data = parseStocks(stocks);
        String[] columnNames = {"Name", "Amount","Rate","Worth","Gained"};
        tbInfo = new JTable(data, columnNames){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public JTable getTbInfo(){
        return tbInfo;
    }

    private String[][] parseStocks(List<Stock> stocks){
        String[][] out = new String[stocks.size()][5];
        for (int i = 0; i < stocks.size(); i++) {
            Stock curr = stocks.get(i);
            int amount = curr.getAmount();
            double curValue = curr.getCurValue();
            double worth =  ((int)(amount*curValue/100 * 100)) / 100.0;
            out[i][0] =  curr.getName();
            out[i][1] =  amount   + "";
            out[i][2] =  curValue + "";
            out[i][3] =  worth + "";
            out[i][4] =  (((int)((worth-curr.getBought())*100))/100.0) + "";
        }
        return out;
    }
}
