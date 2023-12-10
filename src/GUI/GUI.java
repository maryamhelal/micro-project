package GUI;

import Memory.Instruction;
import Process.Main;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUI {

    private static void centerAlignAllTables(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
    private static int findRowIndex(DefaultTableModel model, int address) {
        for (int row = 0; row < model.getRowCount(); row++) {
            if ((int) model.getValueAt(row, 0) == address) {
                return row; // Address found in the table
            }
        }
        return -1; // Address not found in the table
    }

    public static void main(String[] args) {
    	String[] inputValues = new String[8];
    	int[] inputvalues = new int[8];
    	inputValues[0] =  JOptionPane.showInputDialog("Enter size of mul reservation station");
    	inputValues[1] =  JOptionPane.showInputDialog("Enter size of add reservation station");
    	inputValues[2] =  JOptionPane.showInputDialog("Enter size of load reservation station");
    	inputValues[3] =  JOptionPane.showInputDialog("Enter size of store reservation station");
    	inputValues[4] =  JOptionPane.showInputDialog("Enter size of mul cycles");
    	inputValues[5] =  JOptionPane.showInputDialog("Enter size of add cycles");
    	inputValues[6] =  JOptionPane.showInputDialog("Enter size of load cycles");
    	inputValues[7] =  JOptionPane.showInputDialog("Enter size of store cycles");
        for (int i = 0; i < 8; i++) {
        	try {
        		inputvalues[i] = Integer.parseInt(inputValues[i]);
        	} catch(NumberFormatException e) {
        		inputvalues[i] = 3;
        	}	
        }
//        int[] inputvalues = {2,3,3,3,8,3,2,2};
        Main main = new Main(inputvalues);
        // Create tables
        JTable MulTable = createTable("Tag", "Op", "Busy", "Vj", "Vq", "Qj", "Qk", "A");
        JTable AddTable = createTable("Tag", "Op", "Busy", "Vj", "Vq", "Qj", "Qk", "A");
        JTable LoadTable = createTable("Tag", "Busy", "Address");
        JTable Iterations = createTable("Iteration#", "Instruction", "Operand", "j", "k", "Issue", "Execution Complete", "Write Result");
        JTable RegisterFile = createTable("Tag", "Qi", "Content");
        JTable StoreTable = createTable("Tag", "Busy", "V", "Q", "Address");
        JTable MemoryTable = createTable("Address", "Content");
        centerAlignAllTables(MulTable);
        centerAlignAllTables(AddTable);
        centerAlignAllTables(LoadTable);
        centerAlignAllTables(Iterations);
        centerAlignAllTables(RegisterFile);
        centerAlignAllTables(StoreTable);
        centerAlignAllTables(MemoryTable);
        // Create frame
        JFrame frame = new JFrame("Tables Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel clockCycleLabel = new JLabel("Clock Cycle: 0", SwingConstants.CENTER);
        // Add tables to the frame with titles
        JPanel panel1 = new JPanel(new GridLayout(4, 2));
        frame.add(clockCycleLabel, BorderLayout.NORTH);
        panel1.add(createTablePanel(MulTable, "Mul Stations"));
        panel1.add(createTablePanel(AddTable, "Add Stations"));
        panel1.add(createTablePanel(LoadTable, "Load Stations"));
        panel1.add(createTablePanel(StoreTable, "Store Stations"));
        panel1.add(createTablePanel(MemoryTable, "Memory"));
        panel1.add(createTablePanel(Iterations, "Instructions Table"));
        frame.add(createTablePanel(RegisterFile, "Register File"), BorderLayout.EAST);

        JPanel subPanel1 = new JPanel(new GridLayout(2, 1));

        JLabel fetchQueueLabel = new JLabel("Fetch Queue: ", SwingConstants.CENTER);
        fetchQueueLabel.setFont(new Font("Arial", Font.BOLD, 15));
        subPanel1.add(fetchQueueLabel);
        JLabel issueQueueLabel = new JLabel("Issue Queue: ", SwingConstants.CENTER);
        issueQueueLabel.setFont(new Font("Arial", Font.BOLD, 15));
        subPanel1.add(issueQueueLabel);
        JPanel subPanel2 = new JPanel(new GridLayout(2, 1));
        JLabel executeQueueLabel = new JLabel("Execute Queue: ", SwingConstants.CENTER);
        executeQueueLabel.setFont(new Font("Arial", Font.BOLD, 15));
        subPanel2.add(executeQueueLabel);
        JLabel writeQueueLabel = new JLabel("Write Queue: ", SwingConstants.CENTER);
        writeQueueLabel.setFont(new Font("Arial", Font.BOLD, 15));
        subPanel2.add(writeQueueLabel);

        panel1.add(subPanel1, BorderLayout.CENTER);
        panel1.add(subPanel2, BorderLayout.CENTER);
        JButton nextButton = new JButton("NEXT");
        nextButton.setFont(new Font("", Font.PLAIN, 14)); // Set the font size
        nextButton.setPreferredSize(new Dimension(150, 50)); // Set the preferred size

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle NEXT button click event
                // You can increment the clock cycle count here
                clockCycleLabel.setText("Clock Cycle: " + main.getClock());

                main.runOne();
                String[] TagMul = main.getReservationStations().getTagmul();
                String[] VjMul = main.getReservationStations().getVjmul();
                String[] VkMul = main.getReservationStations().getVkmul();
                String[] QjMul = main.getReservationStations().getQjmul();
                String[] QkMul = main.getReservationStations().getQkmul();
                int[] busyMul = main.getReservationStations().getBusymul();
                String[] OpMul = main.getReservationStations().getOpmul();
                String[] TagAdd = main.getReservationStations().getTagadd();
                String[] VjAdd = main.getReservationStations().getVjadd();
                String[] VkAdd = main.getReservationStations().getVkadd();
                String[] QjAdd = main.getReservationStations().getQjadd();
                String[] QkAdd = main.getReservationStations().getQkadd();
                int[] busyAdd = main.getReservationStations().getBusyadd();
                String[] OpAdd = main.getReservationStations().getOpadd();
                String[] TagLoad = main.getReservationStations().getTagload();
                String[] AddressLoad = main.getReservationStations().getAddressload();
                int[] busyLoad = main.getReservationStations().getBusyload();
                String[] TagStore = main.getReservationStations().getTagstore();
                String[] AddressStore = main.getReservationStations().getAddressstore();
                int[] busyStore = main.getReservationStations().getBusystore();
                String[] VStore = main.getReservationStations().getVstore();
                String[] QStore = main.getReservationStations().getQstore();
                Object[] Memory = main.getMainMemory().getMemory();
                String[] TagRegister = main.getRegisterFile().getTag();
                String[] QRegister = main.getRegisterFile().getQ();
                int[] ContentRegister = main.getRegisterFile().getContent();
                ArrayList<Instruction> Instructions = main.getInstructionTable();
                String fetchresult = main.getFetchQueue();
                String issueresult = main.getIssueQueue();
                String executeresult = main.getExecuteQueue();
                String writeresult = main.getWriteQueue();
                fetchQueueLabel.setText("Fetch Queue: " + fetchresult);
                issueQueueLabel.setText("Issue Queue: " + issueresult);
                executeQueueLabel.setText("Execute Queue: " + executeresult);
                writeQueueLabel.setText("Write Queue: " + writeresult);
                
                for (int i = 0; i < Instructions.size(); i++) {
                    DefaultTableModel model = (DefaultTableModel) Iterations.getModel();
                    Object[] row = {Instructions.get(i).getIteration(), Instructions.get(i).getInstruction(), Instructions.get(i).getDestinationRegister(), Instructions.get(i).getJ(), Instructions.get(i).getK(), Instructions.get(i).getIssue(), Instructions.get(i).getExecutionComplete(), Instructions.get(i).getWriteResult()};
                    if (model.getRowCount() < Instructions.size()) {
                        model.addRow(row);
                    } else {
                        model.setValueAt(Instructions.get(i).getIteration(), i, 0);
                        model.setValueAt(Instructions.get(i).getInstruction(), i, 1);
                        model.setValueAt(Instructions.get(i).getDestinationRegister(), i, 2);
                        model.setValueAt(Instructions.get(i).getJ(), i, 3);
                        model.setValueAt(Instructions.get(i).getK(), i, 4);
                        model.setValueAt(Instructions.get(i).getIssue(), i, 5);
                        model.setValueAt(Instructions.get(i).getExecutionComplete(), i, 6);
                        model.setValueAt(Instructions.get(i).getWriteResult(), i, 7);
                    }
                }
                for (int i = 0; i < TagMul.length; i++) {
                    DefaultTableModel model = (DefaultTableModel) MulTable.getModel();
                    Object[] row = {TagMul[i], OpMul[i], busyMul[i], VjMul[i], VkMul[i], QjMul[i], QkMul[i]};
                    if (model.getRowCount() < TagMul.length) {
                        model.addRow(row);
                    } else {
                        model.setValueAt(TagMul[i], i, 0);
                        model.setValueAt(OpMul[i], i, 1);
                        model.setValueAt(busyMul[i], i, 2);
                        model.setValueAt(VjMul[i], i, 3);
                        model.setValueAt(VkMul[i], i, 4);
                        model.setValueAt(QjMul[i], i, 5);
                        model.setValueAt(QkMul[i], i, 6);
                    }
                }
                for (int i = 0; i < TagAdd.length; i++) {
                    DefaultTableModel model = (DefaultTableModel) AddTable.getModel();
                    Object[] row = {TagAdd[i], OpAdd[i], busyAdd[i], VjAdd[i], VkAdd[i], QjAdd[i], QkAdd[i]};
                    if (model.getRowCount() < TagAdd.length) {
                        model.addRow(row);
                    } else {
                        model.setValueAt(TagAdd[i], i, 0);
                        model.setValueAt(OpAdd[i], i, 1);
                        model.setValueAt(busyAdd[i], i, 2);
                        model.setValueAt(VjAdd[i], i, 3);
                        model.setValueAt(VkAdd[i], i, 4);
                        model.setValueAt(QjAdd[i], i, 5);
                        model.setValueAt(QkAdd[i], i, 6);
                    }
                }
                for (int i = 0; i < TagLoad.length; i++) {
                    DefaultTableModel model = (DefaultTableModel) LoadTable.getModel();
                    Object[] row = {TagLoad[i], busyLoad[i], AddressLoad[i]};
                    if (model.getRowCount() < TagLoad.length) {
                        model.addRow(row);
                    } else {
                        model.setValueAt(TagLoad[i], i, 0);
                        model.setValueAt(busyLoad[i], i, 1);
                        model.setValueAt(AddressLoad[i], i, 2);
                    }
                }
                for (int i = 0; i < TagStore.length; i++) {
                    DefaultTableModel model = (DefaultTableModel) StoreTable.getModel();
                    Object[] row = {TagStore[i], busyStore[i], VStore[i], QStore[i], AddressStore[i]};
                    if (model.getRowCount() < TagStore.length) {
                        model.addRow(row);
                    } else {
                        model.setValueAt(TagStore[i], i, 0);
                        model.setValueAt(busyStore[i], i, 1);
                        model.setValueAt(VStore[i], i, 2);
                        model.setValueAt(QStore[i], i, 3);
                        model.setValueAt(AddressStore[i], i, 4);
                    }

                }
                for (int i = 0; i < Memory.length; i++) {
                    DefaultTableModel model = (DefaultTableModel) MemoryTable.getModel();
                    if (main.getMainMemory().getChanged()[i] == 1) {
                        Object[] row = {i, Memory[i]};
                        int rowIndex = findRowIndex(model, i);
                        if (rowIndex == -1) {
                            // Address not found in the table, add a new row
                            model.addRow(row);
                        } else {
                            // Address found in the table, update the existing row
                            model.setValueAt(Memory[i], rowIndex, 1);
                        }
                    }
                }
                for (int i = 0; i < TagRegister.length; i++) {
                    DefaultTableModel model = (DefaultTableModel) RegisterFile.getModel();
                    Object[] row = {TagRegister[i], QRegister[i], ContentRegister[i]};
                    if (model.getRowCount() < TagRegister.length) {
                        model.addRow(row);
                    } else {
                        model.setValueAt(TagRegister[i], i, 0);
                        model.setValueAt(QRegister[i], i, 1);
                        model.setValueAt(ContentRegister[i], i, 2);
                    }
                }
            }
        });
        frame.add(nextButton, BorderLayout.SOUTH);
        frame.add(panel1, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel createTablePanel(JTable table, String title) {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(title));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        return tablePanel;
    }
    private static JTable createTable(String... columnNames) {
        // Create table model
        DefaultTableModel model = new DefaultTableModel();
        for (String columnName : columnNames) {
            model.addColumn(columnName);
        }
        // Create table with the model
        JTable table = new JTable(model);
        // Optionally, set column widths, row heights, etc.
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        return table;
    }
}
