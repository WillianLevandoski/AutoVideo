package com.AutoVideo;

import java.util.ArrayList;

public class FrasesManuais {
	
	
	String cabecalho = "<speak xmlns=\"http://www.w3.org/2001/10/synthesis\" xmlns:mstts=\"http://www.w3.org/2001/mstts\" xmlns:emo=\"http://www.w3.org/2009/10/emotionml\" version=\"1.0\" xml:lang=\"en-US\">";
	String rodape = "</speak>";
	String[]  frases = new String[1000];
	ArrayList<String> frasesPronta = new ArrayList<String>();
	ArrayList<String> listaSSML = new ArrayList();
	StringBuilder ssml = new StringBuilder();
	Boolean acabou = false;

	
	String NARADOR_PADRAO = "<voice name=\"pt-BR-FranciscaNeural\"><prosody rate=\"-5%\" pitch=\"-4%\" contour=\"(20%, +20%) (80%, -20%)\">";
	String Powell = "<voice name=\"pt-BR-AntonioNeural\"><prosody rate=\"10%\" pitch=\"-10%\">";
	String Donovan = "<voice name=\"pt-BR-AntonioNeural\"><prosody rate=\"-15%\" pitch=\"10%\">";
	String robo = "<voice name=\"pt-BR-AntonioNeural\"><prosody rate=\"-20%\" pitch=\"-25%\">";
	String Speedy = "<voice name=\"pt-BR-AntonioNeural\"><prosody rate=\"-25%\" pitch=\"-22%\">";
	String Homem = "<voice name=\"pt-BR-AntonioNeural\"><prosody rate=\"0%\" pitch=\"-2%\">";
	String Sussan = "<voice name=\"pt-BR-FranciscaNeural\"><prosody rate=\"-7%\" pitch=\"+10%\">";


	
	String FIM_VOZ = "</prosody></voice>";
	String FIM_VOZ_CALMA = "</prosody></mstts:express-as></voice>";
	 
	

	
	public FrasesManuais(String senencas) {
		frases = senencas.toString().split("\\n");
		SSMLCreator();
	}
	
	
	public void SSMLCreator() {	
		Integer contador = 0;
		StringBuilder ssmlTemp = new StringBuilder();
		for (String frase : frases) {
			contador = contador + frase.length();
			
//			if(acabou && !frase.startsWith("<")) {
//				frase = "<voice Narrador>" + frase;
//				acabou = false;
//			}
			
			
			if(contador >1000 && frase.contains("</voice>")) {
				ssml.append(" "+replaceNarrador(frase));
				ssmlTemp.append(replaceNarrador(frase));	
				listaSSML.add(cabecalho + ssml.toString() + rodape);
				ssml = new StringBuilder();
				contador = 0;
			}else {
				ssml.append(" "+replaceNarrador(frase));
				ssmlTemp.append(replaceNarrador(frase));
			}
		}
		
		if(contador>0) {
			ssml.append(replaceNarrador(ssmlTemp.toString()));
			listaSSML.add(cabecalho + ssml.toString() + rodape);
			ssml = new StringBuilder();
		}
	}

	private String replaceNarrador(String frase) {
		if(frase.contains("<voice Narrador>")) {
			frase = " "+frase.replace("<voice Narrador>", NARADOR_PADRAO);
		}else if(frase.contains("<voice Powell>")) {
			frase =  " "+frase.replace("<voice Powell>", Powell);
		}else if(frase.contains("<voice Donovan>")) {
			frase =  " "+frase.replace("<voice Donovan>", Donovan);
		}else if(frase.contains("<voice Speedy>")) {
			frase = " "+ frase.replace("<voice Speedy>", Speedy);
		}else if(frase.contains("<voice robo>")) {
			frase = " "+ frase.replace("<voice robo>", robo);
		}else if(frase.contains("<voice Homem>")) {
			frase = " "+ frase.replace("<voice Homem>", Homem);
		}else if(frase.contains("<voice Sussan>")) {
			frase = " "+ frase.replace("<voice Sussan>", Sussan);
		}else{
			
		}
		
		if(frase.contains("</voi")) {
			frase = "</prosody></voice>";
			acabou = true;
		}
		
		
		return frase;
	}
	
	public ArrayList<String> getListaSSML(){
		return listaSSML;
	}
	
}
