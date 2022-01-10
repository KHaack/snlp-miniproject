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
	 * @param model              The {@link Model} object.
	 * @param outputStreamWriter Filepath of the saved object.
	 */
	public static void writeResultsToFile(Model model, Writer outputStreamWriter) {
		model.write(outputStreamWriter, "N-TRIPLES");
	}

	/**
	 * Reads and parses sentences from file.
	 *
	 * @param inputStreamReader
	 * @return
	 */
	public static Set<Sentence> readFromFile(InputStreamReader inputStreamReader) {
		Set<Sentence> sentences = new HashSet<>();
		try (BufferedReader br = new BufferedReader(inputStreamReader)) {
			String line = br.readLine();
			// because we want to ignore the 1st row
			line = br.readLine();
			while (line != null) {
				if (line.isBlank()) {
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
