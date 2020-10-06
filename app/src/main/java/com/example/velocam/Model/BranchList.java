package com.example.velocam.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BranchList {
    @SerializedName("result")
    @Expose
    private List<Branch> result=null;


    public List<Branch> getResult() {
        return result;
    }

    public void setResult(List<Branch> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Branch [branches=" + result + "]";
    }
}
