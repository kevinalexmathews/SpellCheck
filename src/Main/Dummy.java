package Main;


public class Dummy {

	String str;
	Double posterior;
	Long word_count;
	Double prior_prob;
	Double likelihood;
	Integer edit_distance;
	
	Dummy(String str, Integer edit_distance, Long word_count, Double prior_prob, Double likelihood, Double posterior)	{
		this.str = str;
		this.edit_distance = edit_distance;
		this.word_count = word_count;
		this.prior_prob = prior_prob;
		this.likelihood = likelihood;
		this.posterior = posterior;
	}	
}
