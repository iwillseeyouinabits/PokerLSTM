package com.codebind;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
import java.util.Scanner;

public class Data {

	public float[] getData(Player p1, Player p2) {
		float[] inputs = new float[] {(float) (p1.getBets().get(p1.getBets().size()-1)/4000), (float) (p2.getBets().get(p2.getBets().size()-1)/4000), (float) (p1.getRanksTot().get(p1.getRanksTot().size()-1)/9), (float) (p1.getRanksHand().get(p1.getRanksHand().size()-1)/9), (float) (p1.getPots().get(p1.getPots().size()-1)/4000), (float) (p1.getMinBets().get(p1.getMinBets().size()-1)/4000), (float) (p1.getRound().get(p1.getRound().size()-1)/4)};
		return inputs;
	}

	public float[][][][] getFileData(int files, int minSeqLength, int numInput, int numOut) throws FileNotFoundException {
		float[][][][] output = new float[2][files][minSeqLength][];
		for(int file = 0; file < files; file++) {
			Scanner sc = new Scanner(new File("data_" + file + ".csv"));
			int itr = 0;
			while(sc.hasNext()) {
				String[] line = sc.nextLine().split(",");
				output[0][file][itr] = new float[line.length];
				for (int i = 0; i < line.length; i++) {
					output[0][file][itr][i] = Float.parseFloat(line[i]);
				}
				line = sc.nextLine().split(",");
				output[1][file][itr] = new float[line.length];
				for (int i = 0; i < line.length; i++) {
					output[1][file][itr][i] = Float.parseFloat(line[i]);
				}
				itr++;
			}
			for (int i = itr; i < minSeqLength; i++) {
				output[0][file][i] = new float[numInput];
				output[1][file][i] = new float[numOut];
			}
		}
		return output;
	}
}
