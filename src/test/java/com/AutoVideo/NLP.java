package com.AutoVideo;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderFactory;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;

public class NLP {

	private static BufferedReader br = null;
    InputStream inputStream;



	public  ArrayList<String> sentence(String texto) {
		ArrayList<String> listaSetencas = new ArrayList<String>();
		try {
			InputStream is = new FileInputStream("/home/oem/Programacao/npl/pt-sent.bin");
			SentenceModel model = new SentenceModel(is);
			SentenceDetectorME sdetector = new SentenceDetectorME(model);
			String sentences[] = sdetector.sentDetect(texto);
			for (String str : sentences) {
				if (str != null) {
					listaSetencas.add(str);
					//partesDaFala(str);
					
				}
			}
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return listaSetencas;
	}

	public List<String> token(String texto) {
		List<String> listToken = new ArrayList<String>();
		InputStream modelIn = null;
		try {
			modelIn = new FileInputStream("/home/oem/Programacao/npl/pt-token.bin");
			TokenizerModel model = new TokenizerModel(modelIn);
			TokenizerME tokenizer = new TokenizerME(model);
			String tokens[] = tokenizer.tokenize(texto);
			double tokenProbs[] = tokenizer.getTokenProbabilities();

			for (int i = 0; i < tokens.length; i++) {
				listToken.add(tokens[i]);
				System.out.println(tokens[i] + "\t: " + tokenProbs[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		}
		return listToken;
	}
	
	
	public void partesDaFala(String sentence){
	    POSModel model = new POSModelLoader()
	            .load(new File("/home/oem/Programacao/npl/pt-pos-maxent.bin"));
	    POSTaggerME tagger = new POSTaggerME(model);

	    String tokens[] = WhitespaceTokenizer.INSTANCE
	            .tokenize(sentence);
	    String[] tags = tagger.tag(tokens);

	    POSSample sample = new POSSample(tokens, tags);
	    String posTokens[] = sample.getSentence();
	    String posTags[] = sample.getTags();
	    System.out.print(sentence);


	    for (int i = 0; i < tokens.length; i++) {
	        System.out.print(tokens[i] + "[" + tags[i] + "] ");
	        System.out.println();
	    }
	    System.out.println();
	}
	
    public  void nameFinderExample(String sentenca) {
        try {
      
            Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
            TokenNameFinderModel model = new TokenNameFinderModel(new File("/home/oem/Programacao/npl/pt-ner-person.bin"));
            NameFinderME finder = new NameFinderME(model);
                String[] tokens = tokenizer.tokenize(sentenca);

                // Find the names in the tokens and return Span objects
                Span[] nameSpans = finder.find(tokens);
                
                for (Span s : nameSpans) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = s.getStart(); i < s.getEnd(); i++) {
                        stringBuilder.append(tokens[i]);
                    }
                    if(s.getProb()>=0.75)
                    	System.out.println(stringBuilder.toString() + " " + s.getProb());
                }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
		
	
    

	
	
    
	private  File trainModel(){
		 ObjectStream<String> lineStream;
		try {
			lineStream = new PlainTextByLineStream(new MarkableFileInputStreamFactory(
			     new File("/home/oem/Programacao/npl/IdentificadorNoem.txt")),
			     StandardCharsets.UTF_8);

		 TrainingParameters params = new TrainingParameters();
		 params.put(TrainingParameters.ITERATIONS_PARAM, 70);
		 params.put(TrainingParameters.CUTOFF_PARAM, 1);
		 
		 TokenNameFinderModel model;
		 TokenNameFinderFactory nameFinderFactory = new TokenNameFinderFactory();
		 try (ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream)) {
		  model = NameFinderME.train("pt", null, sampleStream, params, nameFinderFactory);
		 }
		 
		 File modelFile = File.createTempFile("model", ".bin");
		 
		 try (BufferedOutputStream modelOut =
		      new BufferedOutputStream(new FileOutputStream(modelFile))) {
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
