package Interface;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class StringSearchable implements Searchable<String,String>{

	private List<String> terms = new ArrayList<String>();

	public StringSearchable(List<String> terms){

		this.terms.addAll(terms);

	}

	public Collection<String> search(String value) {
		List<String> founds = new ArrayList<String>();
		for ( String s : terms ){
			if ( s.indexOf(value) == 0 ){
				founds.add(s);
			}
		}
		return founds;
	}

	

}