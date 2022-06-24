package com.iwaa.client.gui.Table;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class RouteTableModel extends AbstractTableModel {

    private static final int COLUMN_COUNT = 8;
    private final ArrayList<String[]> dataArrayList;
    private final ArrayList<String> listOfColumns;

    public RouteTableModel(ArrayList<String> listOfColumns) {
        dataArrayList = new ArrayList<>();
        for (int i = 0; i < dataArrayList.size(); i++) {
            dataArrayList.add(new String[getColumnCount()]);
        }
        this.listOfColumns = listOfColumns;
    }

    public ArrayList<String[]> getDataArrayList() {
        return dataArrayList;
    }

    @Override
    public int getRowCount() {
        return dataArrayList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return listOfColumns.get(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String[] rows = dataArrayList.get(rowIndex);
        return rows[columnIndex];
    }

    public void clear() {
        dataArrayList.clear();
    }

    public void addDate(String[] row) {
        dataArrayList.add(row);
    }

}
