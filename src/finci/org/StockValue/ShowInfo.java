import javax.swing.*;
import javax.swing.table.DefaultTableModel;


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

    public String[] parseStock(Stock stock){
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
        notify(parseStock(stock));
    }

    public void notify(String[] summery){
        DefaultTableModel model = (DefaultTableModel) tbInfo.getModel();
        model.addRow(summery);
        frame.revalidate();
        frame.repaint();
    }

    public void reset(){
        DefaultTableModel dm = (DefaultTableModel) tbInfo.getModel();
        for (int i= dm.getRowCount() -1; i>=0;i--){
            dm.removeRow(i);
        }
    }
}
