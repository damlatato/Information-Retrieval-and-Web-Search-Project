package Interface;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Demonstration{

        public static void main(String[] args) throws Exception{

		SwingUtilities.invokeAndWait(new Runnable(){

			public void run() {

				List<String> myWords = new ArrayList<String>();

				FileReader input = null;
				String myLine = null;

				try {
					input = new FileReader("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\terms\\Vocabulary_terms.txt");
					BufferedReader bufRead = new BufferedReader(input);
					while ( (myLine = bufRead.readLine()) != null)
					{
						myWords.add(myLine);
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				myWords.add("bike");
//
//				myWords.add("car");
//
//				myWords.add("cap");
//
//				myWords.add("cape");
//
//				myWords.add("canadian");
//
//				myWords.add("caprecious");
//
//				myWords.add("catepult");

				StringSearchable searchable = new StringSearchable(myWords);
				AutocompleteJComboBox combo = new AutocompleteJComboBox(searchable);

				JFrame frame = new JFrame();
				frame.add(combo);
				frame.pack();
				frame.setSize(100, 100);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});

        }

}
