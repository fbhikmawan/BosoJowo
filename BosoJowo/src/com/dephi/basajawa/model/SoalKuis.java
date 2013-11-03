/**
 * 
 */
package com.dephi.basajawa.model;

import java.io.Serializable;

/**
 * @author chul.moveon
 *
 */
public class SoalKuis implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -674593748154457194L;
	
	int SoalNomor;
	String Pertanyaan;
	String[] Opsi;
	int[] jawaban;
	
	
	public int getSoalNomor() {
		return SoalNomor;
	}
	public void setSoalNomor(int soalNomor) {
		SoalNomor = soalNomor;
	}
	public String getPertanyaan() {
		return Pertanyaan;
	}
	public void setPertanyaan(String pertanyaan) {
		Pertanyaan = pertanyaan;
	}
	public String[] getOpsi() {
		return Opsi;
	}
	public void setOpsi(String[] opsi) {
		Opsi = opsi;
	}
	public int[] getJawaban() {
		return jawaban;
	}
	public void setJawaban(int[] jawaban) {
		jawaban = jawaban;
	}
	
	
}
