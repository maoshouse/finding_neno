package findingneno.Job;

public class Job {
    private final String id;
    private final String url;
    private final String tag;
    private final String tagValue;

    public Job(String id, String url, String tag, String tagValue) {
	this.id = id;
	this.url = url;
	this.tag = tag;
	this.tagValue = tagValue;
    }
}
