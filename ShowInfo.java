import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;


public class ShowInfo {
    private JTable tbInfo;
    private JFrame frame;

    public ShowInfo(JFrame frame) {
        this.frame = frame;
        tbInfo = new JTable(new DefaultTableModel(new String[]{"Name", "Amount","Rate","Worth","Gained"},0)){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public JTable getTbInfo(){
        return tbInfo;
    }

    private String[] parseStock(Stock stock){
        String[] out = new String[5];
        int amount = stock.getAmount();
        double curValue = stock.getCurValue();
        double worth =  ((int)(amount*curValue/100 * 100)) / 100.0;
        out[0] =  stock.getName();
        out[1] =  amount   + "";
        out[2] =  curValue + "";
        out[3] =  worth + "";
        out[4] =  (((int)((worth-stock.getBought())*100))/100.0) + "";
        return out;
    }

    public void notify(Stock stock){
        DefaultTableModel model = (DefaultTableModel) tbInfo.getModel();
        model.addRow(parseStock(stock));
        frame.revalidate();
        frame.repaint();
    }
}
