package com.AutoVideo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameFinderME;

import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderFactory;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class AudioLivro {

	private List<Frase> lsFrases;
	private static boolean frasesClassificadas = true;

	public static void main(String[] argvs) {

		
		
		File arquivo = new File("/home/oem/AudioLivro/Eu robo/0.txt");
		StringBuilder text = Utils.lerArquivo(arquivo);
		
		
		ArrayList<String> lsFrases = new ArrayList<String>();
		if(frasesClassificadas) {
			lsFrases = separarFrasesManuais(text);

			Integer contador = 0; 
			for(String str : lsFrases) {
				System.out.println(str);

				AzureReader azure = new AzureReader();
				contador = contador+1;
				System.out.println(contador);

				
				String path = azure.gravarVoz(str, "fundacao-0"+contador);
				System.out.println(path);
			}
		}else {
			lsFrases = separarFrasesAutomatica(text);	
			
			ClassificadorFrase classificador = new ClassificadorFrase(lsFrases);
			
			//for(Frase frase : classificador.getLsFrase()) {
				//System.out.println(frase.getSenteca());
			//}
			
			SSMLCreator ssml = new SSMLCreator(classificador.getLsFrase());
			Integer contador = 122; 
			for(String str : ssml.getListaSSML()) {
				AzureReader azure = new AzureReader();
				contador = contador+1;
				String path = azure.gravarVoz(str, "fundacao-0"+contador);
				System.out.println(path);
			}
		}
		
		ClassificadorFrase classificador = new ClassificadorFrase(lsFrases);
		
		//for(Frase frase : classificador.getLsFrase()) {
			//System.out.println(frase.getSenteca());
		//}
//		
//		SSMLCreator ssml = new SSMLCreator(classificador.getLsFrase());
//		Integer contador = 122; 
//		for(String str : ssml.getListaSSML()) {
//			AzureReader azure = new AzureReader();
//			contador = contador+1;
//			String path = azure.gravarVoz(str, "fundacao-0"+contador);
//			System.out.println(path);
//		}

	}

	private static ArrayList<String> separarFrasesManuais(StringBuilder text) {
		FrasesManuais fraseManual = new FrasesManuais(text.toString());
		return fraseManual.getListaSSML();
	}

	private static ArrayList<String> separarFrasesAutomatica(StringBuilder text) {
		ArrayList<String> lsFrases = new ArrayList<String>();
		String[] linhas = text.toString().split("\\n");
		for (String linha : linhas) {
			if (!linha.isEmpty()) {
				String ultimaLinha = "";
				if (lsFrases.size() > 0)
					ultimaLinha = lsFrases.get(lsFrases.size() - 1);
				if (!ultimaLinha.isEmpty()) {
					if (lsFrases.size() > 0 && !(ultimaLinha.endsWith(".") || ultimaLinha.endsWith("?") || ultimaLinha.endsWith("!") || ultimaLinha.endsWith(":"))) {
						lsFrases.set(lsFrases.size()-1, ultimaLinha + " "+linha);
					} else {
						lsFrases.add(linha);
					}
				}else {
					lsFrases.add(linha);
				}
			}
		}
		return lsFrases;
	}

	private File trainModel() {
		ObjectStream<String> lineStream;
		try {
			lineStream = new PlainTextByLineStream(
					new MarkableFileInputStreamFactory(new File("/home/oem/Programacao/npl/IdentificadorNoem.txt")),
					StandardCharsets.UTF_8);

			TrainingParameters params = new TrainingParameters();
			params.put(TrainingParameters.ITERATIONS_PARAM, 70);
			params.put(TrainingParameters.CUTOFF_PARAM, 1);

			TokenNameFinderModel model;
			TokenNameFinderFactory nameFinderFactory = new TokenNameFinderFactory();
			try (ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream)) {
				model = NameFinderME.train("eng", null, sampleStream, params, nameFinderFactory);
			}

			File modelFile = File.createTempFile("model", ".bin");

			try (BufferedOutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelFile))) {
				model.serialize(modelOut);
			}

			return modelFile;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
