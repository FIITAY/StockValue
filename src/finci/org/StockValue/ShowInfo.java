package finci.org.StockValue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ShowInfo {
    private JTable tbInfo;
    private JFrame frame;

    private int sumWorth;
    private int sumBought;
    private int sumGained;

    public ShowInfo(JFrame frame) {
        this.frame = frame;
        tbInfo = new JTable(new DefaultTableModel(new String[]{"Name", "Amount","Rate","Worth","Gained"},0)){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        //update to 0 all of the summery
        sumWorth= 0;
        sumBought=0;
        sumGained=0;
        //add initial summery
        ((DefaultTableModel)tbInfo.getModel()).addRow(new String[] {"Summery","-","-",""+sumWorth,""+sumBought});
    }

    public JTable getTbInfo(){
        return tbInfo;
    }

    public String[] parseStock(Stock stock){
        String[] out = new String[5];
        int amount = stock.getAmount();
        double curValue = stock.getCurValue();
        double worth =  ((int)(amount*curValue/100 * 100)) / 100.0;
        double gained = (((int)((worth-stock.getBought())*100))/100.0);
        out[0] =  stock.getName();
        out[1] =  amount   + "";
        out[2] =  curValue + "";
        out[3] =  worth + "";
        out[4] =  gained + "";
        //update summery.
        sumBought += stock.getBought();
        sumWorth += worth;
        sumGained += gained;
        return out;
    }

    public void notify(Stock stock){
        notify(parseStock(stock));
    }

    public void notify(String[] stock){
        DefaultTableModel model = (DefaultTableModel) tbInfo.getModel();
        //remove the rows that are summery
        for(int i=0; i<model.getRowCount();i++){
            if(model.getValueAt(i,0).equals("Summery"))
                model.removeRow(i);
        }
        //add the row
        model.addRow(stock);
        //make the summery row
        model.addRow(new String[] {"Summery","-","-",""+sumWorth,""+sumGained});
        frame.revalidate();
        frame.repaint();
    }

    public void reset(){
        DefaultTableModel dm = (DefaultTableModel) tbInfo.getModel();
        for (int i= dm.getRowCount() -1; i>=0;i--){
            dm.removeRow(i);
        }
        //reset the summery
        sumGained =0;
        sumBought=0;
        sumWorth=0;
    }
}
