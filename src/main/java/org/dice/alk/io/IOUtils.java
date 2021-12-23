package org.dice.alk.io;

import org.apache.jena.rdf.model.Model;
import org.dice.alk.model.Sentence;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Input and output util methods.
 */
public class IOUtils {

	/**
	 * Saves a {@link Model} object to a N-Triple file.
	 *
	 * @param model    The {@link Model} object.
	 * @param savePath Filepath of the saved object.
	 */
	public static void writeResultsToFile(Model model, String savePath) {
		try (FileWriter out = new FileWriter(savePath)) {
			model.write(out, "NT");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads and parses sentences from file.
	 *
	 * @param inputFile
	 * @return
	 */
	public static Set<Sentence> readFromFile(String inputFile) {
		Set<Sentence> sentences = new HashSet<>();
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
			String line = br.readLine();
			// because we want to ignore the 1st row FIXME
			line = br.readLine();
			while (line != null) {
				if(line.isBlank()) {
					continue;
				}
				String[] elements = line.split("\t");
				sentences.add(Sentence.createSentence(elements));
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sentences;
	}
}