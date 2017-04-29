package findingneno.Job;

import org.apache.commons.lang3.StringUtils;

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

    @Override
    public String toString() {
	StringBuilder stringBuilder = new StringBuilder();
	if (StringUtils.isNotBlank(url)) {
	    stringBuilder.append(url);
	}
	if (StringUtils.isNotBlank(tag)) {
	    stringBuilder.append(tag);
	}
	if (StringUtils.isNotBlank(className)) {
	    stringBuilder.append(className);
	}
	if (StringUtils.isNotBlank(id)) {
	    stringBuilder.append(id);
	}
	if (StringUtils.isNotBlank(tagValue)) {
	    stringBuilder.append(tagValue);
	}
	return stringBuilder.toString();
    }
}
