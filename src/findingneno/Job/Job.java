package findingneno.Job;

import lombok.experimental.Value;

@Value
public class Job {
    private final String id;
    private final String className;
    private final String url;
    private final String tag;
    private final String tagValue;

    public Job(String id, String className, String url, String tag, String tagValue) {
	this.id = id;
	this.className = className;
	this.url = url;
	this.tag = tag;
	this.tagValue = tagValue;
    }
}
