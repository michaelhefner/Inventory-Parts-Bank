/*************************************************************************
 Michael Hefner
 C482 - Software 1
 *************************************************************************/

package com.michaelhefner.Model;

public class InHouse extends Part {
    private int machineId;

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        setMachineId(machineId);
    }
}