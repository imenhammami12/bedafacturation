package entities;

import java.sql.Date;

public class Facture {
int id;
int CodeClient , quantite ;
private String designation;
Date date ;
float PU ,Pt ,Total ,Fodec ,TVA ,TotalTTC,NetAPayer ;



public String getDesignation() {
    return designation;
}

public void setDesignation(String designation) {
    this.designation = designation;
}



public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getCodeClient() {
	return CodeClient;
}
public void setCodeClient(int codeClient) {
	CodeClient = codeClient;
}
public int getQuantite() {
	return quantite;
}
public void setQuantite(int quantite) {
	this.quantite = quantite;
}

public Date getDate() {
	return date;
}
public void setDate(Date date) {
	this.date = date;
}
public float getPU() {
	return PU;
}
public void setPU(float pU) {
	PU = pU;
}
public float getPt() {
	return Pt;
}
public void setPt(float pI) {
	Pt = pI;
}
public float getTotal() {
	return Total;
}
public void setTotal(float total) {
	Total = total;
}
public float getFodec() {
	return Fodec;
}
public void setFodec(float fodec) {
	Fodec = fodec;
}
public float getTVA() {
	return TVA;
}
public void setTVA(float tVA) {
	TVA = tVA;
}
public float getTotalTTC() {
	return TotalTTC;
}
public void setTotalTTC(float totalTTC) {
	TotalTTC = totalTTC;
}
public float getNetAPayer() {
	return NetAPayer;
}
public void setNetaPayer(float netaPayer) {
	NetAPayer = netaPayer;
}
public Facture(int id, int codeClient, int quantite, String designation, java.util.Date date2, float pU, float pt, float total,
		float fodec, float tVA, float totalTTC, float netaPayer) {
	super();
	this.id = id;
	CodeClient = codeClient;
	this.quantite = quantite;
	this.designation = designation;
	this.date = (Date) date2;
	PU = pU;
	Pt = pt;
	Total = total;
	Fodec = fodec;
	TVA = tVA;
	TotalTTC = totalTTC;
	NetAPayer = netaPayer;
}
public Facture(int codeClient, int quantite, String designation, Date date, float pU, float pt, float total,
		float fodec, float tVA, float totalTTC, float netaPayer) {
	super();
	CodeClient = codeClient;
	this.quantite = quantite;
	this.designation = designation;
	this.date = date;
	PU = pU;
	Pt = pt;
	Total = total;
	Fodec = fodec;
	TVA = tVA;
	TotalTTC = totalTTC;
	NetAPayer = netaPayer;
}
@Override
public String toString() {
	return "Facture [Designation=" + designation + "]";
}





}
