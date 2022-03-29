package com.AutoVideo;

import java.awt.List;
import java.util.ArrayList;

public class ClassificadorFrase {

	public ArrayList<Frase> lsFrase = new ArrayList<Frase>();
	Frase frase;
	static String NARADOR_PADRAO = "padrao";
	Narradores_dialogos ultimoNarador = Narradores_dialogos.NARADOR_2;
	private Integer ultimoId = 0;

	public enum Narradores_dialogos {
		NARADOR_1, NARADOR_2;
	}

	public ClassificadorFrase(ArrayList<String> lsSenencas) {
		for (String sentenca : lsSenencas) {
			defineNarrador(sentenca);
		}
	}

	public void defineNarrador(String senteca, String narador) {
		frase = new Frase(senteca);
		if (narador.isEmpty()) {
			definirNarador();
		} else {
			criarFrases(senteca, narador);
		}
	}

	public void defineNarrador(String senteca) {
		defineNarrador(senteca, "");
	}

	public void definirNarador() {
		if (frase.getSenteca().startsWith("- ")) {
			if (frase.getSenteca().contains(" - ")) {
				if (frase.getSenteca().startsWith("- (")) {
					String[] sentecas = frase.getSenteca().split(" - ");
					criarFrases(sentecas[0], NARADOR_PADRAO);
					String sequneciaFala = getSequenciaSplit(sentecas);
					defineNarrador(sequneciaFala, getProximoNarador());
				} else {
					String[] sentecas = frase.getSenteca().split(" - ");
					criarFrases(sentecas[0], getProximoNarador());
					String sequneciaFala = getSequenciaSplit(sentecas);
					defineNarrador(sequneciaFala);
				}
			} else {
				criarFrases(frase.getSenteca(), getProximoNarador());
			}
		} else {
			if (frase.getSenteca().contains("- ")) {
				String[] sentecas = frase.getSenteca().split(" - ");
				defineNarrador(sentecas[0], NARADOR_PADRAO);
				if (sentecas.length > 1) {
					String sequneciaFala = getSequenciaSplit(sentecas);
					if(Character.isUpperCase(sequneciaFala.charAt(0))) {
						defineNarrador(sequneciaFala, getProximoNarador());
					}else {
						defineNarrador(sequneciaFala, NARADOR_PADRAO);
					}
					
				}
			} else {
				criarFrases(frase.getSenteca(), NARADOR_PADRAO);
			}
		}
	}

	private String getSequenciaSplit(String[] sentecas) {
		String frase = sentecas[1];
		if (sentecas.length > 1) {
			for (int i = 2; i <= sentecas.length - 1; i++) {
				frase = frase + " - " + sentecas[i];
			}
		}
		if (Character.isUpperCase(frase.charAt(0)))
			frase = "- " + frase;
		return frase;
	}

	private void criarFrases(String senteca, String narador) {
		frase.setSenteca(senteca);
		frase.setNarador(narador);
		if (verificaConcatenaFrase()) {
			frase.setSenteca("");
			frase.setNarador("");
		} else {
			setId();
			lsFrase.add(frase);
		}
	}

	private boolean verificaConcatenaFrase() {
		if (lsFrase.size() > 0) {
			Frase ultimaFraseSalva = lsFrase.get(lsFrase.size() - 1);
			if (ultimaFraseSalva.getNarador().equals(frase.getNarador())) {
				String fraseConcatenada = lsFrase.get(lsFrase.size() - 1).getSenteca() + "<break time=\"1s\"/> "
						+ frase.getSenteca();
				lsFrase.get(lsFrase.size() - 1).setSenteca(fraseConcatenada);
				return true;
			}
		}
		return false;
	}

	private String getProximoNarador() {
		if (ultimoNarador.equals(Narradores_dialogos.NARADOR_1)) {
			ultimoNarador = Narradores_dialogos.NARADOR_2;
			return Narradores_dialogos.NARADOR_2.toString();
		} else {
			ultimoNarador = Narradores_dialogos.NARADOR_1;
			return Narradores_dialogos.NARADOR_1.toString();
		}
	}

	private void setId() {
		ultimoId = ultimoId + 1;
		frase.setId(ultimoId);
	}

	public ArrayList<Frase> getLsFrase() {
		return lsFrase;
	}

}
