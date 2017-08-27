package com.huawei.ptn.opensourceDigger.model;

public class ProjectGroups {
    private int ID;

    private String NAME;

    private String OWNER;

    private int ISSUESCORE;

    private int MRSCORE;

    private int PROJECTSCORES;

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String nAME) {
        NAME = nAME;
    }

    public String getOWNER() {
        return OWNER;
    }

    public void setOWNER(String oWNER) {
        OWNER = oWNER;
    }


    //add rank scores for each project
    public int getISSUESCORE() {  return ISSUESCORE; }

    public void setISSUESCORE(int iSSUESCORE) {
        ISSUESCORE = iSSUESCORE;
    }

    public int getMRSCORE() { return MRSCORE; }

    public void setMRSCORE(int mRSCORE) {
        MRSCORE = mRSCORE;
    }

    public void setPROJECTSCORES(int pROJECTSCORES) {
        PROJECTSCORES = pROJECTSCORES;
    }

    public int getPROJECTSCORES() {  return PROJECTSCORES; }



}
