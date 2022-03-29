package com.AutoVideo;

import java.util.ArrayList;
import java.util.Iterator;

public class SSMLCreator {

	StringBuilder ssml = new StringBuilder();
	ArrayList<String> listaSSML = new ArrayList();
	
	String cabecalho = "<speak xmlns=\"http://www.w3.org/2001/10/synthesis\" xmlns:mstts=\"http://www.w3.org/2001/mstts\" xmlns:emo=\"http://www.w3.org/2009/10/emotionml\" version=\"1.0\" xml:lang=\"en-US\">";
	String rodape = "</speak>";

	String NARADOR_PADRAO = "<voice name=\"pt-BR-FranciscaNeural\"><prosody rate=\"-5%\" pitch=\"-4%\" contour=\"(20%, +20%) (80%, -20%)\">";
	String NARADOR_2 = "<voice name=\"pt-BR-AntonioNeural\"><prosody rate=\"12%\" pitch=\"12%\" contour=\"(10%, +20%) (75%, -20%)\">";
	String NARADOR_1 = "<voice name=\"pt-BR-AntonioNeural\"><prosody rate=\"-12%\" pitch=\"-13%\" contour=\"(0%, -20%) (80%, +30%)\">";
	String FIM_VOZ = "</prosody></voice>";
	String FIM_VOZ_CALMA = "</prosody></mstts:express-as></voice>";
	
	String VOZ_1 = "<voice name=\"pt-BR-FranciscaNeural\"><prosody rate=\"-5%\" pitch=\"-4%\" contour=\"(20%, +30%) (80%, -20%)\">";
	String VOZ_2 = "<voice name=\"pt-BR-FranciscaNeural\"><prosody rate=\"-5%\" pitch=\"-4%\" contour=\"(15%, +30%) (60%, -20%) (85%, +20%)\">";
	String VOZ_3 = "<voice name=\"pt-BR-FranciscaNeural\"><prosody rate=\"-5%\" pitch=\"-4%\" contour=\"(20%, +20%) (40%, -10%) (60%, -20%) (85%, +30%)\">";
	String VOZ_4 = "<voice name=\"pt-BR-FranciscaNeural\"><prosody rate=\"-5%\" pitch=\"-4%\" contour=\"(10%, +30%) (25%, +20%) (40%, -20%) (65%, +20%) (90%, +25%)\">";
	String VOZ_5 = "<voice name=\"pt-BR-FranciscaNeural\"><prosody rate=\"-5%\" pitch=\"-4%\" contour=\"(10%, +25%) (20%, -20%) (45%, +30%) (65%, -20%) (80%, +10%) (90%, +20%)\">";
	String VOZ_6 = "<voice name=\"pt-BR-FranciscaNeural\"><prosody rate=\"-5%\" pitch=\"-4%\" contour=\"(5%, +25%) (20%, -20%) (35%, -20%) (50%, -10%) (65%, +15%) (80%, +10%) (90%, +20%)\">";
	String VOZ_7 = "<voice name=\"pt-BR-FranciscaNeural\"><prosody rate=\"-5%\" pitch=\"-4%\" contour=\"(0%, +20%) (15%, -10%) (30%, -20%) (45%, +10%) (60%, -30%) (75%, +10%) (85%, -15%) (90%, +20%)\">";
	String VOZ_8 = "<voice name=\"pt-BR-FranciscaNeural\"><prosody rate=\"-5%\" pitch=\"-4%\" contour=\"(0%, +20%) (10%, -20%) (25%, +20%) (40%, -10%) (55%, +30%) (65%, -30%) (75%, +25%) (85%, -30%) (95%, +20%)\">";
	String VOZ_9 = "<voice name=\"pt-BR-FranciscaNeural\"><prosody rate=\"-5%\" pitch=\"-4%\" contour=\"(5%, +15%) (10%, -20%) (20%, +20%) (35%, +25%) (50%, -40%) (50%, +20%) (65%, -10%) (75%, +20%) (85%, -10%) (90%, -30%)\">";
	String VOZ_10 = "<voice name=\"pt-BR-FranciscaNeural\"><prosody rate=\"-5%\" pitch=\"-4%\" contour=\"(0%, +20%) (5%, -20%) (15%, -20%) (25%, +10%) (35%, -10%) (45%, +10%) (55%, -10%) (65%, -30%) (75%, +20%) (85%, +15%) (95%, -10%)\">";

	public SSMLCreator(ArrayList<Frase> listaFrases) {	
		Integer contador = 0;
		ArrayList<Frase> frasesTemp = new ArrayList<Frase>();
		for (Frase frase : listaFrases) {
			contador = contador + frase.getSenteca().length();
			frasesTemp.add(frase);
			if(contador >1600) {
				String ssml = criarSSML(frasesTemp);
				listaSSML.add(ssml);
				frasesTemp = new ArrayList<Frase>();
				contador = 0;
			}
		}
		if(contador >0) {
			String ssml = criarSSML(frasesTemp);
			listaSSML.add(ssml);
			frasesTemp = new ArrayList<Frase>();
			contador = 0;
		}
	}
	
	public ArrayList<String> getListaSSML(){
		return listaSSML;
	}

	private String criarSSML(ArrayList<Frase> listaFrases) {
		StringBuilder ssml = new StringBuilder();

		ssml.append(cabecalho);
		for (Frase frase : listaFrases) {
			if (frase.getNarador().equals("padrao")) {
				ssml.append(getNarrador(frase.getSenteca()) + frase.getSenteca() + FIM_VOZ);
			} else if (frase.getNarador().equals("NARADOR_1")) {
				ssml.append(NARADOR_1 + frase.getSenteca() + FIM_VOZ);
			} else {
				ssml.append(NARADOR_2 + frase.getSenteca() + FIM_VOZ);
			}
		}
		ssml.append(rodape);
		return ssml.toString();

	}

	private String getNarrador(String senteca) {
		if(senteca.length()<100) {
			return VOZ_1;
		}else if(senteca.length()<200) {
			return VOZ_2;
		}else if(senteca.length()<400) {
			return VOZ_3;
		}else if(senteca.length()<600) {
			return VOZ_4;
		}else if(senteca.length()<900) {
			return VOZ_5;
		}else if(senteca.length()<1100) {
			return VOZ_6;
		}else if(senteca.length()<1400) {
			return VOZ_7;
		}else if(senteca.length()<1700) {
			return VOZ_8;
		}else if(senteca.length()<2000) {
			return VOZ_9;
		}else if(senteca.length()<2200) {
			return VOZ_10;
		}else {
			return VOZ_10;
		}
	}

}