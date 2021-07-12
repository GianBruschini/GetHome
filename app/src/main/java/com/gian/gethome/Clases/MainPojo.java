package com.gian.gethome.Clases;

import java.util.ArrayList;

public class MainPojo {
    String status;
    ArrayList<ListClass>predictions;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<ListClass> getPredictions() {
        return predictions;
    }

    public void setPredictions(ArrayList<ListClass> predictions) {
        this.predictions = predictions;
    }
}
